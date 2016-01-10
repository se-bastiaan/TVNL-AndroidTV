package eu.se_bastiaan.tvnl.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.devbrackets.android.exomedia.event.EMMediaProgressEvent;
import com.devbrackets.android.exomedia.listener.EMProgressCallback;
import com.devbrackets.android.exomedia.listener.ExoPlayerListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.exomedia.TVNLVideoView;
import eu.se_bastiaan.tvnl.model.StreamInfo;
import eu.se_bastiaan.tvnl.ui.dialog.MessageDialogFragment;
import eu.se_bastiaan.tvnl.ui.presenter.VideoPlayerFragPresenter;
import nucleus.factory.PresenterFactory;
import nucleus.factory.ReflectionPresenterFactory;
import nucleus.factory.RequiresPresenter;
import nucleus.view.PresenterLifecycleDelegate;
import nucleus.view.ViewWithPresenter;
import timber.log.Timber;

@RequiresPresenter(VideoPlayerFragPresenter.class)
public class VideoPlayerFragment extends Fragment implements ViewWithPresenter<VideoPlayerFragPresenter> {

    Callback callback;
    View rootView;
    StreamInfo streamInfo;

    @Bind(R.id.video_view)
    TVNLVideoView videoView;
    @Bind(R.id.progress_indicator)
    ProgressBar progressIndicator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) callback = (Callback) context;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null)
            presenterDelegate.onRestoreInstanceState(bundle.getBundle(PRESENTER_STATE_KEY));

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBundle(PRESENTER_STATE_KEY, presenterDelegate.onSaveInstanceState());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_videoplayer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        streamInfo = callback.getInfo();

        if (streamInfo == null){
            getActivity().finish();
            return;
        }

        setRetainInstance(true);

        videoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterDelegate.onResume(this);

        videoView.setOnCompletionListener(onCompletionListener);
        videoView.setOnPreparedListener(onPreparedListener);
        videoView.setOnErrorListener(onErrorListener);
        videoView.setOnInfoListener(onInfoListener);
        videoView.addExoPlayerListener(exoPlayerListener);
        videoView.startProgressPoll(progressCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenterDelegate.onPause(getActivity().isFinishing());

        videoView.removeExoPlayerListener(exoPlayerListener);
        videoView.stopProgressPoll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }

    public TVNLVideoView getVideoView() {
        return videoView;
    }

    public StreamInfo getStreamInfo() {
        return streamInfo;
    }

    public void setProgressVisible(boolean visible) {
        if(progressIndicator.getVisibility() == View.VISIBLE && visible)
            return;

        if(progressIndicator.getVisibility() == View.GONE && !visible)
            return;

        progressIndicator.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void showPlayerInfo(String info) {
        Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
    }

    public void showErrorDialog(String title, String message) {
        if (!getActivity().isFinishing())
            MessageDialogFragment.show(getFragmentManager(), title, message, false, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    getPresenter().loadMedia(VideoPlayerFragment.this);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    getActivity().finish();
                }
            });
    }

    public interface Callback {
        StreamInfo getInfo();
    }

    /* Presenter section */

    private static final String PRESENTER_STATE_KEY = "presenter_state";
    private PresenterLifecycleDelegate<VideoPlayerFragPresenter> presenterDelegate =
            new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<VideoPlayerFragPresenter>fromViewClass(getClass()));

    @Override
    public PresenterFactory<VideoPlayerFragPresenter> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    @Override
    public void setPresenterFactory(PresenterFactory<VideoPlayerFragPresenter> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    @Override
    public VideoPlayerFragPresenter getPresenter() {
        return presenterDelegate.getPresenter();
    }

    /* Listener section */
    MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            getPresenter().isReady(VideoPlayerFragment.this, getVideoView());
        }
    };

    MediaPlayer.OnInfoListener onInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
            return false;
        }
    };

    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            getPresenter().isCompleted(getVideoView());
        }
    };

    MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
            getPresenter().encounteredError(VideoPlayerFragment.this, getVideoView());
            return true;
        }
    };

    EMProgressCallback progressCallback = new EMProgressCallback() {
        @Override
        public boolean onProgressUpdated(EMMediaProgressEvent progressEvent) {
            if(progressEvent.getDuration() > 0)
                getPresenter().updateProgress(progressEvent.getPosition(), progressEvent.getDuration());
            return true;
        }
    };

    ExoPlayerListener exoPlayerListener = new ExoPlayerListener() {
        @Override
        public void onStateChanged(boolean playWhenReady, int playbackState) {

        }

        @Override
        public void onError(Exception e) {
            Timber.e(e, "ExoPlayer exception");
        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

        }
    };

}
