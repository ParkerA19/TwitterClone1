package com.codepath.apps.restclienttemplate;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_heart;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_heart_stroke;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_retweet;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_retweet_stroke;


/**
 * Created by pandrews on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    List<Tweet> mTweets;
    Context context;
    TwitterClient client;
    private TweetAdapterListener mlistener;

    // define an interface required by the viewholder
    public interface TweetAdapterListener {
        public void onItemSelected(View view, int position);
    }


    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        mlistener = listener;
    }

    // for each row, inflate the layout and cache references into ViewHolder


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        client = TwitterApp.getRestClient();

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // get the data according to position
        final Tweet tweet = mTweets.get(position);
        int favorite = (tweet.favorited) ? ic_vector_heart : ic_vector_heart_stroke;
        int retweet = (tweet.retweeted) ? ic_vector_retweet : ic_vector_retweet_stroke;

        // populate the views according to position
        holder.tvUsername.setText(tweet.user.name);
        holder.tvScreenName.setText("@" + tweet.user.screenName);
        holder.tvBody.setText(tweet.body);
        holder.tvTime.setText(tweet.time);
        holder.tvComment.setText("hey");
        holder.tvRetweet.setText(tweet.retweetCount);
        holder.tvLike.setText(tweet.favortieCount);
        holder.tvMessage.setText("hi");
        holder.ibLike.setImageResource(favorite);
        holder.ibRetweet.setImageResource(retweet);
        holder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.favorited) {
                    client.unlikeTweet(tweet.uid, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            tweet.favorited = false;
                            // TODO; change color back to white
                            holder.ibLike.setImageResource(ic_vector_heart_stroke);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("TwitterClient", responseString);
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }
                    });





                } else {
                    client.likeTweet(tweet.uid, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            tweet.favorited = true;
                            // TODO; change color to red
                            holder.ibLike.setImageResource(ic_vector_heart);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("TwitterClient", responseString);
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }
                    });


                }
            }
        });
//
//        holder.ibComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "TODO: Comment", Toast.LENGTH_SHORT).show();
//                // TODO: add a comment ability
//            }
//        });
//
//        holder.ibRetweet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "TODO: Retweet", Toast.LENGTH_SHORT).show();
//                // TODO: add a Retweet ability
//            }
//        });
//
//        holder.ibMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "TODO: Message", Toast.LENGTH_SHORT).show();
//                // TODO: add a DM ability
//            }
//        });

//

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .into(holder.ivProfileImage);

        if (tweet.imageUrl != null) {
            Glide.with(context)
                    .load(tweet.imageUrl)
                    .bitmapTransform(new RoundedCornersTransformation(context, 5, 0))
                    .into(holder.ivImage);
        }

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @Nullable@BindView(R.id.ivImage) ImageView ivImage;
        @BindView(R.id.tvUserName) TextView tvUsername;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.tvScreenName) TextView tvScreenName;
        @BindView(R.id.ibComment) ImageButton ibComment;
        @BindView(R.id.ibRetweet) ImageButton ibRetweet;
        @BindView(R.id.ibLike) ImageButton ibLike;
        @BindView(R.id.ibMessage) ImageButton ibMessage;
        @BindView(R.id.tvComment) TextView tvComment;
        @BindView(R.id.tvRetweet) TextView tvRetweet;
        @BindView(R.id.tvLike) TextView tvLike;
        @BindView(R.id.tvMessage) TextView tvMessage;


        public ViewHolder (View itemView) {
            super(itemView);

            // bind with butterknife
            ButterKnife.bind(this, itemView);

            // set onClickListener
            itemView.setOnClickListener(this);

            // perform findViewById lookups

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener != null) {
                        // get the position of row element
                        int position = getAdapterPosition();
                        // fire the listener callback
                        mlistener.onItemSelected(view, position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            // get item position
            int position = getAdapterPosition();
            //make sure the position is valid
            if (position != RecyclerView.NO_POSITION) {
                // get the tweet and the position
                Tweet tweet = mTweets.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, DetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // show the activity
                context.startActivity(intent);
            }

        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
