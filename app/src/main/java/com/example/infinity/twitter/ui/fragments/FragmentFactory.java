package com.example.infinity.twitter.ui.fragments;

/**
 * Created by infinity on 29.09.15.
 */
public class FragmentFactory {

    private static FragmentFactory instanse;

    private FragmentFactory() { }

    public static FragmentFactory getInstanse(){
        if (instanse == null)
            synchronized (FragmentFactory.class){
                if (instanse == null)
                    instanse = new FragmentFactory();
            }
        return instanse;
    }

    public BaseFragment getFragment(String type){
        switch (type){
            case FragmentType.TAG_HOME:
                return new TimelineFragment();
            case FragmentType.TAG_PROFILE:
                return new ProfileFragment();
            case FragmentType.TAG_LOCATION:
                return new MapFragment();
            case FragmentType.TAG_SETTINGS:
                return new SettingsFragment();
            case FragmentType.TAG_SINGLE_TWEET:
                return new SingleTweetFragment();
            case FragmentType.TAG_IMAGE:
                return new ImageFragment();
        }
        return null;
    }

}
