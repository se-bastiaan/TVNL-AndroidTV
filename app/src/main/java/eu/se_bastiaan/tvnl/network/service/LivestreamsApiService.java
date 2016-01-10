package eu.se_bastiaan.tvnl.network.service;

import retrofit.http.GET;
import retrofit.http.Url;
import rx.Single;

public interface LivestreamsApiService {

    @GET
    Single<String> getRealUrl(@Url String url);

}
