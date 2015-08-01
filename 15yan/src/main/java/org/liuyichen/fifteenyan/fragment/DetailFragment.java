package org.liuyichen.fifteenyan.fragment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.api.Api;
import org.liuyichen.fifteenyan.databinding.FragmentDetailBinding;
import org.liuyichen.fifteenyan.model.DetailCache;
import org.liuyichen.fifteenyan.model.Story;
import org.liuyichen.fifteenyan.utils.AssetsUtils;
import org.liuyichen.fifteenyan.utils.Settings;
import org.liuyichen.fifteenyan.utils.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import ollie.query.Select;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

import static android.view.View.OVER_SCROLL_NEVER;

/**
 * By liuyichen on 15-3-3 下午5:04.
 */
public class DetailFragment extends BaseFragment implements Callback<Response> {

    private static final String EXTRA_STORY = "DetailFragment:EXTRA_STORY";

    public static DetailFragment create(Story story) {

        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STORY, story);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mStory = getArguments().getParcelable(EXTRA_STORY);
    }

    private FragmentDetailBinding binding;

    private Story mStory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);

        initWebview();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestData();
    }

    private void initWebview() {
        WebView webView = binding.webview;
        if (Build.VERSION.SDK_INT < 9) {
            webView.setFadingEdgeLength(0);
        } else if (Build.VERSION.SDK_INT < 21) {
            webView.setOverScrollMode(OVER_SCROLL_NEVER);
        }
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (Build.VERSION.SDK_INT >= 11) {
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setEnabled(false);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        //     String cacheDirPath = getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
        //       webView.getSettings().setAppCachePath(cacheDirPath);
        webView.getSettings().setAppCacheEnabled(true);
    }

    private void requestData() {

        DetailCache cache = Select.from(DetailCache.class).where("storyId = ?", mStory.storyId).fetchSingle();
        if (cache != null) {
            binding.webview.loadDataWithBaseURL(null, cache.detail, "text/html", "utf-8", null);
        } else {
            Api.getDetailStory(mStory.storyId, this);
        }
    }

    @Override
    public void success(Response response, Response ignored) {

        TypedInput body = response.getBody();
        StringBuilder out = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(body.in(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String html = fixHtml(out.toString());
        DetailCache cache = new DetailCache();
        cache.storyId = mStory.storyId;
        cache.detail = html;
        cache.save();

        // picture mode
        binding.webview.getSettings().setBlockNetworkImage(!Settings.canLoadImage());
        binding.webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }

    private String fixHtml(String h) {

        Document doc = Jsoup.parse(h);
        Element body = doc.body();
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

        String html = AssetsUtils.loadText(getActivity().getApplication(), "www/template.html");
        if (html != null) {
            html = html.replace("{content}", body.html());
        }

        return html;
    }

    @Override
    public void failure(RetrofitError error) {

        Toast.makeText(getActivity(), R.string.read_fail, Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding.webview != null) {
            binding.webview.destroy();
        }
    }
}
