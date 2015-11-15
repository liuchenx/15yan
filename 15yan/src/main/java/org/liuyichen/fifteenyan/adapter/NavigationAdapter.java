package org.liuyichen.fifteenyan.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.api.Category;
import org.liuyichen.fifteenyan.fragment.StoryFragment;

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

    public NavigationAdapter(Context context, FragmentManager fm) {
        super(fm);
        TITLES = context.getResources().getStringArray(R.array.topic);
    }

    @Override
    public Fragment getItem(int position) {
        return storyFragments[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    public StoryFragment[] getStoryFragments() {
        return storyFragments;
    }
}
