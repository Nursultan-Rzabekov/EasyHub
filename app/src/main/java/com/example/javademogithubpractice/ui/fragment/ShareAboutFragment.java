package com.example.javademogithubpractice.ui.fragment;



import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.javademogithubpractice.AppData;
import com.example.javademogithubpractice.util.AppOpener;

public class ShareAboutFragment extends Fragment {

    public enum Type{
        Share,About
    }

    public static ShareAboutFragment create(@NonNull ShareAboutFragment.Type type) {
        ShareAboutFragment fragment = new ShareAboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type.toString());
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = this.getArguments().getString("type");

        if(type.equals(Type.Share.toString())){
            AppOpener.shareText(getContext(), AppData.INSTANCE.getLoggedUser().getHtmlUrl());
        }
        else {
            Toast.makeText(getContext(),"About",Toast.LENGTH_SHORT).show();
        }

    }
}
