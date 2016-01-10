package eu.se_bastiaan.tvnl.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.PlaybackOverlaySupportFragment;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.view.InputEvent;

import eu.se_bastiaan.tvnl.TVNLApplication;
import eu.se_bastiaan.tvnl.model.StreamInfo;
import eu.se_bastiaan.tvnl.ui.presenter.PlaybackOverlayFragPresenter;
import nucleus.factory.PresenterFactory;
import nucleus.factory.ReflectionPresenterFactory;
import nucleus.factory.RequiresPresenter;
import nucleus.view.PresenterLifecycleDelegate;
import nucleus.view.ViewWithPresenter;

@RequiresPresenter(PlaybackOverlayFragPresenter.class)
public class PlaybackOverlayFragment extends PlaybackOverlaySupportFragment implements ViewWithPresenter<PlaybackOverlayFragPresenter> {

    StreamInfo streamInfo;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null)
            presenterDelegate.onRestoreInstanceState(bundle.getBundle(PRESENTER_STATE_KEY));

        TVNLApplication.get().appComponent().inject(this);

        setFadeCompleteListener(onFadeCompleteListener);
        setInputEventHandler(inputEventHandler);
        setOnItemViewSelectedListener(onItemViewSelectedListener);
        setBackgroundType(android.support.v17.leanback.app.PlaybackOverlayFragment.BG_LIGHT);
        setFadingEnabled(false);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBundle(PRESENTER_STATE_KEY, presenterDelegate.onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        streamInfo = ((VideoPlayerFragment.Callback) getActivity()).getInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterDelegate.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenterDelegate.onPause(getActivity().isFinishing());
    }

    public StreamInfo getStreamInfo() {
        return streamInfo;
    }

    /* Presenter */

    private static final String PRESENTER_STATE_KEY = "presenter_state";
    private PresenterLifecycleDelegate<PlaybackOverlayFragPresenter> presenterDelegate =
            new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<PlaybackOverlayFragPresenter>fromViewClass(getClass()));

    @Override
    public PresenterFactory<PlaybackOverlayFragPresenter> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    @Override
    public void setPresenterFactory(PresenterFactory<PlaybackOverlayFragPresenter> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    @Override
    public PlaybackOverlayFragPresenter getPresenter() {
        return presenterDelegate.getPresenter();
    }

    /* Listener section */
    PlaybackOverlaySupportFragment.OnFadeCompleteListener onFadeCompleteListener = new PlaybackOverlaySupportFragment.OnFadeCompleteListener() {
        @Override
        public void onFadeInComplete() {
            super.onFadeInComplete();
            getPresenter().onFadeInComplete();
        }

        @Override
        public void onFadeOutComplete() {
            super.onFadeOutComplete();
            getPresenter().onFadeOutComplete();
        }
    };

    OnItemViewSelectedListener onItemViewSelectedListener = new OnItemViewSelectedListener() {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            getPresenter().onItemSelected(itemViewHolder, item, rowViewHolder, row);
        }
    };

    PlaybackOverlaySupportFragment.InputEventHandler inputEventHandler = new PlaybackOverlaySupportFragment.InputEventHandler() {
        @Override
        public boolean handleInputEvent(@NonNull InputEvent event) {
            return getPresenter().handleInputEvent(event);
        }
    };

}