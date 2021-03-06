package com.example.savior.movieapp2.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.savior.movieapp2.R;
import com.example.savior.movieapp2.model.Review;

import java.util.List;

/**
 * Created by savior on 6/22/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MovieViewHolder> {

    private static List<Review> reviews;
    private static Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView reviewDetails;

        public MovieViewHolder(View v) {
            super(v);
            reviewDetails = (TextView) v.findViewById(R.id.review_details);

        }
    }

    public ReviewsAdapter(Context context, List<Review> reviews) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public ReviewsAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_movie, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.reviewDetails.setText(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
