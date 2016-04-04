package com.example.infinity.twitter.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.infinity.twitter.R;
import com.example.infinity.twitter.ui.activity.MainActivity;
import com.example.infinity.twitter.util.SwipeDetectorUtils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.List;

/**
 * Created by infinity on 26.09.15.
 */
public class SingleTweetFragment extends BaseFragment {

    public static final String IMAGE_URL = "IMAGE_URL";
    private List<Tweet> mTweets;
    private Integer mCurrentPosition = 0;
    private RelativeLayout mLayout;
    private FloatingActionButton mUpButton;
    private FloatingActionButton mDownButton;
    private TweetView mPreviesTweetView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_tweet, container, false);
        setHasOptionsMenu(true);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(new IconicsDrawable((MainActivity) getActivity())
                .icon(FontAwesome.Icon.faw_list)
                .color(Color.WHITE)
                .sizeDp(32));
        actionBar.setTitle(getResources().getString(R.string.home));
        mLayout = (RelativeLayout) view.findViewById(R.id.tweet_layout);
        SwipeDetectorUtils swipeDetectorUtils = new SwipeDetectorUtils(this, 1);
        swipeDetectorUtils.setSwipeDownDetector(mSwipeDownDetector);
        swipeDetectorUtils.setSwipeUpDetector(mSwipeUpDetector);
        mLayout.setOnTouchListener(swipeDetectorUtils);
        mUpButton = (FloatingActionButton) view.findViewById(R.id.button_up);
        mUpButton.setOnClickListener(mOnClickListenerUp);
        mDownButton = (FloatingActionButton) view.findViewById(R.id.button_down);
        mDownButton.setOnClickListener(mOnClickListenerDown);
        TwitterApiClient twitterApiClient =  TwitterCore.getInstance().getApiClient(Twitter.getSessionManager().getActiveSession());
        twitterApiClient.getAccountService().verifyCredentials(true, false, new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                final UserTimeline userTimeline = new UserTimeline.Builder()
                        .userId(result.data.getId())
                        .build();
                userTimeline.next(null, new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        mTweets = result.data.items;
                        getCurrentTweet();
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Toast.makeText((MainActivity) getActivity(),
                                "Fail to load first tweet",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText((MainActivity) getActivity(),
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
                ((MainActivity) getActivity()).onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private SwipeDetectorUtils.SwipeDownDetector mSwipeDownDetector = new SwipeDetectorUtils.SwipeDownDetector() {
        @Override
        public void onSwipeDownDetector() {
            moveDown();
        }
    };

    private SwipeDetectorUtils.SwipeUpDetector mSwipeUpDetector = new SwipeDetectorUtils.SwipeUpDetector() {
        @Override
        public void onSwipeUpDetector() {
            moveUp();
        }
    };

    private View.OnClickListener mOnClickListenerUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            moveUp();
        }
    };

    private View.OnClickListener mOnClickListenerDown = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            moveDown();
        }
    };

    private void moveUp(){
        if (mCurrentPosition != 0){
            --mCurrentPosition;
            getCurrentTweet();
        }
    }

    private void moveDown(){
        if (mCurrentPosition < mTweets.size()-1) {
            ++mCurrentPosition;
            getCurrentTweet();
        }
    }

    public void tweetTouch() {
        List<MediaEntity> list = mTweets.get(mCurrentPosition).entities.media;
        if (list != null) {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(IMAGE_URL, list.get(0).mediaUrl);
            editor.apply();
            ((MainActivity) getActivity()).pushFragment(FragmentType.TAG_HOME, FragmentFactory.getInstanse().getFragment(FragmentType.TAG_IMAGE));
        }
    }

    private void getCurrentTweet(){
        TweetUtils.loadTweet(mTweets.get(mCurrentPosition).getId(), new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                if (mPreviesTweetView != null)
                    mPreviesTweetView.removeAllViews();
                TweetView tweetView = new TweetView((MainActivity) getActivity(), result.data,
                        R.style.TweetStyle);
                mPreviesTweetView = tweetView;
                mLayout.addView(tweetView);
                SwipeDetectorUtils swipeDetectorUtils = new SwipeDetectorUtils(SingleTweetFragment.this, 2);
                swipeDetectorUtils.setSwipeDownDetector(mSwipeDownDetector);
                swipeDetectorUtils.setSwipeUpDetector(mSwipeUpDetector);
                tweetView.setOnTouchListener(swipeDetectorUtils);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText((MainActivity) getActivity(),
                        "Fail to load current tweet",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
