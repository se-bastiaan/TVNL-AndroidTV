package eu.se_bastiaan.tvnl.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.squareup.otto.Subscribe;

import eu.se_bastiaan.tvnl.AppInjectionComponent;
import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.event.UpdateBackgroundEvent;
import eu.se_bastiaan.tvnl.ui.activity.base.BaseActivity;
import eu.se_bastiaan.tvnl.ui.fragment.SearchFragment;

public class SearchActivity extends BaseActivity {

    public static Intent startActivity(Activity activity) {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
        return intent;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_search);
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
    public boolean onSearchRequested() {
        ((SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment)).startRecognition();
        return true;
    }

    @Override
    protected void injectComponent(AppInjectionComponent component) {
        component.inject(this);
    }

}
