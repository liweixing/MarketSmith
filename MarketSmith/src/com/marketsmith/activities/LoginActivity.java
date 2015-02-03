/*
 * Project:        MarketSmith
 * File Name:      LoginActivity.java
 * Class Name:     LoginActivity
 */
package com.marketsmith.activities;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.InvalidProtocolBufferException;
import com.marketsmith.R;
import com.marketsmith.constant.Constant;
import com.marketsmith.net.CustomJsonRequest;
import com.marketsmith.net.ResponseCallBack;
import com.marketsmith.net.bean.request.LoginRequest;
import com.marketsmith.services.model.protobuf.ProtoBufData.InitResponseData;
import com.marketsmith.utils.NetworkCheck;

/**
 * Class Name: LoginActivity
 * 
 * Description: TODO
 * 
 * @author Wendy
 * 
 */
public class LoginActivity extends BaseRoboActivity {
  private static final String TAG = "LoginActivity";
  private Context mContext;
  private LoginRequest mLoginRequest;
  private String mLoginRequestBodyStr;
  private String mDeviceTypeStr;
  private String mDeviceIdStr;
  private String mUUIDStr;
  private String mDeviceOSStr;
  private String mAppVersionStr;
  /**
   * phone model
   */
  private String mPhoneModeStr;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = this;
    mDeviceTypeStr = "Android";
    mDeviceIdStr = "N/A";
    mUUIDStr = "MarketSmithHKUID-" + getDeviceId();
    mDeviceOSStr = getOsVersionName();
    mAppVersionStr = getAppVersionName();
    mPhoneModeStr = getPhoneModel();
    mLoginRequest = new LoginRequest(mDeviceTypeStr, mDeviceIdStr, mUUIDStr, mDeviceOSStr, mAppVersionStr,
        mPhoneModeStr);
    mLoginRequestBodyStr = mLoginRequest.getRequestBody();
    // mLoginRequestBodyStr = mLoginRequest.toString();
    if (!NetworkCheck.isNetworkAvailable(mContext)) {
      showToast(mContext, mContext.getString(R.string.network_unavailable), Toast.LENGTH_SHORT);
    }
    loginRequest(Constant.LOGIN, mLoginRequestBodyStr);
//    initRequest(Constant.INIT, mLoginRequestBodyStr);
  }

  private void loginRequest(String url, String requestBody) {

    url = url + "?" + requestBody;
    // url =
    // "http://172.17.17.159:8080/msi_services/user/login.json?dt=Android&di=N/A&uui=MarketSmithHKUID-863908029546215&os=android4.1.1&v=1.0&pf=MI2S";
    Type type = new TypeToken<String>() {
    }.getType();
    // 9th parameter: isLogin
    CustomJsonRequest<String> request = new CustomJsonRequest<String>(mContext, Method.GET, url,
        Constant.DATATYPE_JSON, null, null, null, type, true, new Listener<String>() {

          @Override
          public void onResponse(String arg0) {
            Log.i(TAG, "<loginRequest> -- <onResponse>");
            toActivityClearTop(TestProtobufActivity.class);
          }

        }, errorListener);
    requestQueue.add(request); // 添加请求到请求队列

  }

  private void initRequest(String url, String requestBody) {
    url = "http://172.17.17.159:8080/msi_services/user/init.pb?lang=en&di=MarketSmithHKUID-BA59ADC3-FF94-4029-B20E-40B76745AC04&mtype=3";
    // url="http://172.17.17.159:8080/msi_services/list/1/5/nodeexplore.pb?lang=zh-Hans&di=MarketSmithHKUID-0EB20F4B-0909-4256-9D03-E858E1945CA1";
    Type type = new TypeToken<InitResponseData>() {
    }.getType();
    // 9th parameter: isLogin
    CustomJsonRequest<InitResponseData> request = new CustomJsonRequest<InitResponseData>(mContext, Method.GET, url,
        null, Constant.DATATYPE_PB, null, null, type, false, new Listener<InitResponseData>() {

          @Override
          public void onResponse(InitResponseData arg0) {
            Log.i(TAG, "<initRequest> -- <onResponse>");
          }

        }, new ErrorListener() {
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
              showToast(mContext, getString(R.string.network_unavailable), Toast.LENGTH_SHORT);
            } else if (arg0 != null && arg0.toString().contains("com.android.volley.ServerError")) {
              showToast(mContext, getString(R.string.service_temporarily_unavailable), Toast.LENGTH_SHORT);
            } else if (arg0 != null && arg0.toString().contains("com.android.volley.TimeoutError")) {
              showToast(mContext, getString(R.string.time_out_error), Toast.LENGTH_SHORT);
            } else {
              showToast(mContext, getString(R.string.request_fail), Toast.LENGTH_SHORT);
            }
          }
        });

    request.setCallBack(new ResponseCallBack() {
      @Override
      public void parse(byte[] data) {

        InitResponseData initResponseData = null;
        try {
          initResponseData = InitResponseData.parseFrom(data);
          Log.i(TAG, "<parseNetworkResponse> -- initResponseData = " + initResponseData);
        } catch (InvalidProtocolBufferException e) {
          Log.i(TAG, "<parseNetworkResponse> -- InvalidProtocolBufferException");
        }
      }
    });
    
    requestQueue.add(request); 
  }

}
