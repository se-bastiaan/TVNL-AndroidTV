package eu.se_bastiaan.tvnl.ui.viewpresenter;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import eu.se_bastiaan.tvnl.model.StreamInfo;

public class DescriptionPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        if (!(item instanceof StreamInfo)) return;
        StreamInfo streamInfo = (StreamInfo) item;

        viewHolder.getTitle().setText(streamInfo.getTitle());
        viewHolder.getSubtitle().setText(streamInfo.getSubtitle());
    }
}