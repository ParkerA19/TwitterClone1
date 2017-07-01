package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by pandrews on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    List<Tweet> mTweets;
    Context context;

    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    // for each row, inflate the layout and cache references into ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the data according to position
        Tweet tweet = mTweets.get(position);

        // populate the views according to position
        holder.tvUsername.setText(tweet.user.name);
        holder.tvScreenName.setText("@" + tweet.user.screenName);
        holder.tvBody.setText(tweet.body);
        holder.tvTime.setText(tweet.time);
        holder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "TODO: Like", Toast.LENGTH_SHORT).show();
                // TODO: add a like and change the heart to red
            }
        });

        holder.ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "TODO: Comment", Toast.LENGTH_SHORT).show();
                // TODO: add a comment ability
            }
        });

        holder.ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "TODO: Retweet", Toast.LENGTH_SHORT).show();
                // TODO: add a Retweet ability
            }
        });

        holder.ibMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "TODO: Message", Toast.LENGTH_SHORT).show();
                // TODO: add a DM ability
            }
        });

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
//        public ImageView ivProfileImage;
//        public TextView tvUsername;
//        public TextView tvBody;
//        public TextView tvTime;
//        public TextView tvScreenName;
//        public ImageButton ibComment;
//        public ImageButton ibLike;
//        public ImageButton ibRetweet;
//        public ImageButton ibMessage;

        @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @Nullable@BindView(R.id.ivImage) ImageView ivImage;
        @BindView(R.id.tvUserName) TextView tvUsername;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.tvScreenName) TextView tvScreenName;
        @BindView(R.id.ibComment) ImageButton ibComment;
        @BindView(R.id.ibLike) ImageButton ibLike;
        @BindView(R.id.ibRetweet) ImageButton ibRetweet;
        @BindView(R.id.ibMessage) ImageButton ibMessage;

        public ViewHolder (View itemView) {
            super(itemView);

            // bind with butterknife
            ButterKnife.bind(this, itemView);

            // set onClickListener
            itemView.setOnClickListener(this);

            // perform findViewById lookups
//
//            ibComment = (ImageButton) itemView.findViewById(R.id.ibComment);
//            ibLike = (ImageButton) itemView.findViewById(R.id.ibLike);
//            ibRetweet = (ImageButton) itemView.findViewById(R.id.ibRetweet);
//            ibMessage = (ImageButton) itemView.findViewById(R.id.ibMessage);

//            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
//            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
//            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
//            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
//            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
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
