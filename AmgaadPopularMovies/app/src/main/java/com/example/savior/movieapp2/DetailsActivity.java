package com.example.savior.movieapp2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savior.movieapp2.adapter.ReviewsAdapter;
import com.example.savior.movieapp2.adapter.TrailerAdapter;
import com.example.savior.movieapp2.apiKey.Client;
import com.example.savior.movieapp2.apiKey.Service;
import com.example.savior.movieapp2.model.Movie;
import com.example.savior.movieapp2.model.Review;
import com.example.savior.movieapp2.model.ReviewResponse;
import com.example.savior.movieapp2.model.Trailer;
import com.example.savior.movieapp2.model.TrailerResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.savior.movieapp2.data.Contract.CONTENT_URI;
import static com.example.savior.movieapp2.data.Contract.FavouriteMoviesEntry.COLUMN_MOVIE_ID;
import static com.example.savior.movieapp2.data.Contract.FavouriteMoviesEntry.COLUMN_OVERVIEW;
import static com.example.savior.movieapp2.data.Contract.FavouriteMoviesEntry.COLUMN_POSTER_PATH;
import static com.example.savior.movieapp2.data.Contract.FavouriteMoviesEntry.COLUMN_RATING;
import static com.example.savior.movieapp2.data.Contract.FavouriteMoviesEntry.COLUMN_TITLE;

/**
 * Created by savior on 6/22/2017.
 */

public class DetailsActivity extends AppCompatActivity {
    Movie movie;
    TextView title, overView, rating, releaseDate;
    ImageView imageView;
    ImageButton imageButton;
    RecyclerView recyclerView;
    RecyclerView reviewsRecyclers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_details);


        imageView = (ImageView) findViewById(R.id.image_details);
        title = (TextView) findViewById(R.id.title_details);
        overView = (TextView) findViewById(R.id.overview_details);
        rating = (TextView) findViewById(R.id.vote_average);
        releaseDate = (TextView) findViewById(R.id.release_date);
        recyclerView = (RecyclerView) findViewById(R.id.trailer_details);
        reviewsRecyclers = (RecyclerView) findViewById(R.id.review_details);
        imageButton = (ImageButton) findViewById(R.id.favorite);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        reviewsRecyclers.setLayoutManager(manager);
        Intent intent = getIntent();
        movie = intent.getParcelableExtra(Intent.EXTRA_TEXT);



        if (CheckFavorite()) {
            //it's favorite
            imageButton.setImageResource(R.drawable.ic_favorite_black_24dp);
            imageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public   void  onClick(View v) {
                    imageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    getContentResolver().delete(CONTENT_URI,
                            COLUMN_MOVIE_ID + "=?",
                            new String[]{String.valueOf(movie.getId())}
                    );

                }
            });

        } else {
            //it's not favorit
            imageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                    ContentValues values = new ContentValues();
                    String Title = movie.getOriginalTitle();
                    String PosterPath = movie.getPosterPath();
                    values.put(COLUMN_TITLE, Title);
                    values.put(COLUMN_OVERVIEW, movie.getOverview());
                    values.put(COLUMN_RATING, movie.getVoteAverage());
                    values.put(COLUMN_POSTER_PATH, PosterPath);
                    values.put(COLUMN_MOVIE_ID, movie.getId());



                    getContentResolver().insert(CONTENT_URI, values);
                }
            });

        }


        if (movie != null) {
            String Title = movie.getOriginalTitle();
            String PosterPath = movie.getPosterPath();
            //Toast.makeText(this, "" + movie.getOriginalTitle(), Toast.LENGTH_SHORT).show();
            title.setText(String.valueOf(Title));
            overView.setText(movie.getOverview());
            releaseDate.setText(movie.getReleaseDate());
            rating.setText(String.valueOf(movie.getVoteAverage() + "/10"));
            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w185//"+PosterPath)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);

        } else {

            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }
        long movie_id = movie.getId();
        LoadTrailerJSON(movie_id);
        LoadReviewJSON(movie_id);


    }


    private synchronized boolean  CheckFavorite() {
        Cursor cursor = getContentResolver().query(CONTENT_URI,
                new String[]{COLUMN_MOVIE_ID},
                COLUMN_MOVIE_ID + "=?",
                new String[]{String.valueOf(movie.getId())},
                null);

        if ((cursor != null) && (cursor.getCount() > 0)) {
            Toast.makeText(this, "cursor=" + cursor.toString(), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }

    }


    private void LoadTrailerJSON(long id) {
        Service apiService = Client.getClient().create(Service.class);

        Call<TrailerResponse> call = apiService.getMovieTrailers(id, BuildConfig.API_KEY);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                Toast.makeText(DetailsActivity.this, "loading JSON", Toast.LENGTH_SHORT).show();
                int statusCode = response.code();
                List<Trailer> trailers = response.body().getResults();
                recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailers));
                // recyclerView.smoothScrollToPosition(0);

            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.e("Error", t.toString());
                Toast.makeText(DetailsActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void LoadReviewJSON(long id) {

        Service apiService = Client.getClient().create(Service.class);

        Call<ReviewResponse> call = apiService.getMovieReviews(id, BuildConfig.API_KEY);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                Toast.makeText(DetailsActivity.this, "loading JSON", Toast.LENGTH_SHORT).show();
                int statusCode = response.code();
                List<Review> reviews = response.body().getResults();
                reviewsRecyclers.setAdapter(new ReviewsAdapter(getApplicationContext(), reviews));
                reviewsRecyclers.smoothScrollToPosition(0);

            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                //    Log error here since request failed
                Log.e("Error", t.toString());
                Toast.makeText(DetailsActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }

        });

    }

}


