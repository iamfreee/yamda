package isel.pdm.yamda.data.api;

import isel.pdm.yamda.data.entity.tmdb.ConfigurationDTO;
import isel.pdm.yamda.data.entity.tmdb.ImagesDTO;
import isel.pdm.yamda.data.entity.tmdb.MovieDTO;
import isel.pdm.yamda.data.entity.tmdb.MovieListingDTO;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Restrives information from online database through {@code Retrofit}.
 */
public interface ITheMovieDbServiceAPI {

    String BASE_URL = "http://api.themoviedb.org";
    String API_KEY = "3b4c65c3780fc1ef44ec5500b186d833";

    /**
     * Retrieves configuration information, such as image base url and sizes of images.
     *
     * @param api_key api_key used to retrieve information
     * @return
     */
    @GET("/3/configuration")
    Call<ConfigurationDTO> getConfig(@Query("api_key") String api_key);


    /**
     *  Retrieves a specific movie through his {@code id}
     *
     * @param id         movie id
     * @param api_key    api_key used to retrieve information
     * @param lang       to retrieve in this language (ISO 639-1 code)
     * @return
     */
    @GET("/3/movie/{id}")
    Call<MovieDTO> getMovie(@Path("id") int id,
                         @Query("api_key") String api_key,
                            @Query("language") String lang);

    /**
     *  Retrieves a list of movies that are now on cinema.
     *
     * @param API_KEY     api_key used to retrieve information
     * @param page        page (Minimum 1, maximum 1000)
     * @param language    to retrieve in this language (ISO 639-1 code)
     * @return
     */
    @GET("/3/movie/now_playing")
    Call<MovieListingDTO> getNowPlaying(@Query("api_key") String API_KEY,
                                    @Query("page") int page,
                                    @Query("language") String language);

    /**
     *  Retrieves a list of movies that are to come to cinema.
     *
     * @param API_KEY     api_key used to retrieve information
     * @param page        page (Minimum 1, maximum 1000)
     * @param language    to retrieve in this language (ISO 639-1 code)
     * @return
     */
    @GET("/3/movie/upcoming")
    Call<MovieListingDTO> getUpcoming(@Query("api_key") String API_KEY,
                                  @Query("page") int page,
                                  @Query("language") String language);

    /**
     *  Retrieves a list of movies that are the most popular on TMDb.
     *
     * @param API_KEY     api_key used to retrieve information
     * @param page        page (Minimum 1, maximum 1000)
     * @param language    to retrieve in this language (ISO 639-1 code)
     * @return
     */
    @GET("/3/movie/popular")
    Call<MovieListingDTO> getMostPopular(@Query("api_key") String API_KEY,
                                     @Query("page") int page,
                                     @Query("language") String language);

    /**
     * Retrieves a list of movies by title
     *
     * @param API_KEY api_key used to retrieve information
     * @param movie   movie name
     * @param page    page (Minimum 1, maximum 1000)
     * @param lang    to retrieve in this language (ISO 639-1 code)
     * @return
     */
    @GET("/3/search/movie")
    Call<MovieListingDTO> getSearchedMovies(@Query("api_key") String API_KEY,
                                               @Query("query") String movie,
                                               @Query("page") int page,
                                               @Query("language") String lang);

    /**
     *  Retrieves a list of images of a specific movie.
     *
     * @param id                 movie id
     * @param api_key            api_key used to retrieve information
     * @param lang               to retrieve in this language (ISO 639-1 code)
     * @param response_append
     * @param img_lang
     * @return
     */
    @GET("/3/movie/{id}/images")
    Call<ImagesDTO> getMovieImages(@Path("id") int id,
                                   @Query("api_key") String api_key,
                                   @Query("language") String lang,
                                   @Query("append_to_response") String response_append,
                                   @Query("include_image_language") String img_lang);
}