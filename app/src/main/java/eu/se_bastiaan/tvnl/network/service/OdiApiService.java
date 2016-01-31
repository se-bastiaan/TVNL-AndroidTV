package eu.se_bastiaan.tvnl.network.service;

import eu.se_bastiaan.tvnl.model.OdiData;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Single;

public interface OdiApiService {

    @GET
    Single<OdiData> getData(@Url String url, @Query("extension") String extension);

    @GET
    Single<OdiData> getData(@Url String url);

}
