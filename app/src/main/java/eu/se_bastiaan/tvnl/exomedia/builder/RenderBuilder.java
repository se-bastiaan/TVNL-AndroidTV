/*
 * Copyright (C) 2015 Brian Wernick,
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
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Build;

import com.devbrackets.android.exomedia.exoplayer.EMExoPlayer;
import com.devbrackets.android.exomedia.renderer.EMMediaCodecAudioTrackRenderer;
import com.devbrackets.android.exomedia.util.MediaUtil;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.text.TextTrackRenderer;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;

import eu.se_bastiaan.tvnl.exomedia.okhttp.OkUriDataSource;

/**
 * A default RenderBuilder that can process general
 * media urls including mkv, mp4, mp4, aac, etc.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class RenderBuilder extends com.devbrackets.android.exomedia.builder.RenderBuilder {
    protected static final int DROPPED_FRAME_NOTIFICATION_AMOUNT = 50;
    protected static final long MAX_JOIN_TIME = 5000;

    protected static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    protected static final int BUFFER_SEGMENTS_VIDEO = 200;
    protected static final int BUFFER_SEGMENTS_AUDIO = 54;
    protected static final int BUFFER_SEGMENTS_TEXT = 2;
    protected static final int BUFFER_SEGMENTS_TOTAL = BUFFER_SEGMENTS_VIDEO + BUFFER_SEGMENTS_AUDIO + BUFFER_SEGMENTS_TEXT;

    private final Context context;
    private final String userAgent;
    private final String uri;

    public RenderBuilder(Context context, String userAgent, String uri) {
        super(context, userAgent, uri);
        this.uri = uri;
        this.userAgent = userAgent;
        this.context = context;
    }

    @Override
    public void buildRenderers(EMExoPlayer player) {
        //Create the Sample Source to be used by the renderers
        Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(player.getMainHandler(), player);
        DataSource dataSource = new OkUriDataSource(context, bandwidthMeter, userAgent, true);

        ExtractorSampleSource sampleSource = new ExtractorSampleSource(Uri.parse(MediaUtil.getUriWithProtocol(uri)), dataSource,
               allocator, BUFFER_SEGMENT_SIZE * BUFFER_SEGMENTS_TOTAL);

        //Create the Renderers
        MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(context, sampleSource, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT,
                MAX_JOIN_TIME, null, true, player.getMainHandler(), player, DROPPED_FRAME_NOTIFICATION_AMOUNT);
        EMMediaCodecAudioTrackRenderer audioRenderer = new EMMediaCodecAudioTrackRenderer(sampleSource, null, true, player.getMainHandler(), player);
        TrackRenderer captionsRenderer = new TextTrackRenderer(sampleSource, player, player.getMainHandler().getLooper());


        //Create the Render list to send to the callback
        TrackRenderer[] renderers = new TrackRenderer[EMExoPlayer.RENDER_COUNT];
        renderers[EMExoPlayer.RENDER_VIDEO_INDEX] = videoRenderer;
        renderers[EMExoPlayer.RENDER_AUDIO_INDEX] = audioRenderer;
        renderers[EMExoPlayer.RENDER_CLOSED_CAPTION_INDEX] = captionsRenderer;
        player.onRenderers(renderers, bandwidthMeter);
    }

    public void cancel() {
        //Purposefully left blank
    }
}