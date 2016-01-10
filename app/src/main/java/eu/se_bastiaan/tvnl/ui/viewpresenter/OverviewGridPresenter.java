package eu.se_bastiaan.tvnl.ui.viewpresenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.Presenter;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.model.Broadcast;
import eu.se_bastiaan.tvnl.model.OverviewGridItem;
import eu.se_bastiaan.tvnl.model.Promo;
import eu.se_bastiaan.tvnl.model.Recommendation;
import eu.se_bastiaan.tvnl.model.Series;
import eu.se_bastiaan.tvnl.model.Timeline;
import eu.se_bastiaan.tvnl.model.VideoFragment;
import eu.se_bastiaan.tvnl.util.AnimUtil;
import eu.se_bastiaan.tvnl.widget.CustomImageCardView;

public class OverviewGridPresenter extends Presenter {

	private Context context;
    protected final int cardWidth;
	protected final int cardHeight;
	private final int defaultInfoBackgroundColor;
	private final int defaultSelectedInfoBackgroundColor;

	public OverviewGridPresenter(Context context) {
        this.context = context;
		this.defaultSelectedInfoBackgroundColor = context.getResources().getColor(R.color.primary_dark);
        this.defaultInfoBackgroundColor = context.getResources().getColor(R.color.grey);
        this.cardWidth = context.getResources().getDimensionPixelSize(R.dimen.card_thumbnail_width);
        this.cardHeight = context.getResources().getDimensionPixelSize(R.dimen.card_thumbnail_height);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent) {
        this.context = parent.getContext();

		CustomImageCardView cardView = new CustomImageCardView(parent.getContext()) {
			@Override
			public void setSelected(boolean selected) {
				if (getCustomSelectedSwatch() != null && selected) {
					setInfoAreaBackgroundColor(getCustomSelectedSwatch().getRgb());
				} else {
                    setInfoAreaBackgroundColor(selected ? defaultSelectedInfoBackgroundColor : defaultInfoBackgroundColor);
                }
				super.setSelected(selected);
			}
		};

		cardView.setInfoAreaBackgroundColor(defaultInfoBackgroundColor);
		cardView.setFocusable(true);
		cardView.setFocusableInTouchMode(true);

		return new ViewHolder(cardView);
	}

	@Override
	public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object object) {
        OverviewGridItem overview = (OverviewGridItem) object;

        final CustomImageCardView cardView = (CustomImageCardView) viewHolder.view;

        cardView.setTitleText(overview.getTitle());
        cardView.setContentText(overview.getSubtitle());
        cardView.getMainImageView().setAlpha(1f);
        cardView.getMainImageView().setPadding(0, 0, 0, 0);
        cardView.setMainImageDimensions(cardWidth, cardHeight);
        cardView.getMainImageView().setVisibility(View.GONE);
        cardView.setCustomSelectedSwatch(null);
        cardView.getTitleTextView().setMaxLines(1);
        cardView.getTitleTextView().setEllipsize(TextUtils.TruncateAt.END);
        cardView.getContentTextView().setVisibility(View.VISIBLE);

        if (overview.getImage() != null) {
            Target target = new SimpleTarget<GlideBitmapDrawable>() {
                @Override
                public void onResourceReady(final GlideBitmapDrawable drawable, GlideAnimation glideAnimation) {
                    Palette.from(drawable.getBitmap()).maximumColorCount(16).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            Palette.Swatch swatch = palette.getDarkVibrantSwatch();
                            if(swatch == null) {
                                swatch = palette.getDarkMutedSwatch();
                            }
                            cardView.setCustomSelectedSwatch(swatch);

                            cardView.getMainImageView().setImageDrawable(drawable);
                            cardView.getMainImageView().setVisibility(View.GONE);
                            AnimUtil.fadeIn(cardView.getMainImageView());
                        }
                    });
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    cardView.getMainImageView().setImageResource(R.drawable.placeholder);
                    cardView.getMainImageView().setAlpha(0.4f);
                    cardView.getMainImageView().setVisibility(View.GONE);
                    AnimUtil.fadeIn(cardView.getMainImageView());
                }
            };

            Glide.with(context).load(overview.getImage())
                    .centerCrop()
                    .override(cardWidth, cardHeight)
                    .into(target);
            cardView.setTarget(target);
        } else {
            cardView.getMainImageView().setImageResource(R.drawable.placeholder);
            cardView.getMainImageView().setAlpha(0.4f);
            cardView.getMainImageView().setVisibility(View.GONE);
            AnimUtil.fadeIn(cardView.getMainImageView());
        }
	}

	@Override
	public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
		CustomImageCardView cardView = (CustomImageCardView) viewHolder.view;
		// Remove references to images so that the garbage collector can free up memory
		cardView.setBadgeImage(null);
		cardView.setMainImage(null);
		if (cardView.getTarget() != null) {
            Glide.clear(cardView.getTarget());
			cardView.setTarget(null);
		}
	}

    public static List<OverviewGridItem> toOverviewGridItems(List<?> objects) {
        List<OverviewGridItem> returnData = new ArrayList<>();
        for (Object object : objects) {
            if(object instanceof Timeline) {
                if(((Timeline) object).getUnpublishAt() == null || ((Timeline) object).getUnpublishAt() > (System.currentTimeMillis() / 1000L))
                    returnData.add(((Timeline) object).toOverviewGridItem());
            } else if(object instanceof VideoFragment) {
                returnData.add(((VideoFragment) object).toOverviewGridItem());
            } else if(object instanceof Broadcast) {
                returnData.add(((Broadcast) object).toOverviewGridItem());
            } else if(object instanceof Series) {
                returnData.add(((Series) object).toOverviewGridItem());
            } else if(object instanceof Promo) {
                returnData.add(((Promo) object).toOverviewGridItem());
            } else if(object instanceof Recommendation) {
                returnData.add(((Recommendation) object).toOverviewGridItem());
            }
        }
        return returnData;
    }

}