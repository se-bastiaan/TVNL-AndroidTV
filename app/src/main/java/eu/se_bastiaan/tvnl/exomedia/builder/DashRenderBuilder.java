/*
 * Copyright (C) 2016 Brian Wernick,
 * Copyright (C) 2015 Sébastiaan Versteeg,
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.se_bastiaan.tvnl.exomedia.builder;

import android.content.Context;
import android.media.AudioManager;

import com.google.android.exoplayer.upstream.UriDataSource;

import eu.se_bastiaan.tvnl.exomedia.okhttp.OkUriDataSource;

/**
 * A RenderBuilder for parsing and creating the renderers for
 * DASH streams.
 */
public class DashRenderBuilder extends com.devbrackets.android.exomedia.core.builder.DashRenderBuilder {

    public DashRenderBuilder(Context context, String userAgent, String url) {
        this(context, userAgent, url, AudioManager.STREAM_MUSIC);
    }

    public DashRenderBuilder(Context context, String userAgent, String url, int streamType) {
        super(context, userAgent, url, streamType);
    }

    protected UriDataSource createManifestDataSource(Context context, String userAgent) {
        return new OkUriDataSource(context, userAgent);
    }

}