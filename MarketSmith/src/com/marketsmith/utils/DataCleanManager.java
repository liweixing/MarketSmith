package com.marketsmith.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * Class Name:		DataCleanManager
 * Description:   Clean internal and external cache, database, sharedPreference, files and some user-definde path 
 * @author  Wendy
 *
 */
public class DataCleanManager {
    // (/data/data/com.xxx.xxx/cache) 
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    // database (/data/data/com.xxx.xxx/databases) 
    public static void cleanDatabases(Context context) {
        String path = ComUtilities.getSDPath() + "/" + context.getPackageName() + "/databases";
        try {
            deleteFile(path);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void deleteFile(String path) throws Exception {
        File file = new File(path);
        deleteFile(file);
    }

    public static void deleteFile(File file) throws Exception {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                // file.delete();
            }
        }
    }

    // SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File(ComUtilities.getSDPath() + context.getPackageName() + "/databases"));
    }

    // clean Database By Name
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    // /data/data/com.xxx.xxx/files 
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    // external cache(/mnt/sdcard/android/data/com.xxx.xxx/cache) 
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    // clean the file in user-defined path 
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    //  clean all data of the app 
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    // only delete the files of a folder, if the param "derectory" is a file, do nothing  
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}
