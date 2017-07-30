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