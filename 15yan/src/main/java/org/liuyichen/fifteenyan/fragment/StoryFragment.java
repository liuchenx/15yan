package org.liuyichen.fifteenyan.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.adapter.StoryAdapter;
import org.liuyichen.fifteenyan.api.Api;
import org.liuyichen.fifteenyan.api.Category;
import org.liuyichen.fifteenyan.databinding.FragmentStoryBinding;
import org.liuyichen.fifteenyan.model.Data;
import org.liuyichen.fifteenyan.model.Story;
import org.liuyichen.fifteenyan.provider.StoryProvider;
import org.liuyichen.fifteenyan.utils.Toast;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import ollie.query.Select;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.view.View.OVER_SCROLL_NEVER;

/**
 * By liuyichen on 15-3-3 下午5:03.
 */
public class StoryFragment extends BindFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, PtrHandler, Callback<Data> {

    private static final String EXTRA_CATEGORY = "StoryFragment:EXTRA_CATEGORY";

    public static StoryFragment create(Category category) {

        StoryFragment fragment = new StoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        category = (Category) getArguments().getSerializable(EXTRA_CATEGORY);
    }

    private Category category;

    private StoryAdapter storyAdapter;

    private int offset = 0;

    private boolean loading = false;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    private FragmentStoryBinding binding;

    @Override
    protected View onBindView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_story, container, false);
        initViews();

        return binding.getRoot();
    }

    protected void initViews() {
        getLoaderManager().initLoader(0, null, this);

        initPtr();
        initRecyclerview();
        binding.ptrFrame.setPtrHandler(this);
        storyAdapter = new StoryAdapter(getActivity());
        binding.recyclerview.setAdapter(storyAdapter);
        binding.recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                if (!loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        load(offset);
                    }
                }
            }
        });
    }

    private void initPtr() {

        MaterialHeader header = new MaterialHeader(getActivity());
        header.setPadding(0, 20, 0, 20);
        int[] colors = new int[]{getResources().getColor(R.color.primary)};
        header.setColorSchemeColors(colors);

        binding.ptrFrame.setInterceptEventWhileWorking(true);
        binding.ptrFrame.setDurationToCloseHeader(1500);
        binding.ptrFrame.setHeaderView(header);
        binding.ptrFrame.addPtrUIHandler(header);
    }

    private void initRecyclerview() {

        if (Build.VERSION.SDK_INT >= 9) {
            binding.recyclerview.setOverScrollMode(OVER_SCROLL_NEVER);
        } else {
            binding.recyclerview.setFadingEdgeLength(0);
        }

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void onUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Integer count = Select.columns("COUNT(*)")
                    .from(Story.class)
                    .where("category = ?", category.value())
                    .fetchValue(Integer.class);
            if (count == 0) {
                binding.ptrFrame.autoRefresh();
            }
        }
    }

    private void load(int offset) {

        loading = true;
        Call<Data> call = Api.getStorys(offset, category.value());
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<Data> response, Retrofit retrofit) {
        binding.ptrFrame.refreshComplete();
        ArrayList<Story> results = response.body().result;
        for (Story story : results) {
            story.category = category.value();
            story.saved();
        }
        loading = false;
    }

    @Override
    public void onFailure(Throwable error) {
        binding.ptrFrame.refreshComplete();
        Toast.makeText(getActivity(), R.string.read_fail, Toast.LENGTH_SHORT).show();
        loading = false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), StoryProvider.createUri(Story.class), null,
                "category = ?", new String[]{category.value()}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        storyAdapter.changeCursor(cursor);
        offset = cursor.getCount();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        storyAdapter.changeCursor(null);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptr) {
        offset = 0;
        Story.clear(category.value());
        load(offset);
    }
}
