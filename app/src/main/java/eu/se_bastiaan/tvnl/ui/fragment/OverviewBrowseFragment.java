package eu.se_bastiaan.tvnl.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v17.leanback.app.BrowseSupportFragment;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.ui.activity.SearchActivity;
import eu.se_bastiaan.tvnl.ui.presenter.BrowseFragPresenter;
import nucleus.factory.PresenterFactory;
import nucleus.factory.ReflectionPresenterFactory;
import nucleus.factory.RequiresPresenter;
import nucleus.view.PresenterLifecycleDelegate;
import nucleus.view.ViewWithPresenter;

@RequiresPresenter(BrowseFragPresenter.class)
public class OverviewBrowseFragment extends BrowseSupportFragment implements ViewWithPresenter<BrowseFragPresenter> {

    private static final int PERMISSIONS_REQUEST = 1;

    /* Lifecycle */

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null)
            presenterDelegate.onRestoreInstanceState(bundle.getBundle(PRESENTER_STATE_KEY));

        setOnSearchClickedListener(onSearchClickedListener);
        setOnItemViewSelectedListener(onItemSelectedListener);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupUIElements();
    }

    /* Presenter */

    private static final String PRESENTER_STATE_KEY = "presenter_state";
    private PresenterLifecycleDelegate<BrowseFragPresenter> presenterDelegate =
            new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<BrowseFragPresenter>fromViewClass(getClass()));

    @Override
    public PresenterFactory<BrowseFragPresenter> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    @Override
    public void setPresenterFactory(PresenterFactory<BrowseFragPresenter> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    @Override
    public BrowseFragPresenter getPresenter() {
        return presenterDelegate.getPresenter();
    }

    /* Listeners etc */

    private void setupUIElements() {
        setTitle(getString(R.string.app_name));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getResources().getColor(R.color.primary));
        setSearchAffordanceColor(getResources().getColor(R.color.primary_dark));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PERMISSIONS_REQUEST == requestCode) {
            boolean success = true;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    success = false;
                    break;
                }
            }

            if (success)
                SearchActivity.startActivity(getActivity());
        }
    }

    private View.OnClickListener onSearchClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                SearchActivity.startActivity(getActivity());
            } else {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST);
            }
        }
    };

    private OnItemViewSelectedListener onItemSelectedListener = new OnItemViewSelectedListener() {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            getPresenter().itemSelected(item);
        }
    };

    private OnItemViewClickedListener onItemViewClickedListener = new OnItemViewClickedListener() {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            getPresenter().itemClicked(OverviewBrowseFragment.this, item);
        }
    };

    public void showErrorMessage(@StringRes int res) {
        Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
    }

    public void showErrorMessage() {
        showErrorMessage(R.string.unknown_error);
    }

}
