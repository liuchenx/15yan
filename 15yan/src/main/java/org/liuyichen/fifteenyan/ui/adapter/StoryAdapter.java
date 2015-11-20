package org.liuyichen.fifteenyan.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

import org.liuyichen.fifteenyan.BR;
import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.ui.activity.BaseActivty;
import org.liuyichen.fifteenyan.ui.activity.DetailActivity;
import org.liuyichen.fifteenyan.model.Story;
import org.liuyichen.fifteenyan.utils.Settings;


/**
 * By liuyichen on 14-12-12 下午4:36.
 */
public class StoryAdapter extends CursorAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOAD = 1;

    private boolean isEnd = false;

    public interface OnEndListener {

        void OnEnd();
    }

    public StoryAdapter(Context context) {
        super(context, null);
    }

    public class StoryViewHoler extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;
        public StoryViewHoler(View itemView) {
            super(itemView);
        }
        public ViewDataBinding getBinding() {
            return binding;
        }
        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

    }

    public class LoadViewHoler extends RecyclerView.ViewHolder {

        public LoadViewHoler(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (isEnd || position < super.getItemCount()) {
            return TYPE_ITEM;
        }
        return TYPE_LOAD;
    }

    @Override
    public int getItemCount() {

        if (super.getItemCount() == 0 || isEnd) {
            return super.getItemCount();
        }
        return super.getItemCount() + 1;
    }

    public void end() {
        isEnd = true;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOAD) {
            return new LoadViewHoler(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_loading,
                    parent,
                    false));
        }
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.view_item_story,
                parent,
                false);
        StoryViewHoler viewHoler = new StoryViewHoler(binding.getRoot());
        viewHoler.setBinding(binding);
        return viewHoler;
    }

    private OnEndListener onEndListener;
    public void setOnEndListener(OnEndListener l) {

        onEndListener = l;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor, int position) {

        if (getItemViewType(position) == TYPE_LOAD) {
            if (onEndListener != null) {
                onEndListener.OnEnd();
            }
            return ;
        }
        final Story story = Story.loadCursor(cursor);
        final StoryViewHoler vh = (StoryViewHoler) holder;

        vh.getBinding().setVariable(BR.story, story);
        vh.getBinding().executePendingBindings();

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DetailActivity.launch((BaseActivty) mContext, story);

            }
        });
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        if (Settings.canLoadImage()) {
            view.setVisibility(View.VISIBLE);
            int radiu = (int) (view.getContext().getResources().getDimension(R.dimen.item_avatar_size) / 2.0f);
            RoundedTransformationBuilder transformationBuilder = new RoundedTransformationBuilder().cornerRadius(radiu).oval(false);
            Picasso.with(view.getContext()).load(url).fit().transform(transformationBuilder.build()).into(view);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
