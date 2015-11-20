package org.liuyichen.fifteenyan.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.liuyichen.fifteenyan.App;
import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.network.Category;
import org.liuyichen.fifteenyan.ui.fragment.StoryFragment;

/**
 * Created by root on 15-3-6.
 * and ...
 */
public  class NavigationAdapter extends FragmentPagerAdapter {

    private final String[] TITLES;
    private final StoryFragment[] storyFragments = {
            StoryFragment.create(Category.LATEST),
            StoryFragment.create(Category.HOT),
    };

    public NavigationAdapter(FragmentManager fm) {
        super(fm);
        TITLES = App.getSelf().getResources().getStringArray(R.array.topic);
    }

    @Override
    public Fragment getItem(int position) {
        return storyFragments[position];
    }

    @Override
    public int getCount() {
        return storyFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    public StoryFragment[] getStoryFragments() {
        return storyFragments;
    }
}
