package org.liuyichen.fifteenyan.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.liuyichen.fifteenyan.App;
import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.databinding.FragmentDetailBinding;
import org.liuyichen.fifteenyan.model.DetailCache;
import org.liuyichen.fifteenyan.model.Story;
import org.liuyichen.fifteenyan.network.Api;
import org.liuyichen.fifteenyan.utils.AssetsUtils;
import org.liuyichen.fifteenyan.utils.Settings;
import org.liuyichen.fifteenyan.utils.Toast;

import ollie.query.Select;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * By liuyichen on 15-3-3 下午5:04.
 */
public class DetailFragment extends BaseFragment {

    private static final String TAG = "DetailFragment";

    private static final String EXTRA_STORY = "DetailFragment:EXTRA_STORY";

    public static DetailFragment create(Story story) {

        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STORY, story);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStory = getArguments().getParcelable(EXTRA_STORY);
    }

    private FragmentDetailBinding binding;

    private Story mStory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        initWebview();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestData();
    }

    private void initWebview() {
        WebView webView = binding.webview;
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setEnabled(false);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress < 100 && binding.progress.getVisibility() == ProgressBar.GONE) {
                    binding.progress.setVisibility(View.VISIBLE);
                    binding.webview.setVisibility(View.GONE);
                }
                if (newProgress == 100) {
                    binding.progress.setVisibility(View.GONE);
                    binding.webview.setVisibility(View.VISIBLE);
                }
            }
        });
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
    }


    private Subscription subscription;

    private void requestData() {

        subscription = Select.from(DetailCache.class).where("storyId = ?", mStory.storyId).observableSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<DetailCache, Boolean>() {
                    @Override
                    public Boolean call(DetailCache detailCache) {

                        Boolean b = (detailCache == null);
                        if (!b) {
                            binding.webview.loadDataWithBaseURL(null, detailCache.detail, "text/html", "utf-8", null);
                        }
                        return b;
                    }
                }).observeOn(Schedulers.io())
                .flatMap(new Func1<DetailCache, Observable<String>>() {
                    @Override
                    public Observable<String> call(DetailCache detailCache) {
                        return Api.getDetailStory(mStory.storyId);
                    }
                })
                .map(new Func1<String, String>() {

                    @Override
                    public String call(String s) {
                        return fixHtml(s);
                    }
                })
                .map(new Func1<String, String>() {

                    @Override
                    public String call(String s) {

                        DetailCache cache = new DetailCache();
                        cache.storyId = mStory.storyId;
                        cache.detail = s;
                        cache.save();
                        return s;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(App.getSelf(), R.string.read_fail, Toast.LENGTH_SHORT).show();
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        binding.webview.getSettings().setBlockNetworkImage(!Settings.canLoadImage());
                        binding.webview.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
                    }
                });
    }

    private String fixHtml(String h) {

        Document doc = Jsoup.parse(h);
        Element body = doc.body();
        body.getElementsByClass("post-field").remove();
        body.getElementsByClass("site-nav").remove();
        body.getElementsByClass("nav-side").remove();
        body.getElementsByClass("site-nav-overlay").remove();
        body.getElementsByClass("warn-bar").remove();
        body.getElementsByClass("image-mask-down").remove();
        body.getElementsByClass("post-item-meta").remove();
        body.getElementsByClass("story-cover-reading").remove();
        body.getElementsByClass("post-actions").remove();
        body.getElementsByClass("post-info").remove();
        body.getElementsByClass("post-footer").remove();

        String html = AssetsUtils.loadText(App.getSelf(), "www/template.html");
        if (html != null) {
            html = html.replace("{content}", body.html());
        }

        return html;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        if (binding.webview != null) {
            binding.webview.removeAllViews();
            binding.webview.destroy();
            binding.unbind();
        }
    }
}
