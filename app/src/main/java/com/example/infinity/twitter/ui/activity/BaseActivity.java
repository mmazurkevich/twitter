package com.example.infinity.twitter.ui.activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

import com.example.infinity.twitter.R;
import com.example.infinity.twitter.ui.fragments.BaseFragment;
import com.example.infinity.twitter.ui.fragments.FragmentFactory;
import com.example.infinity.twitter.ui.fragments.FragmentType;
import com.example.infinity.twitter.ui.fragments.MapFragment;
import com.example.infinity.twitter.ui.fragments.ProfileFragment;
import com.example.infinity.twitter.ui.fragments.SettingsFragment;
import com.example.infinity.twitter.ui.fragments.TimelineFragment;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by infinity on 28.09.15.
 */
public class BaseActivity extends AppCompatActivity {

    private TabHost mTabHost;
    private Boolean mBackPressed;
    private HashMap<String, Stack<Fragment>> mStacks;
    private String mCurrentTab;

    public void initTabHost(View v){
        final View view = v;
        mStacks = new HashMap<String, Stack<Fragment>>();
        mStacks.put(FragmentType.TAG_HOME, new Stack<Fragment>());
        mStacks.put(FragmentType.TAG_PROFILE, new Stack<Fragment>());
        mStacks.put(FragmentType.TAG_LOCATION, new Stack<Fragment>());
        mStacks.put(FragmentType.TAG_SETTINGS, new Stack<Fragment>());

        mTabHost = (TabHost)view.findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabHost.TabSpec mTabSpec;

        TabHost.TabContentFactory content = new TabHost.TabContentFactory(){

            @Override
            public View createTabContent(String tag) {
                return view.findViewById(android.R.id.tabcontent);
            }
        };

        mTabSpec = mTabHost.newTabSpec(FragmentType.TAG_HOME);
        mTabSpec.setIndicator("", new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_home)
                .color(Color.WHITE)
                .sizeDp(32));
        mTabSpec.setContent(content);
        mTabHost.addTab(mTabSpec);

        mTabSpec = mTabHost.newTabSpec(FragmentType.TAG_PROFILE);
        mTabSpec.setIndicator("", new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_user)
                .color(Color.WHITE)
                .sizeDp(32));
        mTabSpec.setContent(content);
        mTabHost.addTab(mTabSpec);

        mTabSpec = mTabHost.newTabSpec(FragmentType.TAG_LOCATION);
        mTabSpec.setIndicator("", new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_map_marker)
                .color(Color.WHITE)
                .sizeDp(32));
        mTabSpec.setContent(content);
        mTabHost.addTab(mTabSpec);

        mTabSpec = mTabHost.newTabSpec(FragmentType.TAG_SETTINGS);
        mTabSpec.setIndicator("", new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_cog)
                .color(Color.WHITE)
                .sizeDp(32));
        mTabSpec.setContent(content);
        mTabHost.addTab(mTabSpec);
        mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).getBackground().setColorFilter(getResources().getColor(R.color.color_primary), PorterDuff.Mode.CLEAR);
        mTabHost.setOnTabChangedListener(listener);
        mCurrentTab = FragmentType.TAG_HOME;
        pushFragment(FragmentType.TAG_HOME, FragmentFactory.getInstanse().getFragment(FragmentType.TAG_HOME));
    }

    private TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
            mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).getBackground().setColorFilter(getResources().getColor(R.color.color_primary), PorterDuff.Mode.CLEAR);
            mCurrentTab = tabId;
            if(mStacks.get(tabId).size() == 0) {
                switch (tabId) {
                    case FragmentType.TAG_HOME:
                        pushFragment(tabId, FragmentFactory.getInstanse().getFragment(FragmentType.TAG_HOME));
                        break;
                    case FragmentType.TAG_PROFILE:
                        pushFragment(tabId, FragmentFactory.getInstanse().getFragment(FragmentType.TAG_PROFILE));
                        break;
                    case FragmentType.TAG_LOCATION:
                        pushFragment(tabId, FragmentFactory.getInstanse().getFragment(FragmentType.TAG_LOCATION));
                        break;
                    case FragmentType.TAG_SETTINGS:
                        pushFragment(tabId, FragmentFactory.getInstanse().getFragment(FragmentType.TAG_SETTINGS));
                        break;
                }
            }else
                pushFragment(tabId, mStacks.get(tabId).lastElement());
        }
    };

    public void pushFragment(String tabId, Fragment fragment) {
        mBackPressed = false;
        if (mStacks.get(mCurrentTab).size() == 0){
            mStacks.get(tabId).push(fragment);
            getFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment).commit();
        }else{
            Fragment element = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 1);
            if (element.equals(fragment)){
                popFragment();
            }else
                mStacks.get(tabId).push(fragment);
                getFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment).commit();
        }
    }

    public void popFragment(){
        if (mBackPressed){
            Fragment fragment = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);
            mStacks.get(mCurrentTab).pop();
            getFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment).commit();
        }else {
            Fragment fragment = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 1);
            getFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mStacks.get(mCurrentTab).size() == 1) {
            super.onBackPressed();
        } else {
            mBackPressed = true;
            popFragment();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mStacks.get(mCurrentTab).size() == 0){
            return;
        }
        mStacks.get(mCurrentTab).lastElement().onActivityResult(requestCode, resultCode, data);
    }
}
