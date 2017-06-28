 package com.codepath.apps.restclienttemplate;

 import android.content.Intent;
 import android.os.Bundle;
 import android.support.v7.app.AppCompatActivity;
 import android.support.v7.widget.LinearLayoutManager;
 import android.support.v7.widget.RecyclerView;
 import android.util.Log;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.widget.Toast;

 import com.codepath.apps.restclienttemplate.models.Tweet;
 import com.loopj.android.http.JsonHttpResponseHandler;

 import org.json.JSONArray;
 import org.json.JSONException;
 import org.json.JSONObject;

 import java.util.ArrayList;

 import cz.msebera.android.httpclient.Header;

 public class TimelineActivity extends AppCompatActivity {

    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;

    static final int REQUEST_CODE = 1;

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.timeline, menu);
         return true;
     }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApp.getRestClient();
        // TODO: ButterKnife

        // find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        // init the arraylist (data source)
        tweets = new ArrayList<>();
        // construct the adapter from this data source
        tweetAdapter = new TweetAdapter(tweets);
        // RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        rvTweets.setAdapter(tweetAdapter);

        populateTimeline();
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
             //   Log.d("TwitterClient", response.toString());
                // iterate through the JSON array
                // for each entry, deserialize the JSON object

                for (int i = 0; i < response.length(); i++) {
                    // convert each object to a Tweet model
                    // add that Tweet model to our data source
                    // notify the adapter that we've added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

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

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         // Handle presses on the action bar items
         switch (item.getItemId()) {
             case R.id.miCompose:
                 composeMessage();
                 return true;
             case R.id.miProfile:
                 showProfileView();
                 return true;
             default:
                 return super.onOptionsItemSelected(item);
         }
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         Tweet tweet = data.getParcelableExtra("Tweet");
         tweets.add(0, tweet);
         tweetAdapter.notifyItemInserted(0);
         rvTweets.scrollToPosition(0);
     }

     private void composeMessage(){
      //   Toast.makeText(this, "ComposeMessage", Toast.LENGTH_SHORT).show();

         // create an intent for the new activity
         Intent intent = new Intent(this, ComposeActivity.class);
         //
         startActivityForResult(intent, REQUEST_CODE);
     }

     private void showProfileView() {
         Toast.makeText(this, "showProfileView", Toast.LENGTH_SHORT).show();

     }
}
