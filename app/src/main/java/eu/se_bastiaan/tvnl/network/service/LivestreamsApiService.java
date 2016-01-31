package eu.se_bastiaan.tvnl.network.service;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Single;

public interface LivestreamsApiService {

    @GET
    Single<String> getRealUrl(@Url String url);

}
