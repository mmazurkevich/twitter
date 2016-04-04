package com.example.infinity.twitter.ui.fragments;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.infinity.twitter.R;
import com.example.infinity.twitter.ui.activity.MainActivity;
import com.example.infinity.twitter.ui.view.ExtendTextView;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;

/**
 * Created by infinity on 24.09.15.
 */
public class ProfileFragment extends BaseFragment {

    private ImageView mLayout;
    private ImageView mProfileIcon;
    private ImageView mProfileVerified;
    private ExtendTextView mName;
    private ExtendTextView mNickName;
    private ExtendTextView mTweets;
    private ExtendTextView mFollowers;
    private ExtendTextView mFollowing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_profile, container, false);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(getResources().getString(R.string.profile));
        mLayout = (ImageView) view.findViewById(R.id.profile_banner);
        mProfileIcon = (ImageView) view.findViewById(R.id.profile_icon);
        mProfileVerified = (ImageView) view.findViewById(R.id.profile_verified);
        mName = (ExtendTextView) view.findViewById(R.id.name);
        mNickName = (ExtendTextView) view.findViewById(R.id.nickname);
        mTweets = (ExtendTextView) view.findViewById(R.id.tweets);
        mFollowers = (ExtendTextView) view.findViewById(R.id.followers);
        mFollowing = (ExtendTextView) view.findViewById(R.id.following);
        TwitterApiClient twitterApiClient =  TwitterCore.getInstance().getApiClient(Twitter.getSessionManager().getActiveSession());
        twitterApiClient.getAccountService().verifyCredentials(true, false, new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                Picasso.with((MainActivity)getActivity()).load(result.data.profileBannerUrl).into(mLayout);
                Picasso.with((MainActivity)getActivity()).load(result.data.profileImageUrl).into(mProfileIcon);
                if (result.data.verified)
                    mProfileVerified.setVisibility(View.VISIBLE);
                mName.setText(result.data.name);
                mNickName.setText("@" + result.data.screenName);
                mTweets.setText(String.valueOf(result.data.statusesCount));
                mFollowers.setText(String.valueOf(result.data.followersCount));
                mFollowing.setText(String.valueOf(result.data.friendsCount));
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText((MainActivity)getActivity(),
                        "Fail to load user info",
                        Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
