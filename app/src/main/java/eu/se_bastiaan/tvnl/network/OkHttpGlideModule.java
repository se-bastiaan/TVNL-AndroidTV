package eu.se_bastiaan.tvnl.network;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;

import javax.inject.Inject;

import eu.se_bastiaan.tvnl.TVNLApplication;

public class OkHttpGlideModule extends com.bumptech.glide.integration.okhttp.OkHttpGlideModule {

    @Inject
    OkHttpClient okHttpClient;

	@Override
    public void registerComponents(Context context, Glide glide) {
        TVNLApplication.get().appComponent().inject(this);
		glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
	}

}