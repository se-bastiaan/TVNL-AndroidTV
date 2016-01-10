package eu.se_bastiaan.tvnl.ui.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.ControlButtonPresenterSelector;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.view.InputEvent;
import android.view.KeyEvent;

import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import eu.se_bastiaan.tvnl.AppInjectionComponent;
import eu.se_bastiaan.tvnl.content.EventBus;
import eu.se_bastiaan.tvnl.event.PausePlaybackEvent;
import eu.se_bastiaan.tvnl.event.PlaybackProgressChangedEvent;
import eu.se_bastiaan.tvnl.event.SeekBackwardEvent;
import eu.se_bastiaan.tvnl.event.SeekForwardEvent;
import eu.se_bastiaan.tvnl.event.StartPlaybackEvent;
import eu.se_bastiaan.tvnl.event.UpdatePlaybackStateEvent;
import eu.se_bastiaan.tvnl.model.StreamInfo;
import eu.se_bastiaan.tvnl.ui.fragment.PlaybackOverlayFragment;
import eu.se_bastiaan.tvnl.ui.presenter.base.BasePresenter;
import eu.se_bastiaan.tvnl.ui.viewpresenter.DescriptionPresenter;
import rx.functions.Action1;

public class PlaybackOverlayFragPresenter extends BasePresenter<PlaybackOverlayFragment> {

    private static final String STATE_MEDIA_READY = "state_ready";
    private static final String STATE_MEDIA_PLAYING = "playing";

    private static final int MODE_NOTHING = 0;
    private static final int MODE_FAST_FORWARD = 1;
    private static final int MODE_REWIND = 2;

    @Inject
    EventBus eventBus;

    ArrayObjectAdapter rowsAdapter;
    ArrayObjectAdapter primaryActionsAdapter;
    ArrayObjectAdapter secondaryActionsAdapter;
    PlaybackControlsRow.PlayPauseAction playPauseAction;
    PlaybackControlsRow.RewindAction rewindAction;
    PlaybackControlsRow.FastForwardAction fastForwardAction;
    PlaybackControlsRowPresenter playbackControlsRowPresenter;
    PlaybackControlsRow playbackControlsRow;
    Handler handlerPlayback;
    Handler handlerPlaybackSpeed;
    StreamInfo streamInfo;

    long selectedActionId = 0;
    int currentMode = MODE_NOTHING;
    int seek;
    int currentTime;
    int seekSpeed = SeekBackwardEvent.MINIMUM_SEEK_SPEED;
    boolean isMediaReady = false;
    boolean isPlaying = false;

    /* Lifecycle section */
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        if(savedState != null) {
            isMediaReady = savedState.getBoolean(STATE_MEDIA_READY);
            isPlaying = savedState.getBoolean(STATE_MEDIA_PLAYING);
        }

        handlerPlayback = new Handler();
        handlerPlaybackSpeed = new Handler();

        try {
            eventBus.register(this);
        } catch (IllegalArgumentException e) {
            // Already registered
        }
    }

    @Override
    protected void onTakeView(PlaybackOverlayFragment fragment) {
        super.onTakeView(fragment);

        streamInfo = fragment.getStreamInfo();

        initialisePlaybackControlPresenter(fragment);
        if(isMediaReady) {
            setupPlaybackControlItemsToReadyState();
        } else {
            setupPlaybackControlItemsToInitialisingState(fragment);
        }

        if(isPlaying) {
            playPauseAction.setIndex(PlaybackControlsRow.PlayPauseAction.PAUSE);
            fragment.setFadingEnabled(true);
            notifyPlaybackControlActionChanged(playPauseAction);
        }
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        state.putBoolean(STATE_MEDIA_PLAYING, isPlaying);
        state.putBoolean(STATE_MEDIA_READY, isMediaReady);
    }

    @Override
    protected void onDropView() {
        playPauseAction.setIndex(PlaybackControlsRow.PlayPauseAction.PLAY);
        viewFiltered().first().subscribe(new Action1<PlaybackOverlayFragment>() {
            @Override
            public void call(PlaybackOverlayFragment fragment) {
                fragment.setFadingEnabled(false);
            }
        });
        notifyPlaybackControlActionChanged(playPauseAction);

        super.onDropView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    private void initialisePlaybackControlPresenter(PlaybackOverlayFragment fragment) {
        ClassPresenterSelector presenterSelector = new ClassPresenterSelector();
        playbackControlsRowPresenter = new PlaybackControlsRowPresenter(new DescriptionPresenter());
        playbackControlsRowPresenter.setSecondaryActionsHidden(false);

        presenterSelector.addClassPresenter(PlaybackControlsRow.class, playbackControlsRowPresenter);
        presenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());

        rowsAdapter = new ArrayObjectAdapter(presenterSelector);
        fragment.setAdapter(rowsAdapter);
    }

    private void setupPlaybackControlItemsActions() {
        playbackControlsRowPresenter.setOnActionClickedListener(onActionClickedListener);
    }

    private void setupPlaybackControlItemsToInitialisingState(PlaybackOverlayFragment fragment) {
        if(rowsAdapter == null)
            return;

        rowsAdapter.clear();
        playbackControlsRow = new PlaybackControlsRow(streamInfo);
        playbackControlsRow.setCurrentTime(0);
        playbackControlsRow.setBufferedProgress(0);

        ControlButtonPresenterSelector presenterSelector = new ControlButtonPresenterSelector();
        primaryActionsAdapter = new ArrayObjectAdapter(presenterSelector);
        playbackControlsRow.setPrimaryActionsAdapter(primaryActionsAdapter);

        playPauseAction = new PlaybackControlsRow.PlayPauseAction(fragment.getActivity());
        primaryActionsAdapter.add(playPauseAction);

        setupSecondaryRowPlaybackControl(presenterSelector);

        rowsAdapter.add(playbackControlsRow);
        rowsAdapter.notifyArrayItemRangeChanged(0, rowsAdapter.size());
    }

    private void setupPlaybackControlItemsToReadyState() {
        if(rowsAdapter == null)
            return;

        rowsAdapter.clear();
        playbackControlsRow = new PlaybackControlsRow(streamInfo);
        playbackControlsRow.setCurrentTime(0);
        playbackControlsRow.setBufferedProgress(0);

        ControlButtonPresenterSelector presenterSelector = new ControlButtonPresenterSelector();
        setupPrimaryRowPlaybackControl(presenterSelector);
        setupSecondaryRowPlaybackControl(presenterSelector);

        rowsAdapter.add(playbackControlsRow);
        rowsAdapter.notifyArrayItemRangeChanged(0, rowsAdapter.size());

        setupPlaybackControlItemsActions();
    }

    private void setupPrimaryRowPlaybackControl(@NonNull ControlButtonPresenterSelector presenterSelector) {
        primaryActionsAdapter = new ArrayObjectAdapter(presenterSelector);
        playbackControlsRow.setPrimaryActionsAdapter(primaryActionsAdapter);

        viewFiltered().first().subscribe(new Action1<PlaybackOverlayFragment>() {
            @Override
            public void call(PlaybackOverlayFragment fragment) {
                playPauseAction = new PlaybackControlsRow.PlayPauseAction(fragment.getActivity());
                fastForwardAction = new PlaybackControlsRow.FastForwardAction(fragment.getActivity());
                rewindAction = new PlaybackControlsRow.RewindAction(fragment.getActivity());

                if (isPlaying)
                    playPauseAction.setIndex(PlaybackControlsRow.PlayPauseAction.PAUSE);

                // Add main controls to primary adapter.
                if (!streamInfo.isLive())
                    primaryActionsAdapter.add(rewindAction);
                primaryActionsAdapter.add(playPauseAction);
                if (!streamInfo.isLive())
                    primaryActionsAdapter.add(fastForwardAction);
            }
        });
    }

    private void setupSecondaryRowPlaybackControl(@NonNull PresenterSelector presenterSelector) {
        secondaryActionsAdapter = new ArrayObjectAdapter(presenterSelector);
        playbackControlsRow.setSecondaryActionsAdapter(secondaryActionsAdapter);
    }

    /* Events section */
    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(UpdatePlaybackStateEvent event) {
        isPlaying = event.isPlaying();

        if (isPlaying) {
            if (!isMediaReady) {
                setupPlaybackControlItemsToReadyState();
                isMediaReady = true;
            }

            viewFiltered().first().subscribe(new Action1<PlaybackOverlayFragment>() {
                @Override
                public void call(PlaybackOverlayFragment fragment) {
                    playPauseAction.setIndex(PlaybackControlsRow.PlayPauseAction.PAUSE);
                    fragment.setFadingEnabled(true);
                }
            });
        } else {
            viewFiltered().first().subscribe(new Action1<PlaybackOverlayFragment>() {
                @Override
                public void call(PlaybackOverlayFragment fragment) {
                    playPauseAction.setIndex(PlaybackControlsRow.PlayPauseAction.PLAY);
                    fragment.setFadingEnabled(false);
                }
            });
        }

        notifyPlaybackControlActionChanged(playPauseAction);
        if (rowsAdapter != null) rowsAdapter.notifyArrayItemRangeChanged(0, rowsAdapter.size());
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(@NonNull PlaybackProgressChangedEvent event) {
        if(playbackControlsRow == null)
            return;

        if (playbackControlsRow.getTotalTime() == 0 && !streamInfo.isLive()) {
            playbackControlsRow.setTotalTime((int) event.getDuration());
        }

        if (seek != 0 && currentMode != MODE_NOTHING) {
            return;
        }

        currentTime = (int) event.getCurrentTime();

        viewFiltered().first().subscribe(new Action1<PlaybackOverlayFragment>() {
            @Override
            public void call(PlaybackOverlayFragment fragment) {
                if(!fragment.isHidden()) {
                    playbackControlsRow.setCurrentTime(currentTime);
                    if (rowsAdapter != null) rowsAdapter.notifyArrayItemRangeChanged(0, rowsAdapter.size());
                }
            }
        });
    }

    private void notifyPlaybackControlActionChanged(@NonNull Action action) {
        ArrayObjectAdapter adapter = primaryActionsAdapter;
        if(adapter == null)
            return;

        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1);
            return;
        }

        adapter = secondaryActionsAdapter;
        if(adapter == null)
            return;

        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1);
        }
    }

    private void invokeTogglePlaybackAction(final boolean play) {
        viewFiltered().first().subscribe(new Action1<PlaybackOverlayFragment>() {
            @Override
            public void call(PlaybackOverlayFragment fragment) {
                if (play) {
                    eventBus.postOnMain(new StartPlaybackEvent());
                    fragment.setFadingEnabled(true);
                } else {
                    eventBus.postOnMain(new PausePlaybackEvent());
                    fragment.setFadingEnabled(false);
                }
            }
        });
    }

    private void invokeSeekAction() {
        viewFiltered().first().subscribe(new Action1<PlaybackOverlayFragment>() {
            @Override
            public void call(final PlaybackOverlayFragment fragment) {
                final int refreshDuration = 100;
                final int speedRefreshDuration = 5000;

                Runnable runnablePlayback = new Runnable() {
                    @Override
                    public void run() {
                        fragment.tickle();
                        if (currentMode == MODE_REWIND) {
                            int currentTime = playbackControlsRow.getCurrentTime();
                            currentTime -= seekSpeed;

                            if (currentTime > 0) {
                                playbackControlsRow.setCurrentTime(currentTime);
                                rowsAdapter.notifyArrayItemRangeChanged(0, 1);
                                seek += seekSpeed;
                            }

                            handlerPlayback.postDelayed(this, refreshDuration);
                        } else if (selectedActionId == rewindAction.getId()) {
                            SeekBackwardEvent event = new SeekBackwardEvent();
                            event.setSeek(seek);
                            seek = 0;
                            eventBus.postOnMain(event);
                        } else if (currentMode == MODE_FAST_FORWARD) {
                            int currentTime = playbackControlsRow.getCurrentTime();
                            currentTime += seekSpeed;

                            if (currentTime < playbackControlsRow.getTotalTime()) {
                                playbackControlsRow.setCurrentTime(currentTime);
                                rowsAdapter.notifyArrayItemRangeChanged(0, 1);
                                seek += seekSpeed;
                            }

                            handlerPlayback.postDelayed(this, refreshDuration);
                        } else if (selectedActionId == fastForwardAction.getId()) {
                            SeekForwardEvent event = new SeekForwardEvent();
                            event.setSeek(seek);
                            seek = 0;
                            eventBus.postOnMain(event);
                        }
                    }
                };

                Runnable runnablePlaybackSpeed = new Runnable() {
                    @Override
                    public void run() {
                        if (currentMode == MODE_FAST_FORWARD || currentMode == MODE_REWIND) {
                            seekSpeed *= 2;
                            handlerPlaybackSpeed.postDelayed(this, speedRefreshDuration);
                        } else {
                            seekSpeed = SeekForwardEvent.MINIMUM_SEEK_SPEED;
                        }
                    }
                };

                handlerPlayback.postDelayed(runnablePlayback, refreshDuration);
                handlerPlaybackSpeed.postDelayed(runnablePlaybackSpeed, speedRefreshDuration);
                fragment.tickle();
            }
        });
    }

    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        currentMode = MODE_NOTHING;
        selectedActionId = 0;
        if (item != null && item instanceof Action) {
            Action action = (Action) item;
            selectedActionId = action.getId();
        }
    }

    public boolean handleInputEvent(@NonNull InputEvent event) {
        if (event instanceof KeyEvent) {
            KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.getKeyCode() != KeyEvent.KEYCODE_DPAD_CENTER && keyEvent.getKeyCode() != KeyEvent.KEYCODE_ENTER && keyEvent.getKeyCode() != KeyEvent.KEYCODE_NUMPAD_ENTER) return false;
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (fastForwardAction != null && selectedActionId == fastForwardAction.getId()) {
                    if (keyEvent.getRepeatCount() == 0) {
                        currentMode = MODE_FAST_FORWARD;
                        invokeSeekAction();
                    }
                } else if (rewindAction != null && selectedActionId == rewindAction.getId()) {
                    if (keyEvent.getRepeatCount() == 0) {
                        currentMode = MODE_REWIND;
                        invokeSeekAction();
                    }
                }
            } else if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                currentMode = MODE_NOTHING;
                seekSpeed = SeekBackwardEvent.MINIMUM_SEEK_SPEED;
            }
        }
        return false;
    }

    public void onFadeInComplete() {
        if(playbackControlsRow == null)
            return;

        playbackControlsRow.setCurrentTime(currentTime);
        selectedActionId = playPauseAction.getId();

        if (rowsAdapter != null)
            rowsAdapter.notifyArrayItemRangeChanged(0, rowsAdapter.size());
    }

    public void onFadeOutComplete() {
        currentMode = MODE_NOTHING;
        selectedActionId = 0;
    }

    @Override
    protected void injectComponent(AppInjectionComponent component) {
        component.inject(this);
    }

    OnActionClickedListener onActionClickedListener = new OnActionClickedListener() {
        @Override
        public void onActionClicked(Action action) {
            if (action.getId() == playPauseAction.getId()) {
                invokeTogglePlaybackAction(playPauseAction.getIndex() == PlaybackControlsRow.PlayPauseAction.PLAY);
            }

            if (action instanceof PlaybackControlsRow.MultiAction) {
                notifyPlaybackControlActionChanged(action);
            }
        }
    };

}
