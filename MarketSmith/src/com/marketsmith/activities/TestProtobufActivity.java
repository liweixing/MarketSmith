/*
 * Project:        MarketSmith
 * File Name:      TestProtobufActivity.java
 * Class Name:     TestProtobufActivity
 */
package com.marketsmith.activities;

import java.lang.reflect.Type;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.InvalidProtocolBufferException;
import com.marketsmith.constant.Constant;
import com.marketsmith.net.CustomJsonRequest;
import com.marketsmith.net.ResponseCallBack;
import com.marketsmith.services.model.protobuf.ProtoBufData.InitResponseData;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Class Name: TestProtobufActivity Description: TODO
 * 
 * @author Wendy
 * 
 */
public class TestProtobufActivity extends BaseRoboActivity {
  private static final String TAG = "TestProtobufActivity";
  private Context mContext;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = this;
    initRequest(Constant.INIT, "");

  }

  private void initRequest(String url, String requestBody) {
    url = "http://172.17.17.159:8080/msi_services/user/init.pb?lang=en&di=MarketSmithHKUID-BA59ADC3-FF94-4029-B20E-40B76745AC04&mtype=3";
    // url="http://172.17.17.159:8080/msi_services/list/1/5/nodeexplore.pb?lang=zh-Hans&di=MarketSmithHKUID-0EB20F4B-0909-4256-9D03-E858E1945CA1";
    Type type = new TypeToken<String>() {
    }.getType();
    // 9th parameter: isLogin
    CustomJsonRequest<String> request = new CustomJsonRequest<String>(mContext, Method.GET, url,
        null, Constant.DATATYPE_PB, null, null, type, false, new Listener<String>() {

          @Override
          public void onResponse(String arg0) {
            Log.i(TAG, "<initRequest> -- <onResponse>");
          }

        }, errorListener/*new ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError arg0) {
            Log.i(TAG, "<TestActivity> -- <onErrorResponse>");
          }
        }*/);
    requestQueue.add(request); 
    request.setCallBack(new ResponseCallBack() {
      @Override
      public void parse(byte[] data) {

        InitResponseData initResponseData = null;
        try {
          initResponseData = InitResponseData.parseFrom(data);
          Log.i(TAG, "<initRequest> -- initResponseData = " + initResponseData);
        } catch (InvalidProtocolBufferException e) {
          Log.i(TAG, "<initRequest> -- InvalidProtocolBufferException");
        }
      }
    });
  }


}
