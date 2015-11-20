package org.liuyichen.fifteenyan.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liuchen on 15/8/1.
 * and ...
 */
public abstract class BindFragment extends BaseFragment {

    private boolean isBindView = false;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = onBindView(inflater, container, savedInstanceState);
        if (!isBindView) {
            isBindView = true;
            setUserVisibleHint(getUserVisibleHint());
        }
        return root;
    }

    protected abstract View onBindView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isBindView) {
            onUserVisibleHint(isVisibleToUser);
        }
    }

    public void onUserVisibleHint(boolean isVisibleToUser) {

    }
}
