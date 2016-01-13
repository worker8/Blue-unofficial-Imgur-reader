package worker8.com.github.imgurdiscovery;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ImgurDiscoveryApplication extends Application {
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            //TODO: check if LeakCanary is functional for Android 6.0, add it to check for memory leak if it's patched.
//            LeakCanary.install(this);
        }
        initImageLoader(getApplicationContext());
    }

    public static void initImageLoader(Context context) {
        // https://github.com/nostra13/Android-Universal-Image-Loader/wiki/Useful-Info

        /* enable cache */
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2)
                //.denyCacheImageMultipleSizesInMemory();
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 MiB
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .defaultDisplayImageOptions(displayImageOptions)
                .tasksProcessingOrder(QueueProcessingType.LIFO);

        if (BuildConfig.DEBUG) {
            config.writeDebugLogs(); // Remove for release app
        }

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
