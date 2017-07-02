package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;


@Parcel
/**
 * Created by pandrews on 6/26/17.
 */

public class Tweet {

    // list out the attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public String time;
    public String imageUrl;
    public boolean favorited;
    public boolean retweeted;
    public String favortieCount;
    public String retweetCount;
    public String replyCount;

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");

        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.time = TimeFormatter.getTimeDifference(tweet.createdAt);
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.favortieCount = jsonObject.getString("favorite_count");
        tweet.retweetCount = jsonObject.getString("retweet_count");
    //    tweet.time = TimeFormatter.getTimeStamp(tweet.createdAt);
        if (jsonObject.has("entities")) {
            JSONObject object = jsonObject.getJSONObject("entities");
            if (object.has("media")) {
                JSONArray array = object.getJSONArray("media");
                if (array.length() > 0) {
                    JSONObject object2 = array.getJSONObject(0);
                    if (object2.has("media_url_https")) {
                        tweet.imageUrl = object2.getString("media_url_https");
                    }

                }
            }

        }
        return tweet;
    }

}
