package eu.se_bastiaan.tvnl.ui.activity.base;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import eu.se_bastiaan.tvnl.AppInjectionComponent;
import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.TVNLApplication;
import eu.se_bastiaan.tvnl.content.EventBus;
import eu.se_bastiaan.tvnl.event.UpdateBackgroundEvent;
import eu.se_bastiaan.tvnl.ui.activity.SearchActivity;
import eu.se_bastiaan.tvnl.util.BackgroundUpdater;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends FragmentActivity {

    private static final int PERMISSIONS_REQUEST = 1;

    @Inject
    protected EventBus eventBus;

    BackgroundUpdater backgroundManager;

    public void onCreate(Bundle savedInstanceState, @LayoutRes int layoutRes) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes);
        ButterKnife.bind(this);

        injectComponent(TVNLApplication.get().appComponent());

        backgroundManager = BackgroundUpdater.init(this, R.drawable.default_background);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onSearchRequested() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            SearchActivity.startActivity(this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundManager.destroy();
    }

    @Subscribe
    public void updateBackground(UpdateBackgroundEvent updateBackgroundEvent) {
        if(updateBackgroundEvent.isDrawable()) {
            Drawable drawable = updateBackgroundEvent.getDrawable();
            backgroundManager.updateBackground(drawable);
        } else if(updateBackgroundEvent.isResource()) {
            backgroundManager.updateBackground(updateBackgroundEvent.getResource());
        } else if(updateBackgroundEvent.isUrl()){
            backgroundManager.updateBackgroundAsync(updateBackgroundEvent.getUrl());
        } else {
            backgroundManager.clearBackground();
        }
    }

    protected abstract void injectComponent(AppInjectionComponent component);

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PERMISSIONS_REQUEST == requestCode) {
            boolean success = true;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    success = false;
                    break;
                }
            }

            if (success)
                SearchActivity.startActivity(this);
        }
    }
}

