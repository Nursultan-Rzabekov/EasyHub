package com.example.kotlindemogithubproject.common

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.example.kotlindemogithubproject.AppConfig
import com.example.kotlindemogithubproject.R


@GlideModule
class MyAppGlideModel : AppGlideModule() {

    override fun isManifestParsingEnabled(): Boolean {
        return super.isManifestParsingEnabled()
    }

    override fun applyOptions(context: Context?, builder: GlideBuilder?) {
        super.applyOptions(context, builder)
        builder!!.setDiskCache(InternalCacheDiskCacheFactory(context, AppConfig.IMAGE_MAX_CACHE_SIZE))
        val requestOptions = RequestOptions.placeholderOf(R.mipmap.logo)
        builder.setDefaultRequestOptions(requestOptions)
    }

    override fun registerComponents(context: Context?, glide: Glide?, registry: Registry?) {
        super.registerComponents(context, glide, registry)
    }

}
