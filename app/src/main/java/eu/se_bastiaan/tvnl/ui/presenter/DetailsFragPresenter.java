package eu.se_bastiaan.tvnl.ui.presenter;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.support.v17.leanback.widget.TVNLDetailsOverviewRowPresenter;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import eu.se_bastiaan.tvnl.AppInjectionComponent;
import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.content.EventBus;
import eu.se_bastiaan.tvnl.event.UpdateBackgroundEvent;
import eu.se_bastiaan.tvnl.model.Broadcast;
import eu.se_bastiaan.tvnl.model.EncryptedStreamData;
import eu.se_bastiaan.tvnl.model.Episode;
import eu.se_bastiaan.tvnl.model.Extension;
import eu.se_bastiaan.tvnl.model.OdiData;
import eu.se_bastiaan.tvnl.model.OverviewGridItem;
import eu.se_bastiaan.tvnl.model.Recommendation;
import eu.se_bastiaan.tvnl.model.Series;
import eu.se_bastiaan.tvnl.model.StreamInfo;
import eu.se_bastiaan.tvnl.model.VideoFragment;
import eu.se_bastiaan.tvnl.network.service.OdiApiService;
import eu.se_bastiaan.tvnl.network.service.UGApiService;
import eu.se_bastiaan.tvnl.ui.activity.DetailsActivity;
import eu.se_bastiaan.tvnl.ui.activity.VideoPlayerActivity;
import eu.se_bastiaan.tvnl.ui.dialog.ProgressOverlayDialogFragment;
import eu.se_bastiaan.tvnl.ui.fragment.DetailsFragment;
import eu.se_bastiaan.tvnl.ui.presenter.base.BasePresenter;
import eu.se_bastiaan.tvnl.ui.viewpresenter.OverviewGridPresenter;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DetailsFragPresenter extends BasePresenter<DetailsFragment> {

    public static final int NO_NOTIFICATION = -1;
    private static final int RESTARTABLE_DATA = 0, RESTARTABLE_PLAY = 1;
    private static final int ACTION_PLAY_EPISODE = 0, ACTION_PLAY_FRAG = 1;

    @Inject
    EventBus eventBus;
    @Inject
    Context context;
    @Inject
    UGApiService ugApiService;
    @Inject
    OdiApiService odiApiService;

    OverviewGridItem item;
    Episode episode;
    String title, subtitle, description;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableLatestCache(RESTARTABLE_DATA, new Func0<Observable<ArrayObjectAdapter>>() {
            @Override
            public Observable<ArrayObjectAdapter> call() {
                Episode episode;
                Observable<Episode> episodeObservable = null;
                if (item.getObject() instanceof Episode) {
                    episodeObservable = Observable.just((Episode) item.getObject());
                } else if (item.getObject() instanceof Series) {
                    episodeObservable = ugApiService.getLatestEpisodesBySeriesId(((Series) item.getObject()).getId())
                            .toObservable();
                } else if (item.getObject() instanceof VideoFragment) {
                    VideoFragment fragment = (VideoFragment) item.getObject();
                    title = fragment.getName();
                    episodeObservable = ugApiService.getEpisodeById(fragment.getEpisode().getId())
                            .toObservable();
                } else if (item.getObject() instanceof Broadcast) {
                    episodeObservable = ugApiService.getEpisodeById(((Broadcast) item.getObject()).getEpisode().getId())
                            .toObservable();
                } else if (item.getObject() instanceof Recommendation) {
                    episodeObservable = ugApiService.getEpisodeById(((Recommendation) item.getObject()).getEpisode().getId())
                            .toObservable();
                }

                if (episodeObservable == null)
                    return Observable.just(null);

                return episodeObservable
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .map(new Func1<Episode, ArrayObjectAdapter>() {
                            @Override
                            public ArrayObjectAdapter call(Episode episode) {
                                DetailsFragPresenter.this.episode = episode;

                                if (title == null) {
                                    title = episode.getSeries().getName();
                                }
                                subtitle = context.getString(R.string.episode_subtitle, episode.getName(), episode.getBroadcastedAtAsStr(),
                                        TimeUnit.SECONDS.toMinutes(episode.getDuration()), TextUtils.join(", ", episode.getBroadcasters()));
                                description = episode.getDescription();

                                if(item.getImage() == null) {
                                    eventBus.postOnMain(new UpdateBackgroundEvent(episode.getImage()));
                                } else {
                                    eventBus.postOnMain(new UpdateBackgroundEvent(item.getImage()));
                                }

                                ClassPresenterSelector selector = new ClassPresenterSelector();
                                TVNLDetailsOverviewRowPresenter rowPresenter = new TVNLDetailsOverviewRowPresenter(new DetailsDescriptionPresenter());

                                selector.addClassPresenter(DetailsOverviewRow.class, rowPresenter);
                                selector.addClassPresenter(ListRow.class, new ListRowPresenter());
                                ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(selector);

                                DetailsOverviewRow detailsOverview = new DetailsOverviewRow(new Object());

                                SparseArrayObjectAdapter actionsAdapter = new SparseArrayObjectAdapter();
                                if (item.getObject() instanceof VideoFragment) {
                                    actionsAdapter.set(0, new Action(ACTION_PLAY_FRAG, context.getString(R.string.watch_fragment)));
                                }
                                actionsAdapter.set(1, new Action(ACTION_PLAY_EPISODE, context.getString(R.string.watch_episode)));
                                detailsOverview.setActionsAdapter(actionsAdapter);
                                rowPresenter.setOnActionClickedListener(onActionClickListener);

                                rowsAdapter.add(detailsOverview);

                                return rowsAdapter;
                            }
                        })
                        .flatMap(new Func1<ArrayObjectAdapter, Observable<ArrayObjectAdapter>>() {
                            @Override
                            public Observable<ArrayObjectAdapter> call(final ArrayObjectAdapter rowsAdapter) {
                                return ugApiService.getSeries(DetailsFragPresenter.this.episode.getSeries().getId())
                                        .toObservable()
                                        .map(new Func1<Series, ArrayObjectAdapter>() {
                                            @Override
                                            public ArrayObjectAdapter call(Series series) {
                                                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new OverviewGridPresenter(context));

                                                Series episodeSeries = series.getWithoutEpisodes();
                                                for (Episode episode : series.getEpisodes()) {
                                                    if (episode.getId().equals(DetailsFragPresenter.this.episode.getId()))
                                                        continue;

                                                    episode.setSeries(episodeSeries);
                                                    OverviewGridItem gridItem = episode.toOverviewGridItem();
                                                    gridItem.setSubtitle(context.getString(R.string.episode_grid_subtitle, episode.getBroadcastedAtAsStr(), TimeUnit.SECONDS.toMinutes(episode.getDuration())));
                                                    listRowAdapter.add(gridItem);
                                                }

                                                if (listRowAdapter.size() > 0) {
                                                    HeaderItem header = new HeaderItem(0, context.getString(R.string.episodes));
                                                    rowsAdapter.add(new ListRow(header, listRowAdapter));
                                                }

                                                return rowsAdapter;
                                            }
                                        });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }, new Action2<DetailsFragment, ArrayObjectAdapter>() {
            @Override
            public void call(DetailsFragment detailsFragment, ArrayObjectAdapter arrayObjectAdapter) {
                ProgressOverlayDialogFragment.dismissLoadingProgress(detailsFragment.getChildFragmentManager());
                if (arrayObjectAdapter != null)
                    detailsFragment.setAdapter(arrayObjectAdapter);
            }
        }, new Action2<DetailsFragment, Throwable>() {
            @Override
            public void call(DetailsFragment detailsFragment, Throwable throwable) {
                Timber.e(throwable, "Detail loading problem");
            }
        });
    }

    @Override
    protected void onTakeView(final DetailsFragment detailsFragment) {
        super.onTakeView(detailsFragment);

        ProgressOverlayDialogFragment dialogFragment = ProgressOverlayDialogFragment.showLoadingProgress(detailsFragment.getChildFragmentManager());
        dialogFragment.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                detailsFragment.getActivity().onBackPressed();
            }
        });

        removeNotification(detailsFragment.getNotificationId());

        item = detailsFragment.getItem();

        start(RESTARTABLE_DATA);
    }

    public void itemClicked(Fragment fragment, Object item) {
        if(item instanceof OverviewGridItem) {
            DetailsActivity.startActivity(fragment.getActivity(), (OverviewGridItem) item);
        }
    }

    private void removeNotification(int notificationId) {
        if (notificationId != NO_NOTIFICATION) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(notificationId);
        }
    }

    @Override
    protected void injectComponent(AppInjectionComponent component) {
        component.inject(this);
    }

    private OnActionClickedListener onActionClickListener = new OnActionClickedListener() {
        @Override
        public void onActionClicked(final Action action) {
            if(action.getId() == ACTION_PLAY_EPISODE || action.getId() == ACTION_PLAY_FRAG) {
                restartableFirst(RESTARTABLE_PLAY, new Func0<Observable<StreamInfo>>() {
                    @Override
                    public Observable<StreamInfo> call() {
                        return ugApiService.getEpisodeById(episode.getId())
                                .zipWith(ugApiService.getOdiData(episode.getId(), UGApiService.PubOption.ADAPTIVE)
                                                .flatMap(new Func1<EncryptedStreamData, Single<OdiData>>() {
                                                    @Override
                                                    public Single<OdiData> call(EncryptedStreamData encryptedStreamData) {
                                                        return odiApiService.getData(encryptedStreamData.getUrl(), Extension.HLS);
                                                    }
                                                }),
                                        new Func2<Episode, OdiData, StreamInfo>() {
                                            @Override
                                            public StreamInfo call(Episode episode, OdiData odiData) {
                                                String title = episode.getName();
                                                String subtitle = "";
                                                if (!episode.getSeries().getName().equals(episode.getName()))
                                                    subtitle = String.format(Locale.getDefault(), "%s (%s)", episode.getSeries().getName(), TextUtils.join(", ", episode.getBroadcasters()));
                                                if(action.getId() == ACTION_PLAY_FRAG) {
                                                    return new StreamInfo(episode.getId(), odiData.getUrl(), title, subtitle, episode.getImage(), TimeUnit.SECONDS.toMillis(((VideoFragment) item.getObject()).getStartsAt()));
                                                }
                                                return new StreamInfo(episode.getId(), odiData.getUrl(), title, subtitle, episode.getImage());
                                            }
                                        }
                                )
                                .toObservable()
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                }, new Action2<DetailsFragment, StreamInfo>() {
                    @Override
                    public void call(DetailsFragment detailsFragment, StreamInfo streamInfo) {
                        VideoPlayerActivity.startActivity(detailsFragment.getActivity(), streamInfo);
                    }
                }, new Action2<DetailsFragment, Throwable>() {
                    @Override
                    public void call(DetailsFragment detailsFragment, Throwable throwable) {
                        Timber.e(throwable, "Unable to get streamdata");
                    }
                });

                start(RESTARTABLE_PLAY);
            }
        }
    };

    public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

        @Override
        protected void onBindDescription(ViewHolder viewHolder, Object itemData) {
            viewHolder.getTitle().setText(title);
            viewHolder.getSubtitle().setText(subtitle);
            viewHolder.getBody().setText(description);
        }

    }

}
