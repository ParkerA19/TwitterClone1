package com.codepath.apps.restclienttemplate.models;

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

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.time = TimeFormatter.getTimeDifference(tweet.createdAt);
    //    tweet.time = TimeFormatter.getTimeStamp(tweet.createdAt);

        return tweet;
    }

    public String toString() {
        String b = this.body.toString();
        String u = this.user.toString();
        String c = this.createdAt.toString();
        String t = this.time.toString();
        return b + u + c+ t;
    }
}
