package org.liuyichen.fifteenyan.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.databinding.FragmentSettingsBinding;
import org.liuyichen.fifteenyan.utils.Settings;



public class SettingsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    protected SwitchCompat switchCompat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings,
                container, false);
        binding.onlyWifiSwitch.setOnCheckedChangeListener(this);
        return binding.getRoot();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Settings.switchOnlyWifiMode(b);
    }
}
