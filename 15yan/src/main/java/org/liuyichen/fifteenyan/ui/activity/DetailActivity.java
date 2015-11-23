package org.liuyichen.fifteenyan.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.squareup.picasso.Picasso;

import org.liuyichen.fifteenyan.App;
import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.databinding.ActivityDetailBinding;
import org.liuyichen.fifteenyan.model.Story;
import org.liuyichen.fifteenyan.ui.fragment.DetailFragment;


/**
 * Created by root on 15-3-6.
 * and ...
 */
public class DetailActivity extends BaseActivty {

    private static final String EXTRA_DATA = "DetailActivity:EXTRA_DATA";

    public static void launch(BaseActivty activity, Parcelable data) {

        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_DATA, data);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.swipeback_stack_right_in,
                R.anim.swipeback_stack_to_back);
    }

    private ActivityDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBack.attach(this, Position.LEFT)
                .setContentView(R.layout.activity_detail)
                .setSwipeBackView(R.layout.swipeback_default);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        if (savedInstanceState == null) {
            setSupportActionBar(binding.toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            Story story = getIntent().getParcelableExtra(EXTRA_DATA);
            binding.collapsingToolbarLayout.setTitle(story.title);
            Picasso.with(App.getSelf()).load(story.storyImage).into(binding.image);
            getSupportFragmentManager().beginTransaction().add(R.id.container, DetailFragment.create(story)).commit();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }


}
