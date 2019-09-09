package com.example.javademogithubpractice.util;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.example.javademogithubpractice.R;


public class AppOpener {
    public static void shareText(@NonNull Context context, @NonNull String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try{
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_to))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, context.getString(R.string.no_share_clients),Toast.LENGTH_SHORT).show();
        }
    }
}
