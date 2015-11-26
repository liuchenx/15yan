package org.liuyichen.fifteenyan.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import org.liuyichen.fifteenyan.App;
import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.databinding.ActivityDetailBinding;
import org.liuyichen.fifteenyan.model.Story;
import org.liuyichen.fifteenyan.ui.fragment.DetailFragment;

import java.util.List;


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
            Glide.with(App.getSelf())
                    .load(story.storyImage)
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(binding.image) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            binding.image.setImageBitmap(resource);

                            new Palette.Builder(resource)
                                    .maximumColorCount(1).generate(new Palette.PaletteAsyncListener() {

                                public void onGenerated(Palette palette) {

                                    List<Palette.Swatch> swatches = palette.getSwatches();

                                    if (!swatches.isEmpty() && swatches.get(0) != null) {
                                        Palette.Swatch swatch = palette.getSwatches().get(0);
                                        setStatusBarColor(swatch.getRgb());
                                        setBackArrowColor(swatch.getBodyTextColor());
                                        binding.collapsingToolbarLayout.setContentScrimColor(swatch.getRgb());
                                        binding.collapsingToolbarLayout.setExpandedTitleColor(swatch.getBodyTextColor());
                                        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(swatch.getBodyTextColor());
                                    }
                                }
                            });
                        }
                    });

            getSupportFragmentManager().beginTransaction().add(R.id.container, DetailFragment.create(story)).commit();
        }
    }

    private void setBackArrowColor(@ColorInt int color) {
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
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
