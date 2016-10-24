/*
 * Copyright (C) 2016 Brian Wernick,
 * Copyright (C) 2015 Sébastiaan Versteeg,
 * Copyright (C) 2015 The Android Open Source Project
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

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import com.google.android.exoplayer.upstream.UriDataSource;

import eu.se_bastiaan.tvnl.exomedia.okhttp.OkUriDataSource;

/**
 * A RenderBuilder for parsing and creating the renderers for
 * Smooth Streaming streams.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class SmoothStreamRenderBuilder extends com.devbrackets.android.exomedia.core.builder.SmoothStreamRenderBuilder {

    public SmoothStreamRenderBuilder(Context context, String userAgent, String url) {
        this(context, userAgent, url, AudioManager.STREAM_MUSIC);
    }

    public SmoothStreamRenderBuilder(Context context, String userAgent, String url, int streamType) {
        super(context, userAgent, url, streamType);
    }

    @SuppressWarnings("UnusedParameters") // Context kept for consistency with the HLS and Dash builders
    protected UriDataSource createManifestDataSource(Context context, String userAgent) {
        return new OkUriDataSource(context, userAgent);
    }

}