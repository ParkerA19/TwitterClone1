package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ComposeActivity extends AppCompatActivity {

    // instance variables
    TwitterClient client;
    User user;
 //   EditText etTweet;
 //   TextView tvCharacterCount;

    @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvCharacterCount) TextView tvCharacterCount;
    @BindView(R.id.etTweet) EditText etTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ButterKnife.bind(this);


        // set the client
        client = TwitterApp.getRestClient();

        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    user = User.fromJSON(response);

                    Glide.with(getApplicationContext())
                            .load(user.profileImageUrl)
                            .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 150, 0))
                            .into(ivProfileImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("ComposeActivity", responseString);
                throwable.printStackTrace();
            }
        });

        // set the edit text TODO: use ButterKnife
        etTweet = (EditText) findViewById(R.id.etTweet);
        tvCharacterCount = (TextView) findViewById(R.id.tvCharacterCount);



        updateCharacterCount();

    }

    public void onTweet(View v) {
        // get the text from the edit text
        String message = etTweet.getText().toString();

        client.sendTweet(message, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Tweet tweet = Tweet.fromJSON(response);
                            Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                            intent.putExtra(Tweet.class.getName(), Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Failed to send tweet", "wow");
                    }
                }

        );

    }

    public void updateCharacterCount() {
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int used = s.length();
                int available = 140 - used;
                if (available >= 0)
                    tvCharacterCount.setTextColor(Color.GRAY);
                else if (available < 0)
                    tvCharacterCount.setTextColor(Color.RED);



                //This sets a textview to the current length
                tvCharacterCount.setText(String.valueOf(available));
            }

            public void afterTextChanged(Editable s) {
            }
        };

        etTweet.addTextChangedListener(mTextEditorWatcher);



    }

    public void close(View V) {
        // set the intent
        Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
        // start the intent
        startActivity(intent);
    }

    public void onProfile(View V) {
        // set the new intent
        Intent intent = new Intent(ComposeActivity.this, ProfileActivity.class);
        // populate the new intent
        intent.putExtra(User.class.getName(), Parcels.wrap(user));
        // start Activity
        startActivity(intent);
    }

}
