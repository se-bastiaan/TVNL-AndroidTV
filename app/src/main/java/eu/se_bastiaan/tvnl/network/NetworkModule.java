package eu.se_bastiaan.tvnl.network;

import android.os.Build;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

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
    public LivestreamsApiService provideLivestreamsApiService(OkHttpClient httpClient, Retrofit.Builder retrofit) {
        OkHttpClient okHttpClient = httpClient.clone();
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (request.urlString().contains("livestream")) {
                    HttpUrl url = request.httpUrl().newBuilder().addQueryParameter("type", "json").addQueryParameter("protection", "url").build();
                    request = request.newBuilder().url(url).build();
                }
                return chain.proceed(request);
            }
        });

        return retrofit.client(okHttpClient).build().create(LivestreamsApiService.class);
    }

    @Provides
    @NonNull
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient httpClient = new OkHttpClient();

        httpClient.interceptors()
                .add(new Interceptor() {
                    @Override
                    public com.squareup.okhttp.Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request();

                        Request.Builder requestBuilder = request.newBuilder();
                        requestBuilder.addHeader("User-Agent", String.format(Locale.US, "NPOxUG/%s AndroidTV/%s %s/%s", BuildConfig.VERSION_NAME, Build.VERSION.RELEASE, Build.MANUFACTURER, Build.MODEL));

                        return chain.proceed(requestBuilder.build());
                    }
                });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors()
                .add(interceptor);

        return httpClient;
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
                .validateEagerly()
                .baseUrl(Constants.API_URL);
    }

}
