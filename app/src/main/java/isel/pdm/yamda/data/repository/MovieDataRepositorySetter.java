package isel.pdm.yamda.data.repository;

import android.util.Log;

import java.util.List;

import isel.pdm.yamda.data.api.common.IMovieApi;
import isel.pdm.yamda.data.entity.tmdb.MovieDTO;
import isel.pdm.yamda.data.entity.tmdb.MovieListingDTO;
import isel.pdm.yamda.model.mapper.ModelEntitiesDataMapper;
import isel.pdm.yamda.data.repository.datasource.MovieDataStoreFactory;
import isel.pdm.yamda.model.entity.Movie;
import isel.pdm.yamda.model.repository.IMovieRepository;
import isel.pdm.yamda.presentation.view.contract.ILoadDataView;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * This class uses a factory and a mapper to retrieve the data to the model layer
 */
public class MovieDataRepositorySetter implements IMovieRepository {

    protected final String TAG = "DEBUG_" + getClass().getSimpleName();

    private final MovieDataStoreFactory factory;
    private final ModelEntitiesDataMapper mapper;

    public MovieDataRepositorySetter(MovieDataStoreFactory factory, ModelEntitiesDataMapper mapper) {
        this.factory = factory;
        this.mapper = mapper;
    }

    @Override
    public void setTheatersMovies(ILoadDataView<List<Movie>> presenter, int page) {
        // Always get the online data ?
        final IMovieApi storage = this.factory.createCloudDataStore();

        storage.getTheatersMovies(page).enqueue(new MovieListingCallback(presenter));
    }

    @Override
    public void setSoonMovies(ILoadDataView<List<Movie>> presenter, int page) {
        // Always get the online data ?
        final IMovieApi storage = this.factory.createCloudDataStore();

        storage.getSoonMovies(page).enqueue(new MovieListingCallback(presenter));
    }

    @Override
    public void setTopMovies(ILoadDataView<List<Movie>> presenter, int page) {
        // Always get the online data ?
        final IMovieApi storage = this.factory.createCloudDataStore();

        storage.getTopMovies(page).enqueue(new MovieListingCallback(presenter));
    }

    @Override
    public void  setMovie(final ILoadDataView<Movie> presenter,int id) {
        // get cached or online
        final IMovieApi storage = this.factory.create(id);

        storage.getMovie(id).enqueue(new Callback<MovieDTO>() {
            @Override
            public void onResponse(Response<MovieDTO> response, Retrofit retrofit) {
                Movie movie = mapper.transform(response.body());
                Log.v(TAG, "Downloading a Movie: " + response.raw());

                presenter.hideLoading();
                presenter.setData(movie);
            }

            @Override
            public void onFailure(Throwable t) {
                presenter.hideLoading();
                presenter.showError(t.getMessage());
            }
        });
    }

    @Override
    public void setMovieSearch(final ILoadDataView<List<Movie>> presenter, String search, int page) {
        // Always get the online data ?
        final IMovieApi storage = this.factory.createCloudDataStore();

        storage.getMoviesSearch(search, page).enqueue(new MovieListingCallback(presenter));
    }


    /**
     * Class for representing a list callback
     */
    private class MovieListingCallback implements Callback<MovieListingDTO> {

        private  ILoadDataView<List<Movie>> presenter;

        public MovieListingCallback(ILoadDataView<List<Movie>> presenter) {
            this.presenter = presenter;
        }

        @Override
        public void onResponse(Response<MovieListingDTO> response, Retrofit retrofit) {
            List<Movie> list = mapper.transform(response.body());
            Log.v(TAG, "Downloading a MovieList: " + response.raw());

            presenter.hideLoading();
            presenter.setData(list);
        }

        @Override
        public void onFailure(Throwable t) {
            presenter.hideLoading();
            presenter.showError(t.getMessage());
        }
    }
}