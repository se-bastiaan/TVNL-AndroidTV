package eu.se_bastiaan.tvnl.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.squareup.otto.Subscribe;

import eu.se_bastiaan.tvnl.AppInjectionComponent;
import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.event.UpdateBackgroundEvent;
import eu.se_bastiaan.tvnl.ui.activity.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    @Subscribe
    @Override
    public void updateBackground(UpdateBackgroundEvent updateBackgroundEvent) {
        super.updateBackground(updateBackgroundEvent);
    }

    @Override
    protected void injectComponent(AppInjectionComponent component) {
        component.inject(this);
    }

}
