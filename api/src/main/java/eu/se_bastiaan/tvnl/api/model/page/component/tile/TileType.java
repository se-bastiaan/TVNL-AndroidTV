package eu.se_bastiaan.tvnl.api.model.page.component.tile;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static eu.se_bastiaan.tvnl.api.model.page.component.tile.TileType.ASSET;
import static eu.se_bastiaan.tvnl.api.model.page.component.tile.TileType.OTHER;
import static eu.se_bastiaan.tvnl.api.model.page.component.tile.TileType.REGIONAL_RADIO;
import static eu.se_bastiaan.tvnl.api.model.page.component.tile.TileType.TEASER;

@Retention(RetentionPolicy.SOURCE)
@StringDef({ASSET, OTHER, REGIONAL_RADIO, TEASER})
public @interface TileType {
    String ASSET = "asset";
    String OTHER = "other";
    String REGIONAL_RADIO = "regionalradio";
    String TEASER = "teaser";
}