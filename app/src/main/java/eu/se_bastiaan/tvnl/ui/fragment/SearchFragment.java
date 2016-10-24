package eu.se_bastiaan.tvnl.ui.fragment;

import android.os.Bundle;
import android.support.v17.leanback.app.SearchSupportFragment;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

import eu.se_bastiaan.tvnl.ui.presenter.SearchFragPresenter;
import nucleus.factory.PresenterFactory;
import nucleus.factory.ReflectionPresenterFactory;
import nucleus.factory.RequiresPresenter;
import nucleus.view.PresenterLifecycleDelegate;
import nucleus.view.ViewWithPresenter;

@RequiresPresenter(SearchFragPresenter.class)
public class SearchFragment extends SearchSupportFragment implements ViewWithPresenter<SearchFragPresenter> {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null)
            presenterDelegate.onRestoreInstanceState(bundle.getBundle(PRESENTER_STATE_KEY));
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setSearchResultProvider(searchResultProvider);
        setOnItemViewClickedListener(onItemViewClickedListener);
        setOnItemViewSelectedListener(onItemViewSelectedListener);
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
    public void onDestroy() {
        super.onDestroy();
        presenterDelegate.onDestroy(getActivity().isFinishing());
    }

    /* Presenter */

    private static final String PRESENTER_STATE_KEY = "presenter_state";
    private PresenterLifecycleDelegate<SearchFragPresenter> presenterDelegate =
            new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<SearchFragPresenter>fromViewClass(getClass()));

    @Override
    public PresenterFactory<SearchFragPresenter> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    @Override
    public void setPresenterFactory(PresenterFactory<SearchFragPresenter> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    @Override
    public SearchFragPresenter getPresenter() {
        return presenterDelegate.getPresenter();
    }

    /* Listeners etc. */


    SearchSupportFragment.SearchResultProvider searchResultProvider = new SearchSupportFragment.SearchResultProvider() {
        @Override
        public ObjectAdapter getResultsAdapter() {
            return getPresenter().getRowsAdapter();
        }

        @Override
        public boolean onQueryTextChange(String newQuery) {
            if (newQuery.length() > 3)
                getPresenter().queryByWords(newQuery);
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            getPresenter().queryByWords(query);
            return true;
        }
    };

    OnItemViewClickedListener onItemViewClickedListener = new OnItemViewClickedListener() {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            getPresenter().itemClicked(SearchFragment.this, item);
        }
    };

    OnItemViewSelectedListener onItemViewSelectedListener = new OnItemViewSelectedListener() {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            getPresenter().itemSelected(item);
        }
    };

}