package org.liuyichen.fifteenyan.fragment;

import android.support.v4.app.Fragment;

import com.squareup.leakcanary.RefWatcher;

import org.liuyichen.fifteenyan.App;

/**
 * By liuyichen on 15-3-3 下午5:04.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
