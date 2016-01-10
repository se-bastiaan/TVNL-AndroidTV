package eu.se_bastiaan.tvnl.ui.presenter.base;

import android.os.Bundle;

import eu.se_bastiaan.tvnl.AppInjectionComponent;
import eu.se_bastiaan.tvnl.TVNLApplication;
import nucleus.presenter.RxPresenter;
import nucleus.view.ViewWithPresenter;
import rx.Observable;
import rx.functions.Func1;

public abstract class BasePresenter<V extends ViewWithPresenter> extends RxPresenter<V> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        injectComponent(TVNLApplication.get().appComponent());
    }

    protected Observable<V> viewFiltered() {
        return view().filter(new Func1<V, Boolean>() {
            @Override
            public Boolean call(V v) {
                return v != null;
            }
        });
    }

    protected abstract void injectComponent(AppInjectionComponent component);

}
