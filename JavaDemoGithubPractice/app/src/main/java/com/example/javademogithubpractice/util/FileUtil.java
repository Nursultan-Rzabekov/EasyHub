

package com.example.javademogithubpractice.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

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

    public static boolean isExternalStorageEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String encodeBase64File(@NonNull String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

}
