package dev.jakhongirmadaminov.glassmorphic_sample

import android.app.Application
import android.graphics.Bitmap
import android.util.LruCache

class App : Application() {

    lateinit var memoryCache: LruCache<String, Bitmap>

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / 1024
            }
        }

    }

    companion object {
        private lateinit var INSTANCE: App
        fun getInstance() = INSTANCE
    }
}