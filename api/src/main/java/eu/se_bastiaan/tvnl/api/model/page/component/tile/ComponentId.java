package eu.se_bastiaan.tvnl.api.model.page.component.tile;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static eu.se_bastiaan.tvnl.api.model.page.component.tile.ComponentId.GRID_CONTINUE_WATCHING;
import static eu.se_bastiaan.tvnl.api.model.page.component.tile.ComponentId.GRID_FAVOURITE_EPISODES;
import static eu.se_bastiaan.tvnl.api.model.page.component.tile.ComponentId.GRID_FAVOURITE_SERIES;
import static eu.se_bastiaan.tvnl.api.model.page.component.tile.ComponentId.GRID_HISTORY;

@Retention(RetentionPolicy.SOURCE)
@StringDef({GRID_CONTINUE_WATCHING, GRID_FAVOURITE_EPISODES, GRID_FAVOURITE_SERIES, GRID_HISTORY})
public @interface ComponentId {
    String GRID_CONTINUE_WATCHING = "grid-continue-watching";
    String GRID_FAVOURITE_EPISODES = "grid-favourite-episodes";
    String GRID_FAVOURITE_SERIES = "grid-favourite-series";
    String GRID_HISTORY = "grid-history";
}