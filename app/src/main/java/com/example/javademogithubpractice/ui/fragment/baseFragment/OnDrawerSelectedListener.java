package com.example.javademogithubpractice.ui.fragment.baseFragment;

import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;



public interface OnDrawerSelectedListener {
    void onDrawerSelected(@NonNull NavigationView navView, @NonNull MenuItem item);
}
