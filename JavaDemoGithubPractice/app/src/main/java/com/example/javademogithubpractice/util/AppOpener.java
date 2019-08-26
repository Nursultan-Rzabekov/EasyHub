package com.example.javademogithubpractice.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;

import com.example.javademogithubpractice.R;


public class AppOpener {

    public static void openInCustomTabsOrBrowser(@NonNull Context context, @NonNull String url){
        if(StringUtils.isBlank(url)){
            Toast.makeText(context,context.getString(R.string.invalid_url),Toast.LENGTH_SHORT).show();
            return;
        }
//
//        if(!url.contains("//")){
//            url = "http://".concat(url);
//        }

        System.out.println("??!@?@??@?@?@?@@??@?@@? " + url);
        if (PrefUtils.isCustomTabsEnable()) {
            Bitmap backIconBitmap = ViewUtils.getBitmapFromResource(context, R.drawable.ic_arrow_back_title);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);

            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
//                    .setToolbarColor(ViewUtils.getPrimaryColor(context))
//                    .setCloseButtonIcon(backIconBitmap)
//                    .setShowTitle(true)
                    .build();
            //customTabsIntent.intent.setPackage(customTabsPackageName);
//            customTabsIntent.launchUrl(context, Uri.parse(url));

//            if(PrefUtils.isCustomTabsTipsEnable()){
//                Toast.makeText(context,context.getString(R.string.use_custom_tabs_tips),Toast.LENGTH_SHORT).show();
//                PrefUtils.set(PrefUtils.CUSTOM_TABS_TIPS_ENABLE, false);
//            }

        } else {
            Toast.makeText(context,"Error loading",Toast.LENGTH_SHORT).show();
        }

    }
}



