package eu.se_bastiaan.tvnl.api.model.page.component.tile;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static eu.se_bastiaan.tvnl.api.model.page.component.tile.TileMapping.DEDICATED;
import static eu.se_bastiaan.tvnl.api.model.page.component.tile.TileMapping.DESCRIPTION;
import static eu.se_bastiaan.tvnl.api.model.page.component.tile.TileMapping.FRAGMENT;
import static eu.se_bastiaan.tvnl.api.model.page.component.tile.TileMapping.LIVE;
import static eu.se_bastiaan.tvnl.api.model.page.component.tile.TileMapping.NORMAL;
import static eu.se_bastiaan.tvnl.api.model.page.component.tile.TileMapping.SEARCH;

@Retention(RetentionPolicy.SOURCE)
@StringDef({DEDICATED, DESCRIPTION, FRAGMENT, LIVE, NORMAL, SEARCH})
public @interface TileMapping {
    String DEDICATED = "dedicated";
    String DESCRIPTION = "description";
    String FRAGMENT = "fragment";
    String LIVE = "live";
    String NORMAL = "normal";
    String SEARCH = "search";
}