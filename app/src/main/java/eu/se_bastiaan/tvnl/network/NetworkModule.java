package eu.se_bastiaan.tvnl.network;

import android.os.Build;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.se_bastiaan.tvnl.BuildConfig;
import eu.se_bastiaan.tvnl.Constants;
import eu.se_bastiaan.tvnl.network.service.LivestreamsApiService;
import eu.se_bastiaan.tvnl.network.service.OdiApiService;
import eu.se_bastiaan.tvnl.network.service.RadioboxApiService;
import eu.se_bastiaan.tvnl.network.service.UGApiService;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

@Module
public class NetworkModule {

    @Provides
    @NonNull
    @Singleton
    public UGApiService provideUGApiService(Retrofit.Builder retrofit) {
        return retrofit.build().create(UGApiService.class);
    }

    @Provides
    @NonNull
    @Singleton
    public OdiApiService provideOdiApiService(Retrofit.Builder retrofit) {
        return retrofit.build().create(OdiApiService.class);
    }

    @Provides
    @NonNull
    @Singleton
    public RadioboxApiService provideRadioboxApiService(Retrofit.Builder retrofit) {
        return retrofit.baseUrl(Constants.API_URL_RADIOBOX).build().create(RadioboxApiService.class);
    }

    @Provides
    @NonNull
    @Singleton
    public LivestreamsApiService provideLivestreamsApiService(OkHttpClient.Builder httpClientBuilder, Retrofit.Builder retrofit) {
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (request.url().toString().contains("livestream")) {
                    HttpUrl url = request.url().newBuilder().addQueryParameter("type", "json").addQueryParameter("protection", "url").build();
                    request = request.newBuilder().url(url).build();
                }
                return chain.proceed(request);
            }
        });

        return retrofit.client(httpClientBuilder.build()).build().create(LivestreamsApiService.class);
    }

    @Provides
    @NonNull
    public OkHttpClient.Builder provideOkHttpClientBuilder() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Request.Builder requestBuilder = request.newBuilder();
                requestBuilder.addHeader("User-Agent", String.format(Locale.US, "NPOxUG/%s AndroidTV/%s %s/%s", BuildConfig.VERSION_NAME, Build.VERSION.RELEASE, Build.MANUFACTURER, Build.MODEL));

                return chain.proceed(requestBuilder.build());
            }
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(interceptor);

        return httpClientBuilder;
    }

    @Provides
    @NonNull
    @Singleton
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder httpClientBuilder) {
        return httpClientBuilder.build();
    }

    @Provides
    @NonNull
    @Singleton
    public Retrofit.Builder provideRetrofitBuilder(OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ").create();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .validateEagerly(true)
                .baseUrl(Constants.API_URL);
    }

}
