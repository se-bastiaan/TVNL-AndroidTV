package eu.se_bastiaan.tvnl.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import eu.se_bastiaan.tvnl.AppInjectionComponent;
import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.model.StreamInfo;
import eu.se_bastiaan.tvnl.ui.activity.base.BaseActivity;
import eu.se_bastiaan.tvnl.ui.fragment.PlaybackOverlayFragment;
import eu.se_bastiaan.tvnl.ui.fragment.VideoPlayerFragment;

public class VideoPlayerActivity extends BaseActivity implements VideoPlayerFragment.Callback {

    private VideoPlayerFragment playerFragment;
    @SuppressLint("unused")
    private PlaybackOverlayFragment playbackOverlayFragment;

    public final static String EXTRA_STREAM_INFO = "stream_info";

    private StreamInfo streamInfo;

    public static void startActivity(Context context, StreamInfo info) {
        Intent i = new Intent(context, VideoPlayerActivity.class);
        i.putExtra(EXTRA_STREAM_INFO, info);
        context.startActivity(i);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_videoplayer);
        streamInfo = getIntent().getParcelableExtra(EXTRA_STREAM_INFO);

        playerFragment = (VideoPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        playbackOverlayFragment = (PlaybackOverlayFragment) getSupportFragmentManager().findFragmentById(R.id.playback_overlay_fragment);
    }

    @Override
    public void onVisibleBehindCanceled() {
        playerFragment.getPresenter().pause(playerFragment);
        super.onVisibleBehindCanceled();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public StreamInfo getInfo() {
        return streamInfo;
    }

    @Override
    protected void injectComponent(AppInjectionComponent component) {
        component.inject(this);
    }

}