package eu.se_bastiaan.tvnl.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.inject.Inject;

import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.TVNLApplication;
import eu.se_bastiaan.tvnl.model.OverviewGridItem;
import eu.se_bastiaan.tvnl.model.Recommendation;
import eu.se_bastiaan.tvnl.network.service.UGApiService;
import eu.se_bastiaan.tvnl.service.recommendation.RecommendationBuilder;
import eu.se_bastiaan.tvnl.service.recommendation.RecommendationContentProvider;
import eu.se_bastiaan.tvnl.ui.activity.DetailsActivity;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class RecommendationService extends IntentService {

    private static final int MAX_RECOMMENDATIONS = 3;

    @Inject
    UGApiService ugApiService;

    public RecommendationService() {
        super("RecommendationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TVNLApplication.get().appComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final RecommendationBuilder builder = new RecommendationBuilder()
                .setContext(getApplicationContext())
                .setSmallIcon(R.drawable.ic_recommendation);


        ugApiService.getRecommendations()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<List<Recommendation>>() {
                    @Override
                    public void call(List<Recommendation> recommendations) {
                        try {
                            for(int i = 0; i < MAX_RECOMMENDATIONS; i++) {
                                Recommendation recommendation = recommendations.get(i);
                                builder.setBackgroundContentUri(RecommendationContentProvider.CONTENT_URI + URLEncoder.encode(recommendation.getImage(), "UTF-8"))
                                        .setId(i)
                                        .setPriority(i)
                                        .setTitle(recommendation.getName())
                                        .setDescription(recommendation.getDescription())
                                        .setImage(recommendation.getImage())
                                        .setIntent(buildPendingIntent(recommendation.toOverviewGridItem(), i))
                                        .build();
                            }

                        } catch (IOException e) {
                            Timber.e(e, "Unable to update recommendation");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable, "Unable to update recommendation");
                    }
                });
    }

    private PendingIntent buildPendingIntent(OverviewGridItem media, int notifId) {
        Intent detailIntent = DetailsActivity.buildIntent(this, media, notifId);
        return PendingIntent.getActivity(this, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}