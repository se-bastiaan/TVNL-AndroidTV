package eu.se_bastiaan.tvnl.ui.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.devbrackets.android.exomedia.util.MediaUtil;
import com.squareup.otto.Subscribe;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import eu.se_bastiaan.tvnl.AppInjectionComponent;
import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.content.EventBus;
import eu.se_bastiaan.tvnl.event.PausePlaybackEvent;
import eu.se_bastiaan.tvnl.event.PlaybackProgressChangedEvent;
import eu.se_bastiaan.tvnl.event.SeekBackwardEvent;
import eu.se_bastiaan.tvnl.event.SeekForwardEvent;
import eu.se_bastiaan.tvnl.event.StartPlaybackEvent;
import eu.se_bastiaan.tvnl.event.UpdatePlaybackStateEvent;
import eu.se_bastiaan.tvnl.exomedia.TVNLVideoView;
import eu.se_bastiaan.tvnl.model.OverviewGridItem;
import eu.se_bastiaan.tvnl.model.StreamInfo;
import eu.se_bastiaan.tvnl.ui.activity.DetailsActivity;
import eu.se_bastiaan.tvnl.ui.fragment.DetailsFragment;
import eu.se_bastiaan.tvnl.ui.fragment.VideoPlayerFragment;
import eu.se_bastiaan.tvnl.ui.presenter.base.BasePresenter;
import rx.functions.Action1;


public class VideoPlayerFragPresenter extends BasePresenter<VideoPlayerFragment> {

    private static final String MEDIASESSION_TAG = "TVNLMediaSession";
    private static final String STATE_CURRENTTIME = "state_currenttime";

    @Inject
    Context context;
    @Inject
    EventBus eventBus;
    MediaSession mediaSession;

    long duration = 0;
    int currentTime = -1;

    boolean ended = false;
    boolean seeking = false;
    boolean mediaSessionMetadataApplied = false;

    StreamInfo streamInfo;

    /* Lifecycle Section */

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        try {
            eventBus.register(this);
        } catch (IllegalArgumentException exception) {
            // Object already registered.
        }

        if(savedState != null) {
            currentTime = savedState.getInt(STATE_CURRENTTIME);
        }
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        state.putInt(STATE_CURRENTTIME, currentTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            eventBus.unregister(this);
        } catch (IllegalArgumentException exception) {
            // Object not registered.
        }

        if(mediaSession != null) {
            mediaSession.setActive(false);
            mediaSession.release();
        }
    }

    @Override
    protected void onTakeView(VideoPlayerFragment fragment) {
        super.onTakeView(fragment);

        streamInfo = fragment.getStreamInfo();

        TVNLVideoView videoView = fragment.getVideoView();
        videoView.setKeepScreenOn(true);

        if(duration == 0 || !videoView.isPlaying()) {
            loadMedia(fragment);
        } else {
            fragment.setProgressVisible(false);
        }
    }

    /* MediaPlayer section */

    public void loadMedia(VideoPlayerFragment fragment) {
        String videoLocation;
        if (TextUtils.isEmpty(streamInfo.getVideoLocation())) {
            fragment.showPlayerInfo("Error loading media");
            fragment.getActivity().finish();
            return;
        } else {
            videoLocation = streamInfo.getVideoLocation();
            if (!videoLocation.startsWith("file://") && !videoLocation.startsWith("http://") && !videoLocation.startsWith("https://")) {
                videoLocation = "file://" + videoLocation;
            }
        }

        TVNLVideoView videoView = fragment.getVideoView();

        MediaUtil.MediaType mediaType = MediaUtil.getMediaType(videoLocation);
        if(videoLocation.endsWith("Manifest"))
            mediaType = MediaUtil.MediaType.SMOOTH_STREAM;

        if(currentTime == -1 && streamInfo.getStartPosition() > 0 && streamInfo.getStartPosition() > currentTime) {
            currentTime = streamInfo.getStartPosition().intValue();
        } else if (currentTime == -1) {
            currentTime = 0;
        }
        videoView.setVideoURI(Uri.parse(videoLocation), mediaType);
        ended = false;

        activateMediaSession();
    }

    private Boolean isPlaying(VideoPlayerFragment fragment) {
        return fragment.getVideoView().isPlaying();
    }

    public void setCurrentTime(final long time) {
        viewFiltered().first().subscribe(new Action1<VideoPlayerFragment>() {
            @Override
            public void call(VideoPlayerFragment fragment) {
                fragment.getVideoView().seekTo((int) time);
            }
        });
    }

    private long getCurrentTime(VideoPlayerFragment fragment) {
        return fragment.getVideoView().getCurrentPosition();
    }

    private long getDuration() {
        return duration;
    }

    public void seek(final int delta) {
        if (duration <= 0 && !seeking) return;

        viewFiltered().first().subscribe(new Action1<VideoPlayerFragment>() {
            @Override
            public void call(VideoPlayerFragment fragment) {
                long position = getCurrentTime(fragment) + delta;
                if (position < 0) position = 0;
                setCurrentTime(position);
                updateProgress(getCurrentTime(fragment), getDuration());
            }
        });
    }

    public void isReady(VideoPlayerFragment fragment, TVNLVideoView videoView) {
        videoView.seekTo(currentTime);
        videoView.start();
        duration = videoView.getDuration();
        updatePlayPauseState();
        fragment.setProgressVisible(false);
    }

    public void isCompleted(TVNLVideoView videoView) {
        ended = true;
        currentTime = 0;

        updatePlayPauseState();
        videoView.setKeepScreenOn(false);

        mediaSession.setActive(false);
        mediaSession.release();
    }

    public void encounteredError(VideoPlayerFragment fragment, TVNLVideoView videoView) {
        updatePlayPauseState();
        fragment.showErrorDialog(context.getString(R.string.encountered_error_title), context.getString(R.string.encountered_error));
    }

    public void updateProgress(final long currentTime, final long duration) {
        viewFiltered().first().subscribe(new Action1<VideoPlayerFragment>() {
            @Override
            public void call(VideoPlayerFragment fragment) {
                VideoPlayerFragPresenter.this.currentTime = (int) currentTime;
                eventBus.postOnMain(new PlaybackProgressChangedEvent(currentTime, duration));

                if (mediaSession != null && duration > 0) {
                    PlaybackState.Builder builder = new PlaybackState.Builder();
                    builder.setActions(isPlaying(fragment)
                            ? PlaybackState.ACTION_PLAY_PAUSE | PlaybackState.ACTION_PAUSE
                            : PlaybackState.ACTION_PLAY_PAUSE | PlaybackState.ACTION_PLAY);
                    builder.setState(
                            isPlaying(fragment)
                                    ? PlaybackState.STATE_PLAYING
                                    : PlaybackState.STATE_PAUSED,
                            getCurrentTime(fragment),
                            1.0f);
                    mediaSession.setPlaybackState(builder.build());

                    if (!mediaSessionMetadataApplied) {
                        setupMediaMetadata();
                    }
                }
            }
        });
    }

    public void play(VideoPlayerFragment fragment) {
        TVNLVideoView videoView = fragment.getVideoView();
        videoView.seekTo(currentTime);
        videoView.start();
        videoView.setKeepScreenOn(true);

        updatePlayPauseState();
    }

    public void pause(VideoPlayerFragment fragment) {
        TVNLVideoView videoView = fragment.getVideoView();
        videoView.pause();
        videoView.setKeepScreenOn(false);
        videoView.stopProgressPoll();

        updatePlayPauseState();
    }

    public void togglePlayPause() {
        viewFiltered().first().subscribe(new Action1<VideoPlayerFragment>() {
            @Override
            public void call(VideoPlayerFragment fragment) {
                if(fragment.getVideoView().isPlaying()) {
                    pause(fragment);
                } else {
                    loadMedia(fragment);
                    play(fragment);
                }
            }
        });
    }

    public void updatePlayPauseState() {
        viewFiltered().first().subscribe(new Action1<VideoPlayerFragment>() {
            @Override
            public void call(VideoPlayerFragment fragment) {
                eventBus.postOnMain(new UpdatePlaybackStateEvent(isPlaying(fragment)));

                if (mediaSession != null) {
                    PlaybackState.Builder builder = new PlaybackState.Builder();
                    builder.setActions(isPlaying(fragment)
                            ? PlaybackState.ACTION_PLAY_PAUSE | PlaybackState.ACTION_PAUSE
                            : PlaybackState.ACTION_PLAY_PAUSE | PlaybackState.ACTION_PLAY);
                    builder.setState(
                            isPlaying(fragment) ? PlaybackState.STATE_PLAYING : PlaybackState.STATE_PAUSED,
                            getCurrentTime(fragment),
                            1.0f);
                    mediaSession.setPlaybackState(builder.build());
                }
            }
        });
    }

    /* Eventbus Events section */

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(StartPlaybackEvent event) {
        togglePlayPause();

        viewFiltered().first().subscribe(new Action1<VideoPlayerFragment>() {
            @Override
            public void call(VideoPlayerFragment fragment) {
                if (mediaSession != null) {
                    PlaybackState.Builder builder = new PlaybackState.Builder();
                    builder.setActions(PlaybackState.ACTION_PAUSE | PlaybackState.ACTION_PLAY_PAUSE);
                    builder.setState(PlaybackState.STATE_PLAYING, getCurrentTime(fragment), 1.0f);
                    mediaSession.setPlaybackState(builder.build());
                }
            }
        });
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(PausePlaybackEvent event) {
        togglePlayPause();

        viewFiltered().first().subscribe(new Action1<VideoPlayerFragment>() {
            @Override
            public void call(VideoPlayerFragment fragment) {
                if (mediaSession != null) {
                    PlaybackState.Builder builder = new PlaybackState.Builder();
                    builder.setActions(PlaybackState.ACTION_PLAY | PlaybackState.ACTION_PLAY_PAUSE);
                    builder.setState(PlaybackState.STATE_PAUSED, getCurrentTime(fragment), 1.0f);
                    mediaSession.setPlaybackState(builder.build());
                }
            }
        });
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(SeekBackwardEvent event) {
        if (!seeking) {
            seeking = true;
            seek(event.getSeek());
            seeking = false;
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(SeekForwardEvent event) {
        if (!seeking) {
            seeking = true;
            seek(event.getSeek());
            seeking = false;
        }
    }

    /* MediaSession section */

    private void activateMediaSession() {
        viewFiltered().first().subscribe(new Action1<VideoPlayerFragment>() {
            @Override
            public void call(VideoPlayerFragment fragment) {
                Activity activity = fragment.getActivity();
                mediaSession = new MediaSession(activity, MEDIASESSION_TAG);
                mediaSession.setCallback(new MediaSessionCallback(activity));
                mediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
                mediaSession.setActive(true);
                activity.setMediaController(new MediaController(activity, mediaSession.getSessionToken()));
            }
        });
    }

    private void setupMediaMetadata() {
        mediaSessionMetadataApplied = false;

        final MediaMetadata.Builder metadataBuilder = new MediaMetadata.Builder();
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_MEDIA_ID, streamInfo.getId());
        metadataBuilder.putLong(MediaMetadata.METADATA_KEY_DURATION, getDuration());

        metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, streamInfo.getTitle());
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, streamInfo.getSubtitle());
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE, streamInfo.getTitle());

        String imageUrl = streamInfo.getImage();
        if (imageUrl != null && !imageUrl.equals("")) {
            metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI, imageUrl);
            metadataBuilder.putString(MediaMetadata.METADATA_KEY_ART_URI, imageUrl);

            Target target = new SimpleTarget<GlideBitmapDrawable>() {
                @Override
                public void onResourceReady(GlideBitmapDrawable drawable, GlideAnimation glideAnimation) {
                    metadataBuilder.putBitmap(MediaMetadata.METADATA_KEY_ART, drawable.getBitmap());
                    mediaSession.setMetadata(metadataBuilder.build());
                    mediaSessionMetadataApplied = true;
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    mediaSession.setMetadata(metadataBuilder.build());
                    mediaSessionMetadataApplied = true;
                }
            };

            Glide.with(context).load(imageUrl).into(target);
        } else {
            mediaSession.setMetadata(metadataBuilder.build());
            mediaSessionMetadataApplied = true;
        }
    }

    @Override
    protected void injectComponent(AppInjectionComponent component) {
        component.inject(this);
    }

    private class MediaSessionCallback extends MediaSession.Callback {
        private final WeakReference<Context> contextReference;

        public MediaSessionCallback(Context context) {
            contextReference = new WeakReference<>(context);
        }

        @Override
        public void onPlay() {
            eventBus.postOnMain(new StartPlaybackEvent());
        }

        @Override
        public void onPause() {
            eventBus.postOnMain(new PausePlaybackEvent());
        }

        @Override
        public void onFastForward() {
            eventBus.postOnMain(new SeekForwardEvent());
        }

        @Override
        public void onRewind() {
            eventBus.postOnMain(new SeekForwardEvent());
        }

        @Override
        public void onPlayFromMediaId(@NonNull String mediaId, @NonNull Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
            if (contextReference.get() == null) return;
            Context context = contextReference.get();
            OverviewGridItem item = extras.getParcelable(DetailsFragment.ITEM_ARG);
            if (item == null) return;
            Intent detailIntent = DetailsActivity.buildIntent(context, item, DetailsFragPresenter.NO_NOTIFICATION);
            context.startActivity(detailIntent);
        }

        @Override
        public boolean onMediaButtonEvent(@NonNull Intent intent) {
            if (!intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) return false;
            KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) return false;

            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                eventBus.post(new PausePlaybackEvent());
                return true;
            }
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                viewFiltered().first().subscribe(new Action1<VideoPlayerFragment>() {
                    @Override
                    public void call(VideoPlayerFragment fragment) {
                        if (isPlaying(fragment)) {
                            eventBus.post(new PausePlaybackEvent());
                        } else {
                            eventBus.post(new StartPlaybackEvent());
                        }
                    }
                });
                return true;
            }

            return false;
        }
    }

}
