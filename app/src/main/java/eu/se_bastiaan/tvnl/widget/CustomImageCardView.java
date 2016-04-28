package eu.se_bastiaan.tvnl.widget;

import android.content.Context;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.se_bastiaan.tvnl.R;

public class CustomImageCardView extends ImageCardView {

    private Palette.Swatch customSelectedSwatch;
    private Target target;
    private Integer titleTextColor, contentTextColor;

    @BindView(R.id.title_text)
    TextView titleTextView;
    @BindView(R.id.content_text)
    TextView contentTextView;

    public CustomImageCardView(Context context) {
        super(context);
        init();
    }

    public CustomImageCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomImageCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ALWAYS);
        setBackgroundResource(R.color.grey);
        titleTextColor = titleTextView.getCurrentTextColor();
        contentTextColor = contentTextView.getCurrentTextColor();
    }

    public Palette.Swatch getCustomSelectedSwatch() {
        return customSelectedSwatch;
    }

    public void setCustomSelectedSwatch(Palette.Swatch customSelectedSwatch) {
        this.customSelectedSwatch = customSelectedSwatch;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getContentTextView() {
        return contentTextView;
    }

    public Target getTarget() {
			return target;
		}

    public void setTarget(Target target) {
			this.target = target;
		}

}