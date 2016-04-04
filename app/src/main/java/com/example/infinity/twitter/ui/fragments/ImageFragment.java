package com.example.infinity.twitter.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.infinity.twitter.R;
import com.example.infinity.twitter.ui.activity.MainActivity;
import com.example.infinity.twitter.ui.view.TouchImageView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

/**
 * Created by infinity on 25.09.15.
 */
public class ImageFragment extends BaseFragment {

    private LinearLayout mLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        setHasOptionsMenu(true);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.main_layout);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(new IconicsDrawable((MainActivity) getActivity())
                .icon(FontAwesome.Icon.faw_chevron_left)
                .color(Color.WHITE)
                .sizeDp(32));
        actionBar.setTitle(getResources().getString(R.string.home));
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String url = sharedPref.getString(SingleTweetFragment.IMAGE_URL, null);
        TouchImageView img = new TouchImageView((MainActivity) getActivity());
        Picasso.with((MainActivity) getActivity()).load(url).into(img);
        img.setMaxZoom(6f);
        LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        img.setLayoutParams(layoutParams);
        mLinearLayout.addView(img);
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

}
