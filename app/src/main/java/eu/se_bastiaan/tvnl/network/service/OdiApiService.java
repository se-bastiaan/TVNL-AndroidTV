package eu.se_bastiaan.tvnl.network.service;

import eu.se_bastiaan.tvnl.model.OdiData;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.Url;
import rx.Single;

public interface OdiApiService {

    @GET
    Single<OdiData> getData(@Url String url, @Query("extension") String extension);

    @GET
    Single<OdiData> getData(@Url String url);

}
