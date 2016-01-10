package eu.se_bastiaan.tvnl.ui.viewpresenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.model.Channel;
import eu.se_bastiaan.tvnl.widget.ChannelCardView;
import me.zhanghai.android.materialprogressbar.HorizontalProgressDrawable;

public class ChannelGridPresenter extends Presenter {

    private Context context;
    private final int cardWidth;
    private final int cardHeight;
    private final int defaultInfoBackgroundColor, progressBarColor;

    public ChannelGridPresenter(Context context) {
        this.context = context;
        this.defaultInfoBackgroundColor = context.getResources().getColor(R.color.grey);
        this.progressBarColor = context.getResources().getColor(R.color.white);
        this.cardWidth = context.getResources().getDimensionPixelSize(R.dimen.card_thumbnail_width);
        this.cardHeight = context.getResources().getDimensionPixelSize(R.dimen.card_thumbnail_height);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        this.context = parent.getContext();

        ChannelCardView cardView = new ChannelCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                HorizontalProgressDrawable progressDrawable = new HorizontalProgressDrawable(context);
                progressDrawable.setUseIntrinsicPadding(false);
                if (selected) {
                    setInfoAreaBackgroundColor(context.getResources().getColor(getColor()));
                    progressDrawable.setTint(progressBarColor);
                } else {
                    setInfoAreaBackgroundColor(defaultInfoBackgroundColor);
                    progressDrawable.setTint(context.getResources().getColor(getColor()));
                }
                getProgressBar().setProgressDrawable(progressDrawable);
                super.setSelected(selected);
            }
        };

        cardView.setInfoAreaBackgroundColor(defaultInfoBackgroundColor);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Channel channel = (Channel) item;
        final ChannelCardView cardView = (ChannelCardView) viewHolder.view;

        cardView.setMainImageDimensions(cardWidth, cardHeight);
        cardView.setTitleText(channel.getTitle());
        cardView.setContentTextView(channel.getSubtitle());
        cardView.setColor(channel.getColor());

        HorizontalProgressDrawable progressDrawable = new HorizontalProgressDrawable(context);
        progressDrawable.setUseIntrinsicPadding(false);
        if(!cardView.isSelected()) {
            progressDrawable.setTint(context.getResources().getColor(channel.getColor()));
        } else {
            progressDrawable.setTint(progressBarColor);
        }

        cardView.getProgressBar().setProgressDrawable(progressDrawable);
        cardView.getProgressBar().setMax(100);
        cardView.getProgressBar().setProgress(channel.getProgress());
        cardView.setOverlayImage(channel.getDrawable());

        final boolean fade = !TextUtils.equals(channel.getImage(), cardView.getCurrentImage());
        if(fade)
            cardView.setMainImage(null);

        cardView.setCurrentImage(channel.getImage());

        if (channel.getImage() != null) {
            Target target = new SimpleTarget<GlideBitmapDrawable>() {
                @Override
                public void onResourceReady(final GlideBitmapDrawable drawable, GlideAnimation glideAnimation) {
                    cardView.setMainImage(drawable, fade);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    cardView.setMainImage(ResourcesCompat.getDrawable(context.getResources(), R.drawable.placeholder, null), fade);
                }
            };

            Glide.with(context).load(channel.getImage())
                    .centerCrop()
                    .override(cardWidth, cardHeight)
                    .into(target);
            cardView.setTarget(target);
        } else {
            cardView.setMainImage(ResourcesCompat.getDrawable(context.getResources(), R.drawable.placeholder, null), fade);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ChannelCardView cardView = (ChannelCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setMainImage(null);
        if (cardView.getTarget() != null) {
            Glide.clear(cardView.getTarget());
            cardView.setTarget(null);
        }
    }

}
