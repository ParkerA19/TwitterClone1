package com.codepath.apps.restclienttemplate.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.restclienttemplate.SmartFragmentStatePagerAdapter;

/**
 * Created by pandrews on 7/3/17.
 */

public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {


    private String tabTitles[] = new String[] {"Home", "Mentions"};
    private Context context;

    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    // return the total # of fragments
    @Override
    public int getCount() {
        return 2;
    }

    // return the fragment to use depending on the position
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new HomeTimelineFragment();
        else if (position == 1)
            return new MentionsTimelineFragment();
        else
            return null;
    }
    // return title
    public CharSequence getPageTitle(int position) {
        // generate title based on item position
        return tabTitles[position];
    }
}
