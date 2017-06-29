package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    // instance variables
    TwitterClient client;
    EditText etTweet;
    TextView tvCharacterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // set the client
        client = TwitterApp.getRestClient();

        // set the edit text TODO: use ButterKnife
        etTweet = (EditText) findViewById(R.id.etTweet);
        tvCharacterCount = (TextView) findViewById(R.id.tvCharacterCount);

        int characters = 140;
        tvCharacterCount.setText(String.valueOf(characters));

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
                if (available >= 20)
                    tvCharacterCount.setTextColor(Color.GRAY);
                else if (available < 20)
                    tvCharacterCount.setTextColor(Color.RED);
                else if (available < 0) {
                    // TODO: make the button unclickable
                    // TODO: remove the max length in the xml file for the edit text
                }


                //This sets a textview to the current length
                tvCharacterCount.setText(String.valueOf(available));
            }

            public void afterTextChanged(Editable s) {
            }
        };

        etTweet.addTextChangedListener(mTextEditorWatcher);



    }

    public void close(View V) {
        Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
        startActivity(intent);
    }

}
