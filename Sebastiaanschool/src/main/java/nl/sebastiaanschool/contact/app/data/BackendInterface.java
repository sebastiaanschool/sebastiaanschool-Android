package nl.sebastiaanschool.contact.app.data;

import android.content.Context;

import com.squareup.moshi.Moshi;

import nl.sebastiaanschool.contact.app.BuildConfig;
import nl.sebastiaanschool.contact.app.data.server.BackendApi;
import nl.sebastiaanschool.contact.app.data.server.TimelineJsonConverter;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Singleton provider/accessor for our backend connection.
 */
public class BackendInterface {

    private static final long CACHE_SIZE_BYTES = 512 * 1024;
    private static BackendInterface instance;

    public final BackendApi connector;

    public static synchronized BackendInterface getInstance(Context context) {
        if (instance == null) {
            instance = new BackendInterface(context);
        }
        return instance;
    }

    private BackendInterface(Context context) {
        final Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE_BYTES);
        final OkHttpClient okHttp = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        final Moshi moshi = new Moshi.Builder()
                .add(new TimelineJsonConverter())
                .build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.ENDPOINT_URL)
                .client(okHttp)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        connector = retrofit
                .create(BackendApi.class);
    }
}
