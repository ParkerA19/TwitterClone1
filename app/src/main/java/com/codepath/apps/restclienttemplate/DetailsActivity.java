package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_heart;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_heart_stroke;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_retweet;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_retweet_stroke;

public class DetailsActivity extends AppCompatActivity {

    // instance fields
    AsyncHttpClient clientA;
    // the tweet to display
    Tweet tweet;
    // twitter client
    TwitterClient client;
    // Context
    Context context;

    @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @Nullable@BindView(R.id.ivImage) ImageView ivImage;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvScreenName) TextView tvScreenName;
    @BindView(R.id.tvDate) TextView tvDate;
    @BindView(R.id.tvRetweets) TextView tvRetweets;
    @BindView(R.id.tvLikes) TextView tvLikes;
    @BindView(R.id.ibComment) ImageButton ibComment;
    @BindView(R.id.ibRetweet) ImageButton ibRetweet;
    @BindView(R.id.ibLike) ImageButton ibLike;
    @BindView(R.id.ibMessage) ImageButton imMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        // initialize the client
        clientA = new AsyncHttpClient();
        client = TwitterApp.getRestClient();

        context = getApplicationContext();

        // unwrap the movie passed in the intent, using its simple name as a key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d("DetailsActivity", String.format("Showing details for %s", tweet.body));


        int favorite = (tweet.favorited) ? ic_vector_heart : ic_vector_heart_stroke;
        int retweet = (tweet.retweeted) ? ic_vector_retweet : ic_vector_retweet_stroke;

        final String favString = (tweet.favoriteCount.equals("0")) ? "" : tweet.favoriteCount;
        final String retString = (tweet.retweetCount.equals("0")) ? "" : tweet.retweetCount;

        // set the title and overview
        tvBody.setText(tweet.body);
        tvDate.setText(tweet.createdAt);
        tvScreenName.setText("@" + tweet.user.screenName);
        tvUserName.setText(tweet.user.name);
        tvRetweets.setText(tweet.retweetCount + " Retweets");
        tvLikes.setText(tweet.favoriteCount + " Likes");
        ibLike.setImageResource(favorite);
        ibRetweet.setImageResource(retweet);

        // ivTrailer -- set the backdrop image
        String imageUrl = tweet.user.profileImageUrl;
        String url = tweet.imageUrl;


        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 150, 0))
                .into(ivProfileImage);

        Glide.with(context)
                .load(url)
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 25, 0))
                .into(ivImage);

        ivProfileImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // make intent
                Intent intent = new Intent(context, ProfileActivity.class);
                // add user to intent
                intent.putExtra(User.class.getName(), Parcels.wrap(tweet.user));
                // start activity
                startActivity(intent);

            }
        });

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.favorited) {
                    client.unlikeTweet(tweet.uid, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // set boolean to false
                            tweet.favorited = false;
                            // set the new image
                            ibLike.setImageResource(ic_vector_heart_stroke);
                            // set the new favorites count
                            tweet.favoriteCount = Integer.toString(Integer.parseInt(tweet.favoriteCount) - 1);
                            // set the new text
                            tvLikes.setText(tweet.favoriteCount + " Likes");
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
                            // set boolean to true
                            tweet.favorited = true;
                            // set the new image
                            ibLike.setImageResource(ic_vector_heart);
                            // set the new favorites count
                            tweet.favoriteCount = Integer.toString(Integer.parseInt(tweet.favoriteCount) + 1);
                            // set the new text
                            tvLikes.setText(tweet.favoriteCount + " Likes");
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
    }

}
