package eu.se_bastiaan.tvnl.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.squareup.otto.Subscribe;

import eu.se_bastiaan.tvnl.AppInjectionComponent;
import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.event.UpdateBackgroundEvent;
import eu.se_bastiaan.tvnl.model.OverviewGridItem;
import eu.se_bastiaan.tvnl.ui.activity.base.BaseActivity;
import eu.se_bastiaan.tvnl.ui.fragment.DetailsFragment;

public class DetailsActivity extends BaseActivity {

    public static Intent startActivity(Activity activity, OverviewGridItem item) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra(DetailsFragment.ITEM_ARG, item);
        activity.startActivity(intent);
        return intent;
    }

    public static Intent buildIntent(Context context, OverviewGridItem item, int notifId){
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(DetailsFragment.ITEM_ARG, item);
        intent.putExtra(DetailsFragment.NOTIFICATION_ARG, notifId);
        return intent;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_details);
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
