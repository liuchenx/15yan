package org.liuyichen.fifteenyan.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuItem;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.ui.activity.BaseActivty;
import org.liuyichen.fifteenyan.ui.activity.SettingsActivity;
import org.liuyichen.fifteenyan.ui.adapter.NavigationAdapter;
import org.liuyichen.fifteenyan.databinding.ActivityStoryBinding;
import org.liuyichen.fifteenyan.ui.fragment.StoryFragment;
import org.liuyichen.fifteenyan.utils.Toast;

public class StoryActivity extends BaseActivty {

    private ActivityStoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_story);

        if (savedInstanceState == null) {
            setSupportActionBar(binding.toolbar);

            NavigationAdapter adapter = new NavigationAdapter(getSupportFragmentManager());
            binding.viewpager.setAdapter(adapter);

            binding.tabs.setTabGravity(TabLayout.GRAVITY_CENTER);
            binding.tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
            binding.tabs.setupWithViewPager(binding.viewpager);
            binding.tabs.setTabsFromPagerAdapter(adapter);

            for (StoryFragment storyFragment: adapter.getStoryFragments()) {
                binding.appbar.addOnOffsetChangedListener(storyFragment);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
        Toast.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            SettingsActivity.launch(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
