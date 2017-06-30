package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpClient;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailsActivity extends AppCompatActivity {

    // instance fields
    AsyncHttpClient client;
    // the tweet to display
    Tweet tweet;

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvScreenName) TextView tvScreenName;
    @BindView(R.id.tvDate) TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        // initialize the client
        client = new AsyncHttpClient();

        // unwrap the movie passed in vie intent, using its simple name as a key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d("DetailsActivity", String.format("Showing details for %s", tweet.body));

        // set the title and overview
        tvBody.setText(tweet.body);
        tvDate.setText(tweet.createdAt);
        tvScreenName.setText("@" + tweet.user.screenName);
        tvUserName.setText(tweet.user.name);

        // ivTrailer -- set the backdrop image
        String imageUrl = tweet.user.profileImageUrl;


        Glide.with(getApplicationContext())
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 25, 0))
                //      .placeholder(placeholderId)
                //      .error(placeholderId)
                .into(ivProfileImage);
    }

}
