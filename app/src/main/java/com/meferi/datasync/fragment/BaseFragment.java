package com.meferi.datasync.fragment;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    String keycode;

    protected String getKeycode() {
        return keycode;
    }

    protected void setKeycode(String keycode) {
        this.keycode = keycode;
    }
}
