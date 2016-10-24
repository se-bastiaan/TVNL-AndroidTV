/*
 * Copyright (C) 2016 Brian Wernick
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

package eu.se_bastiaan.tvnl.exomedia;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.devbrackets.android.exomedia.core.builder.RenderBuilder;
import com.devbrackets.android.exomedia.core.video.ExoVideoView;
import com.devbrackets.android.exomedia.type.MediaSourceType;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;

import eu.se_bastiaan.tvnl.exomedia.builder.DashRenderBuilder;
import eu.se_bastiaan.tvnl.exomedia.builder.HlsRenderBuilder;
import eu.se_bastiaan.tvnl.exomedia.builder.SmoothStreamRenderBuilder;

/**
 * This is a support VideoView that will use the standard VideoView on devices below
 * JellyBean.  On devices with JellyBean and up we will use the ExoPlayer in order to
 * better support HLS streaming and full 1080p video resolutions which the VideoView
 * struggles with, and in some cases crashes.
 * <p>
 * To an external user this view should have the same APIs used with the standard VideoView
 * to help with quick implementations.
 */
@SuppressWarnings("UnusedDeclaration")
public class TVNLVideoView extends EMVideoView {

    public TVNLVideoView(Context context) {
        super(context);
    }

    public TVNLVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TVNLVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TVNLVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setVideoURI(Uri uri) {
        RenderBuilder builder = null;

        if(uri != null) {
            builder = getRendererBuilder(MediaSourceType.getByLooseComparison(uri), uri);
        }

        setVideoURI(uri, builder);
    }

    protected RenderBuilder getRendererBuilder(@NonNull MediaSourceType renderType, @NonNull Uri uri) {
        String userAgent = "";
        if (videoViewImpl instanceof ExoVideoView) {
            userAgent = ((ExoVideoView) videoViewImpl).getUserAgent();
        }
        switch (renderType) {
            case HLS:
                return new HlsRenderBuilder(getContext().getApplicationContext(), userAgent, uri.toString());
            case DASH:
                return new DashRenderBuilder(getContext().getApplicationContext(), userAgent, uri.toString());
            case SMOOTH_STREAM:
                return new SmoothStreamRenderBuilder(getContext().getApplicationContext(), userAgent, uri.toString());
            default:
                return new RenderBuilder(getContext().getApplicationContext(), userAgent, uri.toString());
        }
    }

}