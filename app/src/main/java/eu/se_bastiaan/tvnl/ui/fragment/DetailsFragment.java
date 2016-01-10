package eu.se_bastiaan.tvnl.ui.fragment;

import android.os.Bundle;
import android.support.v17.leanback.app.DetailsSupportFragment;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

import eu.se_bastiaan.tvnl.model.OverviewGridItem;
import eu.se_bastiaan.tvnl.ui.presenter.DetailsFragPresenter;
import nucleus.factory.PresenterFactory;
import nucleus.factory.ReflectionPresenterFactory;
import nucleus.factory.RequiresPresenter;
import nucleus.view.PresenterLifecycleDelegate;
import nucleus.view.ViewWithPresenter;

@RequiresPresenter(DetailsFragPresenter.class)
public class DetailsFragment extends DetailsSupportFragment implements ViewWithPresenter<DetailsFragPresenter> {

    public static final String NOTIFICATION_ARG = "NOTIFICATION_ARG";
    public static final String ITEM_ARG = "ITEM_ARG";

    public static DetailsFragment newInstance(OverviewGridItem item) {
        Bundle args = new Bundle();
        args.putParcelable(ITEM_ARG, item);
        return newInstance(args);
    }

    public static DetailsFragment newInstance(Bundle args) {
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(args);
        return detailsFragment;
    }

    /* Lifecycle */

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null)
            presenterDelegate.onRestoreInstanceState(bundle.getBundle(PRESENTER_STATE_KEY));

        setOnItemViewClickedListener(onItemViewClickedListener);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBundle(PRESENTER_STATE_KEY, presenterDelegate.onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterDelegate.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenterDelegate.onPause(getActivity().isFinishing());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public OverviewGridItem getItem() {
        return getActivity().getIntent().getParcelableExtra(ITEM_ARG);
    }

    public int getNotificationId() {
        return getActivity().getIntent().getIntExtra(NOTIFICATION_ARG, DetailsFragPresenter.NO_NOTIFICATION);
    }

    /* Presenter */

    private static final String PRESENTER_STATE_KEY = "presenter_state";
    private PresenterLifecycleDelegate<DetailsFragPresenter> presenterDelegate =
            new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<DetailsFragPresenter>fromViewClass(getClass()));

    @Override
    public PresenterFactory<DetailsFragPresenter> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    @Override
    public void setPresenterFactory(PresenterFactory<DetailsFragPresenter> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    @Override
    public DetailsFragPresenter getPresenter() {
        return presenterDelegate.getPresenter();
    }

    /* Listeners etc */

    private OnItemViewClickedListener onItemViewClickedListener = new OnItemViewClickedListener() {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            getPresenter().itemClicked(DetailsFragment.this, item);
        }
    };

}
