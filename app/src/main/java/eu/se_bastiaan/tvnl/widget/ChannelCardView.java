package eu.se_bastiaan.tvnl.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.Target;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.se_bastiaan.tvnl.R;
import me.zhanghai.android.materialprogressbar.HorizontalProgressDrawable;

public class ChannelCardView extends BaseCardView {

    private int color;
    private Target target;
    private String currentImageUrl = "empty";

    @Bind(R.id.main_image)
    ImageView imageView;
    @Bind(R.id.overlay_image)
    ImageView overlayImageView;
    @Bind(R.id.title_text)
    TextView titleTextView;
    @Bind(R.id.content_text)
    TextView contentTextView;
    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Bind(R.id.info_field)
    RelativeLayout infoField;

    public ChannelCardView(Context context) {
        this(context, null);
    }

    public ChannelCardView(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.Widget_Leanback_MoreCardViewStyle);
    }

    public ChannelCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.channel_card_view, this);

        ButterKnife.bind(this, v);

        setBackgroundResource(R.color.grey);
        setCardType(BaseCardView.CARD_TYPE_INFO_UNDER);
        setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ALWAYS);

        HorizontalProgressDrawable progressDrawable = new HorizontalProgressDrawable(context);
        progressDrawable.setUseIntrinsicPadding(false);
        progressBar.setProgressDrawable(progressDrawable);

        setForeground(ResourcesCompat.getDrawable(getMainImageView().getResources(), R.drawable.card_ripple_background, null));
    }


    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    public void setTitleText(CharSequence text) {
        if (titleTextView == null)
            return;
        titleTextView.setText(text);
    }

    public CharSequence getTitleText() {
        if (titleTextView == null)
            return null;
        return titleTextView.getText();
    }

    public CharSequence getContentTextView() {
        if(contentTextView == null)
            return null;
        return contentTextView.getText();
    }

    public void setContentTextView(CharSequence text) {
        if(contentTextView == null)
            return;
        contentTextView.setText(text);
    }

    public void setColor(@ColorRes int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setInfoAreaBackgroundColor(int color) {
        this.infoField.setBackgroundColor(color);
    }

    public ImageView getMainImageView() {
        return imageView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setMainImage(Drawable drawable) {
        setMainImage(drawable, true);
    }

    public void setMainImage(Drawable drawable, boolean fade) {
        if (imageView == null) {
            return;
        }

        imageView.setImageDrawable(drawable);
        if (drawable == null) {
            imageView.animate().cancel();
            imageView.setAlpha(0.6f);
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            if (fade) {
                fadeIn(imageView);
            } else {
                imageView.animate().cancel();
                imageView.setAlpha(0.6f);
            }
        }
    }

    public void setMainImageDimensions(int width, int height) {
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        imageView.setLayoutParams(lp);
    }

    public void setOverlayImage(@DrawableRes int res) {
        overlayImageView.setImageResource(res);
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public String getCurrentImage() {
        return currentImageUrl;
    }

    public void setCurrentImage(String currentImageUrl) {
        this.currentImageUrl = currentImageUrl;
    }

    private void fadeIn(View v) {
        v.setAlpha(0f);
        v.animate().alpha(0.4f).setDuration(v.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime)).start();
    }

}