

package com.example.javademogithubpractice.util;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;


public class FileUtil {

    private final static String HTTP_CACHE_DIR_NAME = "http_response";

    @Nullable
    public static File getCacheDir(@NonNull Context context, @NonNull String dirName) {
        File rootDir = context.getExternalCacheDir();
        File cacheFile = new File(rootDir, dirName);
        if (!cacheFile.exists()) {
            cacheFile.mkdir();
        }
        return cacheFile;
    }

    @Nullable
    public static File getHttpImageCacheDir(@NonNull Context context) {
        return getCacheDir(context, HTTP_CACHE_DIR_NAME);
    }
}
