/*
 * Copyright (C) 2017 Sébastiaan (github.com/se-bastiaan)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see . Also add information on how to contact you by electronic and paper mail.
 */

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