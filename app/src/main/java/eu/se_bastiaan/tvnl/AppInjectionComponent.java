package eu.se_bastiaan.tvnl;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Component;
import eu.se_bastiaan.tvnl.network.NetworkModule;
import eu.se_bastiaan.tvnl.network.glide.OkHttpGlideModule;
import eu.se_bastiaan.tvnl.service.RecommendationService;
import eu.se_bastiaan.tvnl.service.recommendation.RecommendationContentProvider;
import eu.se_bastiaan.tvnl.ui.activity.DetailsActivity;
import eu.se_bastiaan.tvnl.ui.activity.MainActivity;
import eu.se_bastiaan.tvnl.ui.activity.SearchActivity;
import eu.se_bastiaan.tvnl.ui.activity.VideoPlayerActivity;
import eu.se_bastiaan.tvnl.ui.fragment.PlaybackOverlayFragment;
import eu.se_bastiaan.tvnl.ui.presenter.BrowseFragPresenter;
import eu.se_bastiaan.tvnl.ui.presenter.DetailsFragPresenter;
import eu.se_bastiaan.tvnl.ui.presenter.PlaybackOverlayFragPresenter;
import eu.se_bastiaan.tvnl.ui.presenter.SearchFragPresenter;
import eu.se_bastiaan.tvnl.ui.presenter.VideoPlayerFragPresenter;

@Singleton
@Component(
        modules = {
                AppModule.class,
                NetworkModule.class
        }
)
public interface AppInjectionComponent {

    void inject(@NonNull MainActivity activity);
    void inject(@NonNull DetailsActivity activity);
    void inject(@NonNull SearchActivity activity);
    void inject(@NonNull VideoPlayerActivity activity);

    void inject(@NonNull BrowseFragPresenter presenter);
    void inject(@NonNull VideoPlayerFragPresenter presenter);
    void inject(@NonNull PlaybackOverlayFragPresenter presenter);
    void inject(@NonNull PlaybackOverlayFragment presenter);
    void inject(@NonNull SearchFragPresenter presenter);
    void inject(@NonNull DetailsFragPresenter presenter);

    void inject(@NonNull OkHttpGlideModule module);

    void inject(@NonNull RecommendationContentProvider contentProvider);
    void inject(@NonNull RecommendationService service);

    final class Initializer {
        private Initializer() {
            /* No instances. */
        }

        public static AppInjectionComponent init(TVNLApplication application) {
            return DaggerAppInjectionComponent
                    .builder()
                    .appModule(new AppModule(application))
                    .networkModule(new NetworkModule())
                    .build();
        }
    }

}
