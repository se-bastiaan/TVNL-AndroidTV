package eu.se_bastiaan.tvnl.network.service;

import java.util.List;

import eu.se_bastiaan.tvnl.model.Broadcast;
import eu.se_bastiaan.tvnl.model.EncryptedStreamData;
import eu.se_bastiaan.tvnl.model.Episode;
import eu.se_bastiaan.tvnl.model.MainChannelsGuide;
import eu.se_bastiaan.tvnl.model.Promo;
import eu.se_bastiaan.tvnl.model.Recommendation;
import eu.se_bastiaan.tvnl.model.Series;
import eu.se_bastiaan.tvnl.model.ThemeChannelsGuide;
import eu.se_bastiaan.tvnl.model.Timeline;
import eu.se_bastiaan.tvnl.model.VideoFragment;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Single;

public interface UGApiService {

    class PubOption {
        public static final String DOWNLOAD = "h264_std";
        public static final String ADAPTIVE = "adaptive";
    }

    @GET("tips.json")
    Single<List<Recommendation>> getRecommendations();

    @GET("timeline.json")
    Single<List<Timeline>> getTimeline();

    @GET("broadcasts/recent.json")
    Single<List<Broadcast>> getLatestBroadcasts();

    @GET("broadcasts/{date}.json")
    Single<List<Broadcast>> getBroadcastsByDate(@Path("date") String date);

    @GET("episodes/popular.json")
    Single<List<Episode>> getPopularEpisodes();

    @GET("episodes/trending.json")
    Single<List<Episode>> getTrendingEpisodes();

    @GET("episodes/{mid}.json")
    Single<Episode> getEpisodeById(@Path("mid") String id);

    @GET("episodes/genre/{genre}.json")
    Single<Episode> getEpisodeByGenre(@Path("genre") String genre);

    @GET("episodes/search/{keyword}.json")
    Single<List<Episode>> getEpisodesBySearch(@Path("keyword") String keyword);

    @GET("episodes/series/{mid}/latest.json")
    Single<Episode> getLatestEpisodesBySeriesId(@Path("mid") String id);

    @POST("episodes/{mid}/view.json")
    Single<Object> postEpisodeView(@Path("mid") String id, @Body String string);

    @GET("series.json")
    Single<List<Series>> getSeriesIndex();

    @GET("series/popular.json")
    Single<List<Series>> getPopularSeries();

    @GET("series/{seriesId}.json")
    Single<Series> getSeries(@Path("seriesId") String seriesId);

    @GET("series/suggest/{keyword}.json")
    Single<List<Series>> getSeriesBySuggestion(@Path("keyword") String keyword);

    @GET("fragments/trending.json")
    Single<List<VideoFragment>> getTrendingFragments();

    @GET("fragments/{mid}/view.json")
    Single<List<VideoFragment>> postFragmentView(@Path("mid") String id);

    @GET("promos.json")
    Single<List<Promo>> getPromos();

    @GET("odi_plus/android/{mid}.json")
    Single<EncryptedStreamData> getOdiData(@Path("mid") String id, @Query("pub_option") String option);

    @GET("guide/{date}.json")
    Single<MainChannelsGuide> getMainChannelsGuide(@Path("date") String date);

    @GET("guide/thema/{date}.json")
    Single<ThemeChannelsGuide> getThemeChannelsGuide(@Path("date") String date);

    @GET("live/android/{channelId}.json")
    Single<EncryptedStreamData> getLiveDataByChannel(@Path("channelId") String id, @Query("extension") String extension);

}
