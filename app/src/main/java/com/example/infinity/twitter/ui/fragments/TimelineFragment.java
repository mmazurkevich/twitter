package com.example.infinity.twitter.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.infinity.twitter.R;
import com.example.infinity.twitter.ui.activity.MainActivity;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TimelineFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        setHasOptionsMenu(true);
        final ListView listView = (ListView) view.findViewById(R.id.list);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(new IconicsDrawable((MainActivity) getActivity())
                .icon(FontAwesome.Icon.faw_file_text_o)
                .color(Color.WHITE)
                .sizeDp(32));
        actionBar.setTitle(getResources().getString(R.string.home));
        TwitterApiClient twitterApiClient =  TwitterCore.getInstance().getApiClient(Twitter.getSessionManager().getActiveSession());
        twitterApiClient.getAccountService().verifyCredentials(true, false, new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                final UserTimeline userTimeline = new UserTimeline.Builder()
                        .userId(result.data.getId())
                        .build();
                final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder((MainActivity)getActivity())
                        .setTimeline(userTimeline)
                        .setViewStyle(R.style.TweetStyle)
                        .build();
                listView.setAdapter(adapter);
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText((MainActivity)getActivity(),
                        "Fail to load user tweets",
                        Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((MainActivity)getActivity()).pushFragment(FragmentType.TAG_HOME, FragmentFactory.getInstanse().getFragment(FragmentType.TAG_SINGLE_TWEET));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
