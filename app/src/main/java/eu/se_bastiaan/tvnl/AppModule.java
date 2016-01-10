package eu.se_bastiaan.tvnl;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.se_bastiaan.tvnl.content.EventBus;

@Module
public class AppModule {

    @NonNull
    private final Application app;

    AppModule(@NonNull Application app) {
        this.app = app;
    }

    @Provides
    @NonNull
    @Singleton
    Context provideContext() {
        return app;
    }

    @Provides
    @Singleton
    EventBus provideBus() {
        return new EventBus();
    }

    @Provides
    Resources provideResources(Context context) {
        return context.getResources();
    }

}
