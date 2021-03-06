package nl.sebastiaanschool.contact.app.data.server;

import android.content.Context;
import android.util.Log;

import com.squareup.moshi.Moshi;

import java.io.File;
import java.io.IOException;

import nl.sebastiaanschool.contact.app.BuildConfig;
import nl.sebastiaanschool.contact.app.data.GrabBag;
import nl.sebastiaanschool.contact.app.data.downloadmanager.Download;
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

    private final BackendApi backendApi;
    private final NotificationApi notificationApi;
    private final OkHttpClient okHttpClient;

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new BackendInterface(context.getApplicationContext().getCacheDir(),
                    GrabBag.constructUserAgent(context));
        }
    }

    public static synchronized BackendInterface getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not initialised.");
        }
        return instance;
    }

    private BackendInterface(File cacheDir, final String userAgent) {
        final Cache cache = new Cache(cacheDir, CACHE_SIZE_BYTES);
        okHttpClient = addRequestLoggingTo(new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(chain.request().newBuilder()
                                .header("User-Agent", userAgent)
                                .build());
                    }
                }))
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
        backendApi = retrofit
                .create(BackendApi.class);
        notificationApi = retrofit
                .create(NotificationApi.class);
    }

    private static OkHttpClient.Builder addRequestLoggingTo(OkHttpClient.Builder builder) {
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    Log.i("SebApp", request.method() + " " + request.url()
                            + " -> " + response.code());
                    return response;
                }
            });
        }
        return builder;
    }

    /**
     * Returns a cold observable that yields the uncompressed content-length of the given URL.
     * @param target an URL.
     * @return its content-length, or {@code -1} if unknown.
     */
    public Single<Download> getDownloadSize(final Download target) {
        return Single.create(new Single.OnSubscribe<Download>() {
            @Override
            public void call(final SingleSubscriber<? super Download> subscriber) {
                Request req = new Request.Builder()
                        .head()
                        .url(HttpUrl.parse(target.remoteUrl))
                        .addHeader("Accept", "*/*")
                        .addHeader("Accept-Encoding", "identity") // Disable gzip, or OkHttp discards Content-Length.
                        .build();
                // We use the async API so that getDownloadSize()s can execute in parallel when we
                // run through the items in the timeline response.
                okHttpClient.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // We're not propagating the error, simply "size unknown".
                        subscriber.onSuccess(target.withSizeInBytes(Download.SIZE_UNKNOWN));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // We're not interested in the response body, there shouldn't even be any.
                        response.body().close();
                        final String contentLength = response.header("Content-Length", "-1");
                        long sizeInBytes;
                        try {
                            sizeInBytes = Long.parseLong(contentLength);
                        } catch (NumberFormatException e) {
                            sizeInBytes = -1;
                        }
                        subscriber.onSuccess(target.withSizeInBytes(sizeInBytes));
                    }
                });
            }
        });
    }

    public BackendApi getBackendApi() {
        return backendApi;
    }

    public NotificationApi getNotificationApi() {
        return notificationApi;
    }
}
