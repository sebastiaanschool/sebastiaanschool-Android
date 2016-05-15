package nl.sebastiaanschool.contact.app.data;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.squareup.moshi.Moshi;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import nl.sebastiaanschool.contact.app.BuildConfig;
import nl.sebastiaanschool.contact.app.data.server.AgendaJsonConverter;
import nl.sebastiaanschool.contact.app.data.server.BackendApi;
import nl.sebastiaanschool.contact.app.data.server.TimelineJsonConverter;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import rx.Single;
import rx.SingleSubscriber;

/**
 * Singleton provider/accessor for our backend connection.
 */
public class BackendInterface {

    private static final long CACHE_SIZE_BYTES = 512 * 1024;
    private static BackendInterface instance;

    public final BackendApi connector;
    private final OkHttpClient okHttpClient;

    public static synchronized void init(Context context) {
        if (instance != null) {
            throw new IllegalStateException("Already initialised.");
        }
        instance = new BackendInterface(context.getCacheDir(), constructUserAgent(context));
    }

    public static synchronized BackendInterface getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not initialised.");
        }
        return instance;
    }

    private BackendInterface(File cacheDir, final String userAgent) {
        final Cache cache = new Cache(cacheDir, CACHE_SIZE_BYTES);
        okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(chain.request().newBuilder()
                                .header("User-Agent", userAgent)
                                .build());
                    }
                })
                .build();
        final Moshi moshi = new Moshi.Builder()
                .add(new TimelineJsonConverter())
                .add(new AgendaJsonConverter())
                .build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.ENDPOINT_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        connector = retrofit
                .create(BackendApi.class);
    }

    /**
     * Returns a cold observable that yields the content-length of the given URL.
     * @param target an URL.
     * @return its content-length, or {@code -1} if unknown.
     */
    public Single<Long> getDownloadSize(final HttpUrl target) {
        return Single.create(new Single.OnSubscribe<Long>() {
            @Override
            public void call(final SingleSubscriber<? super Long> subscriber) {
                Request req = new Request.Builder()
                        .head()
                        .url(target)
                        .addHeader("Accept", "application/pdf,image/*")
                        .build();
                okHttpClient.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("BINF", "Download size error: " + e + " of " + target);
                        subscriber.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // We're not interested in the response body, there shouldn't even be any.
                        response.body().close();
                        final String contentLength = response.header("Content-Length", "-1");
                        long result;
                        try {
                            result = Long.parseLong(contentLength);
                        } catch (NumberFormatException e) {
                            Log.d("BINF", "Download size: NFE on " + contentLength + " of " + target);
                            result = -1;
                        }
                        Log.d("BINF", "Download size: " + result + " of " + target);
                        subscriber.onSuccess(result);
                    }
                });
            }
        });
    }

    /**
     * Creates a user-agent string that should generally be RFC compliant. If the device constants
     * contain context-invalid characters these are used as-is and RFC compliance will suffer.
     * @param context used to obtain some device data.
     * @return e.g. "Sebastiaanschool/2.0.0 (LGE; Nexus 5; Android 6.1 SDK 23; nl_NL)"
     */
    private static String constructUserAgent(Context context) {
        return  new StringBuilder(256)
                .append("Sebastiaanschool/").append(BuildConfig.VERSION_NAME)
                .append(" (").append(Build.MANUFACTURER).append("; ").append(Build.MODEL)
                .append("; Android ").append(Build.VERSION.RELEASE)
                .append(" SDK ").append(Build.VERSION.SDK_INT)
                .append("; ").append(context.getResources().getConfiguration().locale)
                .append(')')
                .toString();
    }
}
