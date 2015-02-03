package com.marketsmith.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.widget.Toast;

/**
 * Class Name:		FileUtil
 * Description:		TODO
 * @author 	Wendy
 *
 */
public class FileUtil {
    /**
     * check whether SD card is exsit
     */
    public static boolean checkSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static File getSaveFile(String fileNmae) {
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory().getCanonicalFile() + "/" + fileNmae);
        } catch (IOException e) {}
        return file;
    }

    public static File getSaveFile(String folder, String fileNmae) {
        File file = new File(getSavePath(folder), fileNmae);
        return file;
    }

    public static String getSavePath(String folder) {
        return Environment.getExternalStorageDirectory() + "/" + folder;
    }

    // download apk from server
    public static File getFileFromServer(Context context, String urlPath, ProgressDialog pd, String apkName) throws Exception {
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        // get size of the file
        pd.setMax(conn.getContentLength());
        InputStream is = conn.getInputStream();
        File file = null;
        File apkFile = null;
        if (hasSDCard()) {
            file = new File(getRootFilePath());
            // File file = new File("/storage/sdcard1/");
            if (!file.exists()) {
                file.mkdir();
            }
            apkFile = new File(getRootFilePath() + apkName);
        } else {
            file = new File("/storage/sdcard0/Android/data/com.kmfex.smartfex/");// com.kmfex.smartfex
            apkFile = new File("/storage/sdcard0/Android/data/com.kmfex.smartfex/" + apkName);
            if (!file.exists()) {
                if (file.mkdir()) {
                    // apkFile = new File("/storage/sdcard0/" + apkName);
                } else {
                    file = new File("/storage/sdcard1/Android/data/com.kmfex.smartfex/");
                    apkFile = new File("/storage/sdcard1/Android/data/com.kmfex.smartfex/" + apkName);
                    if (!file.exists()) {
                        if (file.mkdir()) {
                            // apkFile = new File("/storage/sdcard1/" + apkName);
                        } else {
                            file = new File("/storage/sdcard/Android/data/com.kmfex.smartfex/");
                            if (!file.exists()) {
                                // file.mkdir();
                                // fail alert
                                Toast.makeText(context, "Please istall the SD card!", Toast.LENGTH_LONG).show();
                            }
                            apkFile = new File("/storage/sdcard/Android/data/com.kmfex.smartfex/" + apkName);
                        }
                    }
                }
            }
        }
        // Log.i("FileUtils", "<getFileFromServer> -- file = " + file);
        FileOutputStream fos = new FileOutputStream(apkFile);
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int len;
        int total = 0;
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            total += len;
            // current total number of download
            pd.setProgress(total);
        }
        fos.close();
        bis.close();
        is.close();
        return apkFile;
    }

    public static String[] getVolumePaths(Context context) {
        StorageManager mStorageManager;
        Method mMethodGetPaths;
        mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        String[] paths = null;
        try {
            mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }

    public static String getVolumeState(Context context, String volumePath) {
        StorageManager mStorageManager;
        Method mMethodGetState;
        mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        String state = null;
        if (volumePath == null) {
            return null;
        }
        try {
            mMethodGetState = mStorageManager.getClass().getMethod("getVolumeState");
            state = (String) mMethodGetState.invoke(mStorageManager, volumePath);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return state;
    }

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    public static String getRootFilePath() {
        // if (hasSDCard()) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
        // } else {
        // // String dirPath =
        // return Environment.getDataDirectory().getAbsolutePath(); // filePath:
        // // /data/data/
        // }
    }
}
