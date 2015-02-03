package com.marketsmith.utils;

import java.io.File;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import com.marketsmith.R;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Class Name: ComUtilities 
 * Description: TODO
 * @author Wendy
 */
public class ComUtilities {
  private static Toast mToast;
  private static long lastClickTime;
  private static String TAG = "ComUtilities";

  public static String MD5(String str) {
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
    char[] charArray = str.toCharArray();
    byte[] byteArray = new byte[charArray.length];
    for (int i = 0; i < charArray.length; i++) {
      byteArray[i] = (byte) charArray[i];
    }
    byte[] md5Bytes = md5.digest(byteArray);
    StringBuffer hexValue = new StringBuffer();
    for (int i = 0; i < md5Bytes.length; i++) {
      int val = ((int) md5Bytes[i]) & 0xff;
      if (val < 16) {
        hexValue.append("0");
      }
      hexValue.append(Integer.toHexString(val));
    }
    return hexValue.toString();
  }

  public static boolean isStringEmpty(String str) {
    boolean isEmpty = false;
    if (null == str || str.trim().equals("") || "null".equals(str)) {
      isEmpty = true;
    }
    return isEmpty;
  }

  /**
   * install apk
   * 
   * @param url
   */
  public static void installApk(Context mContext, String saveFileName) {
    File apkfile = new File(saveFileName);
    if (!apkfile.exists()) {
      return;
    }
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
    mContext.startActivity(i);
  }

  /**
   * if the Sdcard is exist
   */
  public static boolean hasSdcard() {
    String status = Environment.getExternalStorageState();
    if (status.equals(Environment.MEDIA_MOUNTED)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * create path
   */
  public static void createPath(String path) {
    File file = new File(path);
    if (!file.exists()) {
      file.mkdir();
    }
  }

  public static void deletefile(String fileName) {
    File f = new File(fileName);
    if (f.exists()) {
      f.delete();
    }
  }

  public static boolean notEmpty(Object o) {
    return o != null && !"".equals(o.toString().trim()) && !"null".equalsIgnoreCase(o.toString().trim())
        && !"undefined".equalsIgnoreCase(o.toString().trim());
  }

  public static boolean empty(Object o) {
    return o == null || "".equals(o.toString().trim()) || "null".equalsIgnoreCase(o.toString().trim())
        || "undefined".equalsIgnoreCase(o.toString().trim());
  }

  public static String readFromAsset(String fileName, Context context) {
    String res = "";
    try {
      InputStream in = context.getResources().getAssets().open(fileName);
      int length = in.available();
      byte[] buffer = new byte[length];
      in.read(buffer);
      res = EncodingUtils.getString(buffer, "UTF-8");
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }

  /**
   * to judge if the GPS or AGPS is open.
   * 
   * @param context
   * @return true: means open
   */
  public static void isGPSOpen(Context context) {
    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    if (!gps) {
      openGPS(context);
    }
  }

  /**
   * open gps
   * 
   * @param context
   */
  public static final void openGPS(Context context) {
    Intent GPSIntent = new Intent();
    GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
    GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
    GPSIntent.setData(Uri.parse("custom:3"));
    try {
      PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
    } catch (CanceledException e) {
      e.printStackTrace();
    }
  }

  public static boolean isChineseChar(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
        || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
        || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
      return true;
    }
    return false;
  }

  public static void setTextWatcher(final Context context, EditText editText) {
    final String pwdNoSpace = context.getString(R.string.pwd_no_space);
    final String pwdNoChinese = context.getString(R.string.pwd_no_chinese);
    TextWatcher mTextWatcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
      }

      @Override
      public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
        if (s.length() > 0) {
          int pos = s.length() - 1;
          char c = s.charAt(pos);
          if (c == ' ') {
            // not allow space
            s.delete(pos, pos + 1);
            Toast.makeText(context, pwdNoSpace, Toast.LENGTH_SHORT).show();
          } else if (isChineseChar(c)) {
            s.delete(pos, pos + 1);
            Toast.makeText(context, pwdNoChinese, Toast.LENGTH_SHORT).show();
          }
        }
      }
    };
    editText.addTextChangedListener(mTextWatcher);
  }

  public static boolean isMobileNum(String number) {
    Pattern p = Pattern.compile("^((1[358]))\\d{9}$");
    Matcher m = p.matcher(number);
    return m.matches();
  }

  public static boolean isFastDoubleClick() {
    long time = System.currentTimeMillis();
    long timeD = time - lastClickTime;
    if (0 < timeD && timeD < 500) {
      return true;
    }
    lastClickTime = time;
    return false;
  }

  /**
   * get version of current application
   */
  public static String getVersion(Context context) throws Exception {
    try {
      PackageManager manager = context.getPackageManager();
      PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
      String version = info.versionName;
      return version;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  // check the icon, application name, package name of non-system applications
  public void getPackageInfo(Context context) {
    PackageManager pManager = context.getPackageManager();
    List<PackageInfo> appList = getAllApps(context);
    for (int i = 0; i < appList.size(); i++) {
      PackageInfo pinfo = appList.get(i);
      // get Icon
      // shareItem.setIcon(pManager.getApplicationIcon(pinfo.applicationInfo));
      // set Application Name
      Log.i(TAG, "<getPackageInfo> -- Application Name: "
          + pManager.getApplicationLabel(pinfo.applicationInfo).toString());
      // shareItem.setLabel(pManager.getApplicationLabel(pinfo.applicationInfo).toString());
      // set Package Name
      // shareItem.setPackageName(pinfo.applicationInfo.packageName);
      Log.i(TAG, "<getPackageInfo> -- Package Name: " + pinfo.applicationInfo.packageName);
    }
  }

  // Return the packageinfo list of non-system application 
  public List<PackageInfo> getAllApps(Context context) {
    List<PackageInfo> apps = new ArrayList<PackageInfo>();
    PackageManager pManager = context.getPackageManager();
    // get all applications of the phone, including system applications and non-system applications
    List<PackageInfo> paklist = pManager.getInstalledPackages(0);
    for (int i = 0; i < paklist.size(); i++) {
      PackageInfo pak = (PackageInfo) paklist.get(i);
      // to judge if the application is non-system application 
      if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
        // customs applications
        apps.add(pak);
      }
    }
    return apps;
  }

  /**
   * get sdCard path
   */
  public static String getSDPath() {
    File sdDir = null;
    boolean sdCardExist = android.os.Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()); // 判断sd卡是否存在
    if (sdCardExist) {
      sdDir = Environment.getExternalStorageDirectory();
    } else {
      return "/data/data/";
    }
    return sdDir.toString();
  }

  /**
   * to judge if sd is exist
   */
  public static boolean hasSDCard() {
    String status = Environment.getExternalStorageState();
    if (!Environment.MEDIA_MOUNTED.equals(status)) {
      return false;
    }
    return true;
  }
}
