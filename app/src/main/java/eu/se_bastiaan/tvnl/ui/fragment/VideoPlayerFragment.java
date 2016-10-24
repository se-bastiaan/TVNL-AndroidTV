package eu.se_bastiaan.tvnl.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.util.Repeater;

import butterknife.BindView;
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

@RequiresPresenter(VideoPlayerFragPresenter.class)
public class VideoPlayerFragment extends Fragment implements ViewWithPresenter<VideoPlayerFragPresenter> {

    Callback callback;
    View rootView;
    StreamInfo streamInfo;

    @NonNull
    protected Repeater progressPollRepeater = new Repeater();

    @BindView(R.id.video_view)
    TVNLVideoView videoView;
    @BindView(R.id.progress_indicator)
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

        progressPollRepeater.start();
        progressPollRepeater.setRepeatListener(progressPollRepeatListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        progressPollRepeater.stop();
        progressPollRepeater.setRepeatListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterDelegate.onDestroy(getActivity().isFinishing());
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
    OnPreparedListener onPreparedListener = new OnPreparedListener() {
        @Override
        public void onPrepared() {
            getPresenter().isReady(VideoPlayerFragment.this, getVideoView());
        }
    };

    OnCompletionListener onCompletionListener = new OnCompletionListener() {
        @Override
        public void onCompletion() {
            getPresenter().isCompleted(getVideoView());
        }
    };

    OnErrorListener onErrorListener = new OnErrorListener() {
        @Override
        public boolean onError() {
            getPresenter().encounteredError(VideoPlayerFragment.this, getVideoView());
            return true;
        }
    };

    Repeater.RepeatListener progressPollRepeatListener = new Repeater.RepeatListener() {
        @Override
        public void onRepeat() {
            if(getVideoView().getDuration() > 0)
                getPresenter().updateProgress(getVideoView().getCurrentPosition(), getVideoView().getDuration());
        }
    };

}
