package serverside.example.com.popularmovies1.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import serverside.example.com.popularmovies1.Model.MoviesResponse;

public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

  @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key" )String apiKey);
}
