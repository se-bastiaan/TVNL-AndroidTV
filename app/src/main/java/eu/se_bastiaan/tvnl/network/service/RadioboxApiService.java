package eu.se_bastiaan.tvnl.network.service;

import eu.se_bastiaan.tvnl.model.RadioboxAudiofragment;
import eu.se_bastiaan.tvnl.model.RadioboxBroadcast;
import eu.se_bastiaan.tvnl.model.RadioboxChannel;
import eu.se_bastiaan.tvnl.model.RadioboxSearch;
import eu.se_bastiaan.tvnl.model.RadioboxTrack;
import eu.se_bastiaan.tvnl.model.RadioboxVideostream;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Single;

public interface RadioboxApiService {

    @GET("broadcast/search.json")
    Single<RadioboxSearch<RadioboxBroadcast>> broadcastSearch(@Query("q") String query, @Query("max-results") Integer limit, @Query("order") String order);

    @GET("track/search.json")
    Single<RadioboxSearch<RadioboxTrack>> trackSearch(@Query("q") String query, @Query("max-results") Integer limit, @Query("order") String order);

    @GET("videostream/search.json")
    Single<RadioboxSearch<RadioboxVideostream>> videostreamSearch(@Query("q") String query, @Query("max-results") Integer limit, @Query("order") String order);

    @GET("audiofragment/search.json")
    Single<RadioboxSearch<RadioboxAudiofragment>> audiofragmentSearch(@Query("q") String query, @Query("max-results") Integer limit, @Query("order") String order);

    @GET("data/radiobox2/nowonair/{channel}.json")
    Single<Object> getNowOnAirByChannel(@Path("channel") Integer channel);

    @GET("channel/rest/{channel}.json")
    Single<RadioboxChannel> getChannelData(@Path("channel") Integer channel);

    class QueryBuilder {

        private StringBuilder stringBuilder;

        public QueryBuilder() {
            stringBuilder = new StringBuilder();
            stringBuilder.append(' ');
        }

        public QueryBuilder equals(String key, Object value) {
            stringBuilder.append(key);
            stringBuilder.append(":");
            stringBuilder.append('\'');
            stringBuilder.append(value);
            stringBuilder.append('\'');
            stringBuilder.append(' ');
            return this;
        }

        public QueryBuilder greaterThan(String key, Object value) {
            stringBuilder.append(key);
            stringBuilder.append(">");
            stringBuilder.append('\'');
            stringBuilder.append(value);
            stringBuilder.append('\'');
            stringBuilder.append(' ');
            return this;
        }

        public QueryBuilder lowerThan(String key, Object value) {
            stringBuilder.append(key);
            stringBuilder.append("<");
            stringBuilder.append('\'');
            stringBuilder.append(value);
            stringBuilder.append('\'');
            stringBuilder.append(' ');
            return this;
        }

        public QueryBuilder subquery(QueryBuilder queryBuilder) {
            stringBuilder.append('(');
            stringBuilder.append(queryBuilder.build());
            stringBuilder.append(')');
            stringBuilder.append(' ');
            return this;
        }

        public QueryBuilder and() {
            stringBuilder.append("AND");
            stringBuilder.append(' ');
            return this;
        }

        public QueryBuilder or() {
            stringBuilder.append("OR");
            stringBuilder.append(' ');
            return this;
        }

        public String build() {
            return stringBuilder.toString();
        }

        public static class Prebuilts {

            public static String getCurrentDataByChannel(Object channel) {
                return new QueryBuilder().equals("channel.id", channel).and().subquery(new QueryBuilder().greaterThan("stopdatetime", "NOW").and().lowerThan("startdatetime", "NOW")).build();
            }

            public static String getCurrentDataForMainChannels() {
                return new QueryBuilder().greaterThan("channel.id", 0).and().lowerThan("channel.id", 8).and().subquery(new QueryBuilder().greaterThan("stopdatetime", "NOW").and().lowerThan("startdatetime", "NOW")).build();
            }

            public static String getNextDataByChannel(Object channel) {
                return new QueryBuilder().equals("channel.id", channel).and().subquery(new QueryBuilder().greaterThan("stopdatetime", "NOW").and().lowerThan("startdatetime", "NOW").or().greaterThan("startdatetime", "NOW")).build();
            }

            public static String getUnprotectedDataByChannel(Object channel) {
                return new QueryBuilder().equals("channel.id", channel).and().equals("protected", 0).build();
            }

            public static String getAvailableDataByChannel(Object channel) {
                return new QueryBuilder().equals("channel.id", channel).and().equals("available", 1).build();
            }

        }

    }

}
