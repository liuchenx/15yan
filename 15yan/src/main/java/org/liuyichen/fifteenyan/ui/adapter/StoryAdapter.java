package org.liuyichen.fifteenyan.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.liuyichen.fifteenyan.App;
import org.liuyichen.fifteenyan.BR;
import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.databinding.ViewItemStoryBinding;
import org.liuyichen.fifteenyan.model.Story;
import org.liuyichen.fifteenyan.ui.activity.BaseActivty;
import org.liuyichen.fifteenyan.ui.activity.DetailActivity;
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
        public StoryViewHoler(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setStory(Story story) {

            binding.setVariable(BR.story, story);
            binding.executePendingBindings();
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
        ViewDataBinding binding = ViewItemStoryBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new StoryViewHoler(binding);
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

        vh.setStory(story);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DetailActivity.launch((BaseActivty) mContext, story);

            }
        });
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(final ImageView view, String url) {
        if (Settings.canLoadImage()) {
            view.setVisibility(View.VISIBLE);
            Glide.with(App.getSelf()).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(view) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(App.getSelf().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    view.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
