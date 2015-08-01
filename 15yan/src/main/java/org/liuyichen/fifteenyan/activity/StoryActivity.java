package org.liuyichen.fifteenyan.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuItem;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.adapter.NavigationAdapter;
import org.liuyichen.fifteenyan.databinding.ActivityStoryBinding;
import org.liuyichen.fifteenyan.utils.Toast;

public class StoryActivity extends BaseActivty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityStoryBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_story);

        setSupportActionBar(binding.toolbar);

        NavigationAdapter adapter = new NavigationAdapter(this, getSupportFragmentManager());
        binding.viewpager.setAdapter(adapter);

        binding.tabs.setTabGravity(TabLayout.GRAVITY_CENTER);
        binding.tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        binding.tabs.setupWithViewPager(binding.viewpager);
        binding.tabs.setTabsFromPagerAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SettingsActivity.launch(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
