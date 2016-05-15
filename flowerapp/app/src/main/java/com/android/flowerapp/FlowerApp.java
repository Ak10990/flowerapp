package com.android.flowerapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Akanksha on 14/5/16.
 */
public class FlowerApp extends Application {

    public static FlowerApp mInstance;
    private static SharedPreferences mPrefs;
    private static Realm realm;

    public static FlowerApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initSharedPreferences();
        initDatabase();
        initLibs();
    }

    private void initSharedPreferences() {
        mPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
    }

    private void initDatabase() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).build();
        realm = Realm.getInstance(realmConfig);
    }

    private void initLibs() {
        String sdCardPathName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FlowerApp/";
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(getApplicationContext()).setBaseDirectoryPath(new File(sdCardPathName))
                .setBaseDirectoryName("Images")
                .setMaxCacheSize(100 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(10 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(5 * ByteConstants.MB)
                .setVersion(1)
                .build();
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(getApplicationContext()).setMainDiskCacheConfig(diskCacheConfig).build();
        Fresco.initialize(getApplicationContext(), imagePipelineConfig);
    }

    public static SharedPreferences getprefs() {
        return mPrefs;
    }

    public static Realm getRealm() {
        return realm;
    }
}