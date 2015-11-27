package org.liuyichen.fifteenyan.ui.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.liuyichen.fifteenyan.App;
import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.databinding.FragmentStoryBinding;
import org.liuyichen.fifteenyan.model.Data;
import org.liuyichen.fifteenyan.model.Story;
import org.liuyichen.fifteenyan.network.Api;
import org.liuyichen.fifteenyan.network.Category;
import org.liuyichen.fifteenyan.provider.StoryProvider;
import org.liuyichen.fifteenyan.ui.adapter.StoryAdapter;
import org.liuyichen.fifteenyan.utils.Toast;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import ollie.Ollie;
import ollie.query.Select;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * By liuyichen on 15-3-3 下午5:03.
 */
public class StoryFragment extends BindFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, PtrHandler
            , AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = "StoryFragment";
    private static final String EXTRA_CATEGORY = "StoryFragment:EXTRA_CATEGORY";

    public static StoryFragment create(Category category) {

        StoryFragment fragment = new StoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = (Category) getArguments().getSerializable(EXTRA_CATEGORY);
    }

    private Category category;

    private StoryAdapter storyAdapter;

    private int offset = 0;

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
        binding.ptrFrame.setPtrHandler(this);
        storyAdapter = new StoryAdapter(getActivity());
        storyAdapter.setOnEndListener(new StoryAdapter.OnEndListener() {
            @Override
            public void OnEnd() {
                load(offset);
            }
        });
        binding.recyclerview.setAdapter(storyAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
        getLoaderManager().destroyLoader(0);
        if (fristSubscription != null && !fristSubscription.isUnsubscribed()) {
            fristSubscription.unsubscribe();
        }
        if (loadSubscription != null && !loadSubscription.isUnsubscribed()) {
            loadSubscription.unsubscribe();
        }
    }

    private void initPtr() {

        MaterialHeader header = new MaterialHeader(getActivity());
        header.setPadding(0, 20, 0, 20);
        int[] colors = new int[]{getResources().getColor(R.color.primary)};
        header.setColorSchemeColors(colors);

        binding.ptrFrame.setHeaderView(header);
        binding.ptrFrame.addPtrUIHandler(header);
    }

    private Subscription fristSubscription;
    private Subscription loadSubscription;

    @Override
    public void onUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            fristSubscription = Select.columns("COUNT(*)")
                    .from(Story.class)
                    .where("category = ?", category.value()).observableValue(Integer.class)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(new Func1<Integer, Boolean>() {
                        @Override
                        public Boolean call(Integer integer) {
                            return integer == 0;
                        }
                    }).subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            binding.ptrFrame.autoRefresh();
                        }
                    });
        }
    }

    private boolean isLoading = false;

    private void load(final int offset) {
        if (isLoading) {
            return;
        }
        isLoading = true;
        loadSubscription = Observable
                .just(offset).flatMap(new Func1<Integer, Observable<Data>>() {
                    @Override
                    public Observable<Data> call(Integer offset) {
                        if (offset == 0) {
                            Story.clear(category.value());
                        }
                        return Api.getStorys(offset, category.value());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<Data, Integer>() {
                    @Override
                    public Integer call(Data data) {

                        SQLiteDatabase db = Ollie.getDatabase();
                        db.beginTransaction();
                        try {
                            for (Story story : data.result) {
                                story.category = category.value();
                                story.saved();
                            }
                            db.setTransactionSuccessful();
                        } finally {
                            db.endTransaction();
                        }
                        return data.result.size();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        isLoading = false;
                        binding.ptrFrame.refreshComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        isLoading = false;
                        binding.ptrFrame.refreshComplete();
                        Toast.makeText(App.getSelf(), R.string.read_fail, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Integer i) {
                        if (i == 0) {
                            storyAdapter.end();
                            Toast.makeText(App.getSelf(), R.string.no_more, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(App.getSelf(), StoryProvider.createUri(Story.class), null,
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
    public void onRefreshBegin(PtrFrameLayout ptr) {
        offset = 0;
        if (loadSubscription != null && !loadSubscription.isUnsubscribed()) {
            loadSubscription.unsubscribe();
        }
        load(offset);
    }

    private int mAppBarOffset = 0;
    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return (mAppBarOffset == 0) && PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mAppBarOffset = verticalOffset;
    }
}
