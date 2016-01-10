package eu.se_bastiaan.tvnl.ui.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import eu.se_bastiaan.tvnl.AppInjectionComponent;
import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.content.EventBus;
import eu.se_bastiaan.tvnl.event.UpdateBackgroundEvent;
import eu.se_bastiaan.tvnl.model.Episode;
import eu.se_bastiaan.tvnl.model.OverviewGridItem;
import eu.se_bastiaan.tvnl.model.Series;
import eu.se_bastiaan.tvnl.network.service.UGApiService;
import eu.se_bastiaan.tvnl.ui.activity.DetailsActivity;
import eu.se_bastiaan.tvnl.ui.fragment.SearchFragment;
import eu.se_bastiaan.tvnl.ui.presenter.base.BasePresenter;
import eu.se_bastiaan.tvnl.ui.viewpresenter.OverviewGridPresenter;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SearchFragPresenter extends BasePresenter<SearchFragment> {

    private static final int SEARCH_DELAY_MS = 300;

    @Inject
    Context context;
    @Inject
    EventBus eventBus;
    @Inject
    UGApiService ugApiService;

    ArrayObjectAdapter rowsAdapter;
    ListRowPresenter listRowPresenter;
    Subscription searchSubscription;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        listRowPresenter = new ListRowPresenter();
        rowsAdapter = new ArrayObjectAdapter(listRowPresenter);
    }

    private void loadRows(final String query) {
        rowsAdapter.clear();

        Single<Pair<String, List<OverviewGridItem>>> seriesSearchSingle = ugApiService.getSeriesIndex()
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<List<Series>, Pair<String, List<OverviewGridItem>>>() {
                    @Override
                    public Pair<String, List<OverviewGridItem>> call(List<Series> seriesList) {
                        List<OverviewGridItem> returnList = new ArrayList<>();
                        for (Series series : seriesList) {
                            if (series.getName().toLowerCase().contains(query))
                                returnList.add(series.toOverviewGridItem());
                        }
                        return new Pair<>(context.getString(R.string.series), returnList);
                    }
                })
                .onErrorReturn(new Func1<Throwable, Pair<String, List<OverviewGridItem>>>() {
                    @Override
                    public Pair<String, List<OverviewGridItem>> call(Throwable throwable) {
                        Timber.d("Couldn't load search results", throwable);
                        List<OverviewGridItem> returnList = new ArrayList<>();
                        return new Pair<>(context.getString(R.string.series), returnList);
                    }
                });

        searchSubscription = ugApiService.getEpisodesBySearch(query)
                .delay(SEARCH_DELAY_MS, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<List<Episode>, Pair<String, List<OverviewGridItem>>>() {
                    @Override
                    public Pair<String, List<OverviewGridItem>> call(List<Episode> episodeList) {
                        List<OverviewGridItem> returnList = new ArrayList<>();
                        for (Episode episode : episodeList) {
                            returnList.add(episode.toOverviewGridItem());
                        }
                        return new Pair<>(context.getString(R.string.episodes), returnList);
                    }
                })
                .onErrorReturn(new Func1<Throwable, Pair<String, List<OverviewGridItem>>>() {
                    @Override
                    public Pair<String, List<OverviewGridItem>> call(Throwable throwable) {
                        Timber.d("Couldn't load search results", throwable);
                        List<OverviewGridItem> returnList = new ArrayList<>();
                        return new Pair<>(context.getString(R.string.episodes), returnList);
                    }
                })
                .concatWith(seriesSearchSingle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Pair<String, List<OverviewGridItem>>>() {
                    @Override
                    public void call(Pair<String, List<OverviewGridItem>> dataPair) {
                        addRow(dataPair.first, dataPair.second);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.d("Couldn't load search results", throwable);
                    }
                });
    }

    private void addRow(String title, List<OverviewGridItem> items) {
        OverviewGridPresenter presenter = new OverviewGridPresenter(context);
        HeaderItem header = new HeaderItem(title);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenter);
        listRowAdapter.addAll(0, items);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    public ObjectAdapter getRowsAdapter() {
        return rowsAdapter;
    }

    public void queryByWords(String words) {
        rowsAdapter.clear();
        if (!TextUtils.isEmpty(words)) {
            if(searchSubscription != null && !searchSubscription.isUnsubscribed())
                searchSubscription.unsubscribe();
            loadRows(words.toLowerCase());
        }
    }

    public void itemSelected(Object item) {
        if (item instanceof OverviewGridItem) {
            eventBus.postOnMain(new UpdateBackgroundEvent(((OverviewGridItem) item).getImage()));
        }
    }

    public void itemClicked(Fragment fragment, Object item) {
        if(item instanceof OverviewGridItem) {
            OverviewGridItem gridItem = (OverviewGridItem) item;
            DetailsActivity.startActivity(fragment.getActivity(), gridItem);
        }
    }

    @Override
    protected void injectComponent(AppInjectionComponent component) {
        component.inject(this);
    }

}
