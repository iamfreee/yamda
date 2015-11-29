package isel.pdm.yamda.presentation.presenter.common;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import isel.pdm.yamda.YamdaApplication;
import isel.pdm.yamda.model.entity.MovieListDetails;
import isel.pdm.yamda.presentation.navigator.Navigator;
import isel.pdm.yamda.presentation.presenter.base.IPresenter;
import isel.pdm.yamda.presentation.view.adapter.LazyAdapter;
import isel.pdm.yamda.presentation.view.contract.ILoadDataView;

public abstract class MovieListablePresenter implements IPresenter, ILoadDataView<List<MovieListDetails>>,
        AdapterView.OnItemClickListener {

    protected ListView listView;
    protected View loadingView;
    protected Activity activity;

    public MovieListablePresenter(Activity activity, ListView listView, View loading) {
        this.listView = listView;
        this.activity = activity;
        this.loadingView = loading;
    }

    /*
    |--------------------------------------------------------------------------
    | DataView Methods
    |--------------------------------------------------------------------------
    */
    @Override
    public void showLoading() {
        this.listView.setVisibility(View.GONE);
        this.loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        this.loadingView.setVisibility(View.GONE);
        this.listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        this.hideLoading();
    }

    @Override
    public void setData(List<MovieListDetails> data) {
        this.hideLoading();
        this.setListViewAdapter(data);
    }

    /*
    |--------------------------------------------------------------------------
    | Presenter Lifecycle
    |--------------------------------------------------------------------------
    */
    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    /*
    |--------------------------------------------------------------------------
    | Modify the view
    |--------------------------------------------------------------------------
    */
    private void setListViewAdapter(List<MovieListDetails> list) {
        this.listView.setAdapter(new LazyAdapter(this.activity, list));
        this.listView.setOnItemClickListener(this);
        //this.fragment.getViewContainer().invalidate();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieListDetails movie = ((MovieListDetails) parent.getAdapter().getItem(position));
        Navigator nav = ((YamdaApplication) this.activity.getApplication()).getNavigator();

        nav.navigateToMovieDetails(this.activity, movie);
    }

}