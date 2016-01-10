package eu.se_bastiaan.tvnl;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;

import eu.se_bastiaan.tvnl.service.RecommendationService;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class TVNLApplication extends MultiDexApplication {

    @Nullable
    private volatile AppInjectionComponent appInjectionComponent;
    private static TVNLApplication sThis;

    @Override
    public void onCreate() {
        super.onCreate();
        sThis = this;

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            //Timber.plant(new CrashlyticsLogTree(Log.INFO));
        }

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Intent recommendationIntent = new Intent(this, RecommendationService.class);
        startService(recommendationIntent);
    }

    public static TVNLApplication get() {
        return sThis;
    }

    @NonNull
    public AppInjectionComponent appComponent() {
        if (appInjectionComponent == null) {
            synchronized (Application.class) {
                if (appInjectionComponent == null) {
                    appInjectionComponent = AppInjectionComponent.Initializer.init(this);
                }
            }
        }

        //noinspection ConstantConditions
        return appInjectionComponent;
    }

}
