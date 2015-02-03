package com.marketsmith.activities;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.marketsmith.CustomerAppl;
import com.marketsmith.R;
import com.marketsmith.constant.Constant;
import com.marketsmith.db.DatabaseHelper;
import com.marketsmith.dialog.CustomProgressDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import roboguice.activity.RoboActivity;

public class BaseRoboActivity extends RoboActivity implements OnClickListener {

  private static String TAG = "BaseActivity";
  private Context context;
  protected SharedPreferences sharedPreferences;
  protected CustomerAppl appl;
  private CustomProgressDialog pd;
  private Toast mToast;
  public RequestQueue requestQueue;
  public DatabaseHelper databaseHelper = null;

  // public CustomerReceiver receiver;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    appl = (CustomerAppl) getApplicationContext();
    appl.activities.add(this);
    // appl.inMain = true;
    context = this;
    requestQueue = Volley.newRequestQueue(this);
    Display mDisplay = getWindowManager().getDefaultDisplay();
    int Width = mDisplay.getWidth();
    int Height = mDisplay.getHeight();
    Log.i(context + "", "Width = " + Width + "; Height = " + Height);
    sharedPreferences = getSharedPreferences(Constant.LOGIN_SET, context.MODE_PRIVATE);
  }

  @Override
  protected void onStop() {
    super.onStop();
    /**
     * cancel all network request befor the Activity is stop. in order to avoid
     * app crash, and avoid wasting CPU, battery and network...
     */
    if (null != requestQueue) {
      requestQueue.cancelAll(this);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // unregisterReceiver(receiver);
    if (databaseHelper != null) {
      // 释放helper
      OpenHelperManager.releaseHelper();
      databaseHelper = null;
    }
  }

  @Override
  public void onClick(View v) {
  }

  public void showToast(Context context, String msg, int duration) {
    if (mToast == null) {
      mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    } else {
      mToast.setText(msg);
    }
    mToast.show();
  }

  public void toActivity(Class<?> cls) {
    Intent intent = new Intent(this, cls);
    startActivity(intent);
    // this.finish();
  }

  public void toActivityClearTop(Class<?> cls) {
    Intent intent = new Intent(this, cls);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
    startActivity(intent);
    finish();
  }

  public String getAppVersionName() {
    try {
      PackageManager manager = this.getPackageManager();
      PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
      // version name of current app
      String versionName = info.versionName;
      // version code of current app
      int versionCode = info.versionCode;
      // package name of current app
      String packageNames = info.packageName;
      Log.i(TAG, "<getAppVersionName> -- versionName = " + versionName);
      return versionName;
    } catch (Exception e) {
      e.printStackTrace();
      return "can_not_find_version_name";
    }
  }

  public int getAppVersionCode() {
    PackageManager manager = this.getPackageManager();
    PackageInfo info;
    try {
      info = manager.getPackageInfo(this.getPackageName(), 0);
      // version code of current app
      int versionCode = info.versionCode;
      Log.i(TAG, "<getAppVersionCode> -- versionCode = " + versionCode);
      return versionCode;
    } catch (NameNotFoundException e) {
      e.printStackTrace();
      return 0;
    }
  }

  public String getOsVersionName() {
    return "android" + android.os.Build.VERSION.RELEASE;
  }

  public String getPhoneModel() {
    return android.os.Build.MODEL;
  }

  public String getDeviceId() {
    String deviceId;
    String androidId;
    String uuid;
    final SharedPreferences prefs = context.getSharedPreferences(Constant.PREFS_FILE, 0);
    // Use the ids previously computed and stored in the prefs file
    deviceId = prefs.getString(Constant.PREFS_DEVICE_ID, null);
    if (null == deviceId) {
      /**
       * need add the below permission in AndroidManifest.xml: <uses-permission
       * android:name="android.permission.READ_PHONE_STATE" />
       */
      deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
      // for some Pad, deviceId is null
      if (null == deviceId || "".equals(deviceId)) {
        androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        if (null != androidId && !"".equals(androidId) && !"9774d56d682e549c".equals(androidId)) {
          deviceId = androidId;
        } else {
          uuid = UUID.randomUUID().toString();
          deviceId = uuid;
        }
      }
    }
    return deviceId;
  }

  @SuppressWarnings("unused")
  public void ShowDialog() {
    Dialog dialog = new Dialog(appl);
  }

  public void showProgressDialog() {
    if (pd != null && pd.isShowing()) {
      pd.dismiss();
      pd = null;
    }
    if (pd == null) {
      pd = CustomProgressDialog.createDialog(this);
      pd.setMessage(getResources().getString(R.string.loading));
      // pd.setCanceledOnTouchOutside(false);
    }
    try {
      pd.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void showProgressDialogNoTouch() {
    if (pd != null && pd.isShowing()) {
      pd.dismiss();
      pd = null;
    }
    if (pd == null) {
      pd = CustomProgressDialog.createDialog(this);
      pd.setMessage(getResources().getString(R.string.loading));
      pd.setCanceledOnTouchOutside(false); // not cancel when touch outside
      // pd.setCancelable(false);// not cancel when click back-key
    }
    try {
      pd.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void closeProgressDialog() {
    if (pd != null && pd.isShowing()) {
      pd.dismiss();
      pd = null;
    }
  }

  // private void registerRece() {
  // receiver = new CustomerReceiver();
  // IntentFilter filter = new IntentFilter();
  // filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
  // registerReceiver(receiver, filter);
  // }
  /**
   * You'll need this in your class to get the helper from the manager once per
   * class.
   */
  public DatabaseHelper getHelper() {
    if (databaseHelper == null) {
      databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
    }
    return databaseHelper;
  }

  /**
   * 数据错误处理
   */
  public ErrorListener errorListener = new ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError arg0) {
      NetworkResponse response = arg0.networkResponse;
      if (response != null) {
        try {
          String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
          Log.i(TAG, "<errorListener>--<onErrorResponse> -- json = " + json);
          Log.i(TAG, "<errorListener>--<onErrorResponse> -- response.data = " + response.data);
          Log.i(TAG, "<errorListener>--<onErrorResponse> -- response.headers = " + response.headers);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      }
      closeProgressDialog();
      if (arg0 != null && arg0.toString().contains("com.android.volley.NoConnectionError")) {
        showToast(BaseRoboActivity.this, getString(R.string.network_unavailable), Toast.LENGTH_SHORT);
      } else if (arg0 != null && arg0.toString().contains("com.android.volley.ServerError")) {
        showToast(BaseRoboActivity.this, getString(R.string.service_temporarily_unavailable), Toast.LENGTH_SHORT);
      } else if (arg0 != null && arg0.toString().contains("com.android.volley.TimeoutError")) {
        showToast(BaseRoboActivity.this, getString(R.string.time_out_error), Toast.LENGTH_SHORT);
      } else {
        showToast(BaseRoboActivity.this, getString(R.string.request_fail), Toast.LENGTH_SHORT);
      }
    }
  };

}
