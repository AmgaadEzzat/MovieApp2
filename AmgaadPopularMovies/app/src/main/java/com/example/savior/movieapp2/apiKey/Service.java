package com.example.savior.movieapp2.apiKey;

import com.example.savior.movieapp2.model.MovieResponse;
import com.example.savior.movieapp2.model.ReviewResponse;
import com.example.savior.movieapp2.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by savior on 6/22/2017.
 */

public interface Service {


    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);
    @GET("movie/popular")
    Call<MovieResponse> getMostPopularMovies(@Query("api_key") String apiKey);
    @GET("movie/{sort}")
    Call<MovieResponse> getMovie(@Path("sort") String order, @Query("api_key") String key);
    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("movie_id") long id, @Query("api_key") String apiKey);
    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("movie_id") long id, @Query("api_key") String apiKey);


}
