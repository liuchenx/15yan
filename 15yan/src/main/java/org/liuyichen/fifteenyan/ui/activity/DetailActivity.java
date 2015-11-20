package org.liuyichen.fifteenyan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;


import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.ui.fragment.DetailFragment;
import org.liuyichen.fifteenyan.model.Story;



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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBack.attach(this, Position.LEFT)
                .setContentView(R.layout.activity_detail)
                .setSwipeBackView(R.layout.swipeback_default);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, DetailFragment.create((Story)getIntent().getParcelableExtra(EXTRA_DATA)))
                    .commit();
        }

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }


}
