package eu.se_bastiaan.tvnl.ui.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import eu.se_bastiaan.tvnl.AppInjectionComponent;
import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.content.EventBus;
import eu.se_bastiaan.tvnl.event.UpdateBackgroundEvent;
import eu.se_bastiaan.tvnl.model.Broadcast;
import eu.se_bastiaan.tvnl.model.Channel;
import eu.se_bastiaan.tvnl.model.EncryptedStreamData;
import eu.se_bastiaan.tvnl.model.Episode;
import eu.se_bastiaan.tvnl.model.Extension;
import eu.se_bastiaan.tvnl.model.MainChannelsGuide;
import eu.se_bastiaan.tvnl.model.OdiData;
import eu.se_bastiaan.tvnl.model.OverviewGridItem;
import eu.se_bastiaan.tvnl.model.Promo;
import eu.se_bastiaan.tvnl.model.RadioboxBroadcast;
import eu.se_bastiaan.tvnl.model.RadioboxChannel;
import eu.se_bastiaan.tvnl.model.RadioboxSearch;
import eu.se_bastiaan.tvnl.model.RadioboxVideostream;
import eu.se_bastiaan.tvnl.model.Series;
import eu.se_bastiaan.tvnl.model.StreamInfo;
import eu.se_bastiaan.tvnl.model.ThemeChannelsGuide;
import eu.se_bastiaan.tvnl.model.Timeline;
import eu.se_bastiaan.tvnl.model.VideoFragment;
import eu.se_bastiaan.tvnl.network.RxSntpClient;
import eu.se_bastiaan.tvnl.network.service.LivestreamsApiService;
import eu.se_bastiaan.tvnl.network.service.OdiApiService;
import eu.se_bastiaan.tvnl.network.service.RadioboxApiService;
import eu.se_bastiaan.tvnl.network.service.UGApiService;
import eu.se_bastiaan.tvnl.ui.activity.DetailsActivity;
import eu.se_bastiaan.tvnl.ui.activity.VideoPlayerActivity;
import eu.se_bastiaan.tvnl.ui.dialog.ProgressDialogFragment;
import eu.se_bastiaan.tvnl.ui.fragment.OverviewBrowseFragment;
import eu.se_bastiaan.tvnl.ui.presenter.base.BasePresenter;
import eu.se_bastiaan.tvnl.ui.viewpresenter.ChannelGridPresenter;
import eu.se_bastiaan.tvnl.ui.viewpresenter.OverviewGridPresenter;
import rx.Observable;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class BrowseFragPresenter extends BasePresenter<OverviewBrowseFragment> {

    private static final Integer TIMELINE = 0, FRAGMENTS = 1, MISSED = 2, LIVE = 3, RADIO = 4, LIVESTREAM = 5, REFRESH = 6, CLICKED = 7, PLAY = 8;

    @Inject
    Resources resources;
    @Inject
    EventBus eventBus;
    @Inject
    Context context;
    @Inject
    UGApiService ugApiService;
    @Inject
    OdiApiService odiApiService;
    @Inject
    RadioboxApiService radioboxApiService;
    @Inject
    LivestreamsApiService livestreamsApiService;

    ArrayObjectAdapter rowsAdapter;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        if (rowsAdapter.size() < 1) {
            rowsAdapter.add(new ListRow(new HeaderItem(context.getString(R.string.start)), new ArrayObjectAdapter()));
            rowsAdapter.add(new ListRow(new HeaderItem(context.getString(R.string.fragments)), new ArrayObjectAdapter()));
            rowsAdapter.add(new ListRow(new HeaderItem(context.getString(R.string.missed)), new ArrayObjectAdapter()));
            rowsAdapter.add(new ListRow(new HeaderItem(context.getString(R.string.live)), new ArrayObjectAdapter()));
            rowsAdapter.add(new ListRow(new HeaderItem(context.getString(R.string.radio)), new ArrayObjectAdapter()));
        }

        restartableLatestCache(TIMELINE, new Func0<Observable<List<OverviewGridItem>>>() {
            @Override
            public Observable<List<OverviewGridItem>> call() {
                return ugApiService.getTimeline()
                        .observeOn(Schedulers.newThread())
                        .zipWith(ugApiService.getTrendingFragments(), new Func2<List<Timeline>, List<VideoFragment>, List<OverviewGridItem>>() {
                            @Override
                            public List<OverviewGridItem> call(List<Timeline> timelines, List<VideoFragment> videoFragments) {
                                List<OverviewGridItem> overviewGridItemList = new ArrayList<>();
                                for (Timeline timeline : timelines) {
                                    if (timeline.getKind().equals("segment")) {
                                        VideoFragment fragment = null;
                                        for (VideoFragment videoFragment : videoFragments) {
                                            if (videoFragment.getId().equals(timeline.getId())) {
                                                fragment = videoFragment;
                                                break;
                                            }
                                        }

                                        if (fragment != null) {
                                            OverviewGridItem overviewGridItem = new OverviewGridItem<>(fragment, timeline.getTitle(), timeline.getLabel(), timeline.getImage());
                                            overviewGridItemList.add(overviewGridItem);
                                        }
                                    } else {
                                        overviewGridItemList.add(timeline.toOverviewGridItem());
                                    }
                                }
                                return overviewGridItemList;
                            }
                        })
                        .zipWith(ugApiService.getPromos(), new Func2<List<OverviewGridItem>, List<Promo>, List<OverviewGridItem>>() {
                            @Override
                            public List<OverviewGridItem> call(List<OverviewGridItem> overviewGridItems, List<Promo> promos) {
                                List<OverviewGridItem> overviewGridItemList = new ArrayList<>();
                                for (OverviewGridItem overviewGridItem : overviewGridItems) {
                                    if (overviewGridItem.getObject() instanceof Timeline && ((Timeline) overviewGridItem.getObject()).getKind().equals("promo")) {
                                        Timeline timeline = (Timeline) overviewGridItem.getObject();
                                        Promo promoItem = null;
                                        for (Promo promo : promos) {
                                            if (promo.getId().equals(timeline.getId())) {
                                                promoItem = promo;
                                                break;
                                            }
                                        }

                                        if (promoItem != null) {
                                            overviewGridItem = new OverviewGridItem<>(promoItem, timeline.getTitle(), timeline.getLabel(), timeline.getImage());
                                            overviewGridItemList.add(overviewGridItem);
                                        }
                                    } else {
                                        overviewGridItemList.add(overviewGridItem);
                                    }
                                }
                                return overviewGridItemList;
                            }
                        })
                        .zipWith(ugApiService.getTrendingEpisodes(), new Func2<List<OverviewGridItem>, List<Episode>, List<OverviewGridItem>>() {
                            @Override
                            public List<OverviewGridItem> call(List<OverviewGridItem> overviewGridItems, List<Episode> episodeList) {
                                List<OverviewGridItem> overviewGridItemList = new ArrayList<>();
                                for (OverviewGridItem overviewGridItem : overviewGridItems) {
                                    if (overviewGridItem.getObject() instanceof Timeline && ((Timeline) overviewGridItem.getObject()).getKind().equals("program")) {
                                        Timeline timeline = (Timeline) overviewGridItem.getObject();
                                        Episode episodeItem = null;
                                        for (Episode episode : episodeList) {
                                            if (episode.getId().equals(timeline.getId())) {
                                                episodeItem = episode;
                                                break;
                                            }
                                        }

                                        if (episodeItem != null) {
                                            overviewGridItem = new OverviewGridItem<>(episodeItem, timeline.getTitle(), timeline.getLabel(), timeline.getImage());
                                            overviewGridItemList.add(overviewGridItem);
                                        }
                                    } else {
                                        overviewGridItemList.add(overviewGridItem);
                                    }
                                }
                                return overviewGridItemList;
                            }
                        })
                        .toObservable()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }, new Action2<OverviewBrowseFragment, List<OverviewGridItem>>() {
            @Override
            public void call(OverviewBrowseFragment overviewBrowseFragment, List<OverviewGridItem> timelineList) {
                OverviewGridPresenter presenter = new OverviewGridPresenter(context);
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenter);
                listRowAdapter.addAll(0, timelineList);
                HeaderItem header = new HeaderItem(overviewBrowseFragment.getString(R.string.start));

                rowsAdapter.replace(TIMELINE, new ListRow(header, listRowAdapter));
            }
        }, new Action2<OverviewBrowseFragment, Throwable>() {
            @Override
            public void call(OverviewBrowseFragment overviewBrowseFragment, Throwable throwable) {
                Timber.e(throwable, "Unable to update timeline items");
            }
        });

        restartableLatestCache(FRAGMENTS, new Func0<Observable<List<VideoFragment>>>() {
            @Override
            public Observable<List<VideoFragment>> call() {
                return ugApiService.getTrendingFragments()
                        .toObservable()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }, new Action2<OverviewBrowseFragment, List<VideoFragment>>() {
            @Override
            public void call(OverviewBrowseFragment overviewBrowseFragment, List<VideoFragment> videoFragmentList) {
                OverviewGridPresenter presenter = new OverviewGridPresenter(context);
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenter);
                listRowAdapter.addAll(0, OverviewGridPresenter.toOverviewGridItems(videoFragmentList));
                HeaderItem header = new HeaderItem(overviewBrowseFragment.getString(R.string.fragments));

                rowsAdapter.replace(FRAGMENTS, new ListRow(header, listRowAdapter));
            }
        }, new Action2<OverviewBrowseFragment, Throwable>() {
            @Override
            public void call(OverviewBrowseFragment overviewBrowseFragment, Throwable throwable) {
                Timber.e(throwable, "Unable to update fragment items");
            }
        });

        restartableLatestCache(MISSED, new Func0<Observable<List<OverviewGridItem>>>() {
            @Override
            public Observable<List<OverviewGridItem>> call() {
                Single<List<Broadcast>> broadcasts = ugApiService.getLatestBroadcasts()
                        .observeOn(Schedulers.newThread());

                return ugApiService.getTrendingEpisodes()
                        .observeOn(Schedulers.newThread())
                        .zipWith(broadcasts, new Func2<List<Episode>, List<Broadcast>, List<OverviewGridItem>>() {
                            @Override
                            public List<OverviewGridItem> call(List<Episode> episodeList, List<Broadcast> broadcastsList) {
                                ArrayList<OverviewGridItem> returnData = new ArrayList<>();

                                for (Episode episode : episodeList) {
                                    OverviewGridItem gridItem = episode.toOverviewGridItem();
                                    gridItem.setSubtitle("meest bekeken");
                                    returnData.add(gridItem);
                                }

                                for (Broadcast broadcast : broadcastsList) {
                                    OverviewGridItem gridItem = broadcast.toOverviewGridItem();
                                    gridItem.setSubtitle("nieuw");
                                    returnData.add(gridItem);
                                }

                                Collections.shuffle(returnData);

                                return returnData;
                            }
                        })
                        .toObservable()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }, new Action2<OverviewBrowseFragment, List<OverviewGridItem>>() {
            @Override
            public void call(OverviewBrowseFragment overviewBrowseFragment, List<OverviewGridItem> items) {
                OverviewGridPresenter presenter = new OverviewGridPresenter(context);
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenter);
                listRowAdapter.addAll(listRowAdapter.size(), items);
                HeaderItem header = new HeaderItem(overviewBrowseFragment.getString(R.string.missed));

                rowsAdapter.replace(MISSED, new ListRow(header, listRowAdapter));
            }
        }, new Action2<OverviewBrowseFragment, Throwable>() {
            @Override
            public void call(OverviewBrowseFragment overviewBrowseFragment, Throwable throwable) {
                Timber.e(throwable, "Unable to update missed items");
            }
        });



        restartableLatestCache(LIVE, new Func0<Observable<List<Channel>>>() {
            @Override
            public Observable<List<Channel>> call() {
                return RxSntpClient.getCurrentTime("pool.ntp.org", 3000)
                        .observeOn(Schedulers.newThread())
                        .onErrorReturn(new Func1<Throwable, Long>() {
                            @Override
                            public Long call(Throwable throwable) {
                                Timber.e(throwable, "Unable to get current timestamp, returning device time");
                                return System.currentTimeMillis();
                            }
                        }).flatMap(new Func1<Long, Single<List<Channel>>>() {
                            @Override
                            public Single<List<Channel>> call(final Long timestamp) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(timestamp);
                                cal.add(Calendar.HOUR, -5);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                final String date = sdf.format(cal.getTime());

                                Single<ThemeChannelsGuide> themeChannels = ugApiService.getThemeChannelsGuide(date)
                                        .observeOn(Schedulers.newThread());

                                return ugApiService.getMainChannelsGuide(date)
                                        .observeOn(Schedulers.newThread())
                                        .zipWith(themeChannels, new Func2<MainChannelsGuide, ThemeChannelsGuide, List<Channel>>() {
                                            @Override
                                            public List<Channel> call(MainChannelsGuide mainChannelsGuide, ThemeChannelsGuide themeChannelsGuide) {
                                                ArrayList<Channel> returnData = new ArrayList<>();
                                                ArrayList<List<Broadcast>> channelList = new ArrayList<>();
                                                String[] channelIds = {"ned1", "ned2", "ned3", "journaal24", "politiek24", "best24", "cultura24", "hollanddoc24", "humor24", "101tv", "zappelin24"};
                                                String[] names = {"NPO 1", "NPO 2", "NPO 3", "NPO Nieuws", "NPO Politiek", "NPO Best", "NPO Cultura", "NPO Doc", "NPO Humor TV", "NPO 101", "NPO Zapp Xtra"};
                                                int[] images = {R.drawable.ic_npo1, R.drawable.ic_npo2, R.drawable.ic_npo3, R.drawable.ic_npo_nieuws, R.drawable.ic_npo_politiek, R.drawable.ic_npo_best, R.drawable.ic_npo_cultura, R.drawable.ic_npo_doc, R.drawable.ic_npo_humor_tv, R.drawable.ic_npo_101, R.drawable.ic_npo_zapp_xtra};
                                                int[] colors = {R.color.npo1, R.color.npo2, R.color.npo3, R.color.npo_nieuws, R.color.npo_pol, R.color.npo_best, R.color.npo_cult, R.color.npo_doc, R.color.npo_humor_tv, R.color.npo_101, R.color.npo_zapp_xtra};

                                                channelList.add(mainChannelsGuide.getNPO1());
                                                channelList.add(mainChannelsGuide.getNPO2());
                                                channelList.add(mainChannelsGuide.getNPO3());

                                                channelList.add(themeChannelsGuide.getNews());
                                                channelList.add(themeChannelsGuide.getPol());
                                                channelList.add(themeChannelsGuide.getBest());
                                                channelList.add(themeChannelsGuide.getCult());
                                                channelList.add(themeChannelsGuide.getDoc());
                                                channelList.add(themeChannelsGuide.getHumor());
                                                channelList.add(themeChannelsGuide.get101());
                                                channelList.add(themeChannelsGuide.getZapp());

                                                for (int i = 0; i < channelList.size(); i++) {
                                                    Broadcast currentBroadcast = null;
                                                    List<Broadcast> broadcastList = channelList.get(i);

                                                    Collections.sort(broadcastList, new Comparator<Broadcast>() {
                                                        @Override
                                                        public int compare(Broadcast b1, Broadcast b2) {
                                                            return b1.getStartsAt().compareTo(b2.getStartsAt());
                                                        }
                                                    });
                                                    Collections.reverse(broadcastList);

                                                    for (Broadcast broadcast : broadcastList) {
                                                        Long starts = broadcast.getStartsAt().getTime();
                                                        if (starts < timestamp) {
                                                            currentBroadcast = broadcast;
                                                            break;
                                                        }
                                                    }

                                                    String broadcastImage = null;
                                                    if (currentBroadcast != null)
                                                        broadcastImage = currentBroadcast.getEpisode().getImage();
                                                    if (broadcastImage == null && currentBroadcast != null && currentBroadcast.getEpisode().getStills() != null)
                                                        broadcastImage = currentBroadcast.getEpisode().getStills().get(0).getUrl();

                                                    String name = "Geen informatie beschikbaar";
                                                    int progress = 0;
                                                    if (currentBroadcast != null) {
                                                        long startedAt = currentBroadcast.getStartsAt().getTime() / 1000L;
                                                        long timePassed = (timestamp / 1000L) - startedAt;
                                                        progress = (int) Math.ceil((100f / currentBroadcast.getDuration()) * timePassed);

                                                        if (currentBroadcast.getEpisode().getSeries() != null) {
                                                            name = currentBroadcast.getEpisode().getSeries().getName();

                                                            if (TextUtils.isEmpty(broadcastImage)) {
                                                                Series series = ugApiService.getSeries(currentBroadcast.getEpisode().getSeries().getId())
                                                                        .toBlocking()
                                                                        .value();
                                                                if (series != null)
                                                                    broadcastImage = series.getImage();
                                                            }
                                                        } else {
                                                            name = currentBroadcast.getEpisode().getName();
                                                        }

                                                    }

                                                    returnData.add(new Channel(channelIds[i], name, names[i], broadcastImage, progress, images[i], colors[i]));
                                                }

                                                return returnData;
                                            }
                                        });
                            }
                        })
                        .toObservable()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }, new Action2<OverviewBrowseFragment, List<Channel>>() {
            @Override
            public void call(OverviewBrowseFragment overviewBrowseFragment, List<Channel> channels) {
                ChannelGridPresenter presenter = new ChannelGridPresenter(context);
                ArrayObjectAdapter listRowAdapter = ((ArrayObjectAdapter) ((ListRow) rowsAdapter.get(LIVE)).getAdapter());
                if (listRowAdapter == null || listRowAdapter.size() == 0) {
                    listRowAdapter = new ArrayObjectAdapter(presenter);
                    listRowAdapter.addAll(0, channels);
                    HeaderItem header = new HeaderItem(overviewBrowseFragment.getString(R.string.live));
                    rowsAdapter.replace(LIVE, new ListRow(header, listRowAdapter));
                    return;
                }

                for (int i = 0; i < channels.size(); i++) {
                    listRowAdapter.replace(i, channels.get(i));
                }
            }
        }, new Action2<OverviewBrowseFragment, Throwable>() {
            @Override
            public void call(OverviewBrowseFragment overviewBrowseFragment, Throwable throwable) {
                Timber.e(throwable, "Unable to update live items");
            }
        });

        restartableLatestCache(RADIO, new Func0<Observable<List<Channel>>>() {
            @Override
            public Observable<List<Channel>> call() {
                return RxSntpClient.getCurrentTime("pool.ntp.org", 3000)
                        .observeOn(Schedulers.newThread())
                        .onErrorReturn(new Func1<Throwable, Long>() {
                            @Override
                            public Long call(Throwable throwable) {
                                Timber.e(throwable, "Unable to get current timestamp, returning device time");
                                return System.currentTimeMillis();
                            }
                        })
                        .toObservable()
                        .flatMap(new Func1<Long, Observable<List<Channel>>>() {
                            @Override
                            public Observable<List<Channel>> call(final Long timestamp) {
                                return radioboxApiService.broadcastSearch(RadioboxApiService.QueryBuilder.Prebuilts.getCurrentDataForMainChannels(), 7, "channel.id:asc")
                                        .observeOn(Schedulers.newThread())
                                        .toObservable()
                                        .flatMap(new Func1<RadioboxSearch<RadioboxBroadcast>, Observable<List<Channel>>>() {
                                            @Override
                                            public Observable<List<Channel>> call(RadioboxSearch<RadioboxBroadcast> radioboxBroadcastRadioboxSearch) {
                                                final AtomicInteger i = new AtomicInteger();
                                                final List<RadioboxBroadcast> radioboxBroadcasts = radioboxBroadcastRadioboxSearch.getResults();
                                                final String[] channelIds = {"1", "2", "3", "4", "5", "6", "7"};
                                                final String[] names = {"NPO Radio 1", "NPO Radio 2", "NPO 3FM", "NPO Radio 4", "NPO Radio 5", "NPO Radio 6", "NPO FunX"};
                                                final int[] images = {R.drawable.ic_radio1, R.drawable.ic_radio2, R.drawable.ic_radio3, R.drawable.ic_radio4, R.drawable.ic_radio5, R.drawable.ic_radio6, R.drawable.ic_radio7};
                                                final int[] colors = {R.color.radio1, R.color.radio2, R.color.radio3, R.color.radio4, R.color.radio5, R.color.radio6, R.color.radio7};

                                                return Observable.from(radioboxBroadcasts)
                                                        .map(new Func1<RadioboxBroadcast, Channel>() {
                                                            @Override
                                                            public Channel call(RadioboxBroadcast broadcast) {
                                                                long startedAt = broadcast.getStartTime().getTime();
                                                                long timePassed = timestamp - startedAt;
                                                                long duration = broadcast.getStopTime().getTime() - broadcast.getStartTime().getTime();
                                                                int progress = (int) Math.ceil((100f / duration) * timePassed);

                                                                String image = null;
                                                                if (broadcast.getImage() != null)
                                                                    image = broadcast.getImage().getUrl();

                                                                int index = i.getAndIncrement();
                                                                return new Channel(channelIds[index], broadcast.getName(), names[index], image, progress, images[index], colors[index], true);
                                                            }
                                                        })
                                                        .toList();
                                            }
                                        });
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }, new Action2<OverviewBrowseFragment, List<Channel>>() {
            @Override
            public void call(OverviewBrowseFragment overviewBrowseFragment, List<Channel> channels) {
                ChannelGridPresenter presenter = new ChannelGridPresenter(context);
                ArrayObjectAdapter listRowAdapter = ((ArrayObjectAdapter) ((ListRow) rowsAdapter.get(RADIO)).getAdapter());
                if (listRowAdapter == null || listRowAdapter.size() == 0) {
                    listRowAdapter = new ArrayObjectAdapter(presenter);
                    listRowAdapter.addAll(0, channels);
                    HeaderItem header = new HeaderItem(overviewBrowseFragment.getString(R.string.radio));
                    rowsAdapter.replace(RADIO, new ListRow(header, listRowAdapter));
                    return;
                }

                for (int i = 0; i < channels.size(); i++) {
                    listRowAdapter.replace(i, channels.get(i));
                }
            }
        }, new Action2<OverviewBrowseFragment, Throwable>() {
            @Override
            public void call(OverviewBrowseFragment overviewBrowseFragment, Throwable throwable) {
                Timber.e(throwable, "Unable to update radio items");
            }
        });


        restartable(REFRESH, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return Observable.just(this)
                        .delay(90, TimeUnit.SECONDS)
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object browseFragPresenter) {
                                start(LIVE);
                                start(RADIO);
                                start(REFRESH);
                            }
                        });
            }
        });
    }

    @Override
    protected void onTakeView(OverviewBrowseFragment overviewBrowseFragment) {
        super.onTakeView(overviewBrowseFragment);

        overviewBrowseFragment.setAdapter(rowsAdapter);

        start(TIMELINE);
        start(FRAGMENTS);
        start(MISSED);
        start(LIVE);
        start(RADIO);
        start(REFRESH);
    }

    @Override
    protected void injectComponent(AppInjectionComponent component) {
        component.inject(this);
    }

    public void itemSelected(Object item) {
        if (item instanceof OverviewGridItem) {
            eventBus.postOnMain(new UpdateBackgroundEvent(((OverviewGridItem) item).getImage()));
        } else if (item instanceof Channel) {
            String image = ((Channel) item).getImage();
            if(image != null) {
                eventBus.postOnMain(new UpdateBackgroundEvent(image));
            } else {
                eventBus.postOnMain(new UpdateBackgroundEvent());
            }
        }
    }

    public void itemClicked(Object item) {
        if(item instanceof OverviewGridItem) {
            final OverviewGridItem gridItem = (OverviewGridItem) item;
            Object object = gridItem.getObject();

            if(object instanceof Timeline && ((Timeline) object).isLive()) {
                final Timeline timeline = (Timeline) object;
                restartableFirst(CLICKED, new Func0<Observable<Channel>>() {
                    @Override
                    public Observable<Channel> call() {
                        return viewFiltered().first()
                                .observeOn(Schedulers.newThread())
                                .flatMap(new Func1<OverviewBrowseFragment, Observable<Channel>>() {
                                    @Override
                                    public Observable<Channel> call(OverviewBrowseFragment overviewBrowseFragment) {
                                        ProgressDialogFragment dialogFragment = ProgressDialogFragment.showLoadingProgress(overviewBrowseFragment.getChildFragmentManager(), overviewBrowseFragment.getString(R.string.loading_livestream), true);
                                        dialogFragment.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialogInterface) {
                                                stop(CLICKED);
                                            }
                                        });

                                        if (timeline.getKind().equals("radio")) {
                                            return radioboxApiService.getChannelData(Integer.parseInt(timeline.getId()))
                                                    .toObservable()
                                                    .map(new Func1<RadioboxChannel, Channel>() {
                                                        @Override
                                                        public Channel call(RadioboxChannel radioboxChannel) {
                                                            return new Channel(timeline.getId(), null, radioboxChannel.getName(), null, -1, -1, -1, true);
                                                        }
                                                    });
                                        } else if (timeline.getKind().equals("tv")) {
                                            String id = timeline.getId().replace("NPO", "");
                                            Channel channel = new Channel("ned" + id, null, "NPO " + id, null, -1, -1, -1, false);
                                            return Observable.just(channel);
                                        }

                                        return Observable.just(null);
                                    }
                                })
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                }, new Action2<OverviewBrowseFragment, Channel>() {
                    @Override
                    public void call(OverviewBrowseFragment overviewBrowseFragment, Channel channel) {
                        if(channel != null)
                            openLiveStream(channel);
                    }
                }, new Action2<OverviewBrowseFragment, Throwable>() {
                    @Override
                    public void call(OverviewBrowseFragment overviewBrowseFragment, Throwable throwable) {
                        ProgressDialogFragment.dismissLoadingProgress(overviewBrowseFragment.getChildFragmentManager());
                        overviewBrowseFragment.showErrorMessage(R.string.loading_livestream_error);
                    }
                });

                start(CLICKED);
                return;
            } else if(object instanceof Promo) {
                playVideo(object);
                return;
            }

            viewFiltered().first().observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Action1<OverviewBrowseFragment>() {
                        @Override
                        public void call(OverviewBrowseFragment overviewBrowseFragment) {
                            DetailsActivity.startActivity(overviewBrowseFragment.getActivity(), gridItem);
                        }
                    })
                    .subscribe();
        } else if (item instanceof Channel) {
            Timber.d("Channel clicked! -> %s", ((Channel) item).getId());
            openLiveStream((Channel) item);
        }
    }

    private void openLiveStream(final Channel channel) {
        restartableFirst(LIVESTREAM, new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                final Observable<String> liveStreamUrlObservable;
                if (channel.isRadio()) {
                    liveStreamUrlObservable = radioboxApiService.videostreamSearch(RadioboxApiService.QueryBuilder.Prebuilts.getUnprotectedDataByChannel(channel.getId()), null, null)
                            .toObservable()
                            .observeOn(Schedulers.newThread())
                            .map(new Func1<RadioboxSearch<RadioboxVideostream>, String>() {
                                @Override
                                public String call(RadioboxSearch<RadioboxVideostream> radioboxSearch) {
                                    String url = "";
                                    for (RadioboxVideostream videostream : radioboxSearch.getResults()) {
                                        if (videostream.getName().equals("hasp-hls") || videostream.getName().endsWith(".m3u8")) {
                                            url = videostream.getUrl();
                                        } else if (videostream.getName().equals("hasp-sms")) {
                                            return videostream.getUrl();
                                        }
                                    }
                                    return url;
                                }
                            });
                } else {
                    liveStreamUrlObservable = ugApiService.getLiveDataByChannel(channel.getId(), Extension.HLS)
                            .toObservable()
                            .observeOn(Schedulers.newThread())
                            .map(new Func1<EncryptedStreamData, String>() {
                                @Override
                                public String call(EncryptedStreamData encryptedStreamData) {
                                    return encryptedStreamData.getUrl();
                                }
                            });
                }

                return viewFiltered().first()
                        .subscribeOn(Schedulers.newThread())
                        .flatMap(new Func1<OverviewBrowseFragment, Observable<String>>() {
                            @Override
                            public Observable<String> call(OverviewBrowseFragment overviewBrowseFragment) {
                                ProgressDialogFragment dialogFragment = ProgressDialogFragment.showLoadingProgress(overviewBrowseFragment.getChildFragmentManager(), overviewBrowseFragment.getString(R.string.loading_livestream), true);
                                dialogFragment.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        stop(LIVESTREAM);
                                    }
                                });
                                return liveStreamUrlObservable;
                            }
                        })
                        .flatMap(new Func1<String, Observable<String>>() {
                            @Override
                            public Observable<String> call(String streamUrl) {
                                return livestreamsApiService.getRealUrl(streamUrl).toObservable();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }, new Action2<OverviewBrowseFragment, String>() {
            @Override
            public void call(OverviewBrowseFragment overviewBrowseFragment, String streamUrl) {
                ProgressDialogFragment.dismissLoadingProgress(overviewBrowseFragment.getChildFragmentManager());

                StreamInfo streamInfo = new StreamInfo(channel.getId(), streamUrl, channel.getSubtitle(), "Live", channel.getImage(), true);
                VideoPlayerActivity.startActivity(overviewBrowseFragment.getActivity(), streamInfo);
            }
        }, new Action2<OverviewBrowseFragment, Throwable>() {
            @Override
            public void call(OverviewBrowseFragment overviewBrowseFragment, Throwable throwable) {
                ProgressDialogFragment.dismissLoadingProgress(overviewBrowseFragment.getChildFragmentManager());

                Timber.e(throwable, "Unable to get videostream data");

                overviewBrowseFragment.showErrorMessage(R.string.loading_livestream_error);
            }
        });

        start(LIVESTREAM);
    }

    private void playVideo(final Object data) {
        restartableFirst(PLAY, new Func0<Observable<StreamInfo>>() {
            @Override
            public Observable<StreamInfo> call() {
                if(data instanceof Episode) {
                    Episode episode = (Episode) data;
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
                                            if (!episode.getSeries().getName().equals(episode.getName()))
                                                title = String.format(Locale.getDefault(), "%s: %s", episode.getSeries().getName(), episode.getName());
                                            return new StreamInfo(episode.getId(), odiData.getUrl(), title, TextUtils.join(", ", episode.getBroadcasters()), episode.getImage());
                                        }
                                    }
                            )
                            .toObservable()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread());
                } else if (data instanceof Promo) {
                    return Observable.just((Promo) data)
                            .map(new Func1<Promo, StreamInfo>() {
                                @Override
                                public StreamInfo call(Promo promo) {
                                    String location = promo.getLocations().get(0).getUrl();
                                    String lastPart = location.substring(location.lastIndexOf('/'));
                                    lastPart = lastPart.replace("ism", "m3u8");
                                    location = location + lastPart;

                                    return new StreamInfo(promo.getId(), location, promo.getName(), TextUtils.join(", ", promo.getEpisode().getBroadcasters()), promo.getEpisode().getImage());
                                }
                            })
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread());
                }

                return Observable.just(null);
            }
        }, new Action2<OverviewBrowseFragment, StreamInfo>() {
            @Override
            public void call(OverviewBrowseFragment detailsFragment, StreamInfo streamInfo) {
                VideoPlayerActivity.startActivity(detailsFragment.getActivity(), streamInfo);
            }
        }, new Action2<OverviewBrowseFragment, Throwable>() {
            @Override
            public void call(OverviewBrowseFragment detailsFragment, Throwable throwable) {
                Timber.e(throwable, "Unable to get streamdata");
            }
        });

        start(PLAY);
    }

}
