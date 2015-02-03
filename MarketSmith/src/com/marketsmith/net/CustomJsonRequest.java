package com.marketsmith.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.InvalidProtocolBufferException;
import com.marketsmith.constant.Constant;
import com.marketsmith.services.model.protobuf.ProtoBufData;
import com.marketsmith.services.model.protobuf.ProtoBufData.InitResponseData;
import com.marketsmith.services.model.protobuf.ProtoBufData.NodeExplorerResponseData;
import com.marketsmith.utils.JsonUtil;

/*
 * Class Name:      CustomJsonRequest
 * Discription:   TODO
 * @author  Wendy
 *
 */
public class CustomJsonRequest<T> extends JsonRequest<T> {
  public final static String TAG = CustomJsonRequest.class.getSimpleName();
  private Context context = null;
  // public final static int SOCKET_TIMEOUT = 5000;
  private Gson gson;
  private Class<T> clazz;
  private String mKey;
  private Type type;
  private Boolean isLogin;
  private String cookie;
  private String requestBody;
  private SharedPreferences sharedPreferences;
  private String dataType = Constant.DATATYPE_JSON;
  private ResponseCallBack callBack;

  /**
   * GET request
   */
  public CustomJsonRequest(String url, Class<T> clazz, Type type, Boolean isLogin, Listener<T> listener,
      ErrorListener errorListener) {
    this(url, null, clazz, type, isLogin, listener, errorListener);
  }

  /**
   * GET request
   */
  public CustomJsonRequest(String url, String key, Class<T> clazz, Type type, Boolean isLogin, Listener<T> listener,
      ErrorListener errorListener) {
    this(Method.GET, url, null, key, clazz, type, isLogin, listener, errorListener);
  }

  /**
   * 
   * @param method
   *          request method (GET, POST...) Use
   *          {@link com.android.volley.Request.Method}.
   * @param clazz
   *          if key=null，clazz=null
   */
  public CustomJsonRequest(int method, String url, String requestBody, String key, Class<T> clazz, Type type,
      Boolean isLogin, Listener<T> listener, ErrorListener errorListener) {
    super(method, url, requestBody, listener, errorListener);
    Log.i(TAG, url);
    this.clazz = clazz;
    mKey = key;
    this.type = type;
    this.isLogin = isLogin;
    gson = new Gson();
  }

  /**
   * 
   * @param method
   *          request method (GET, POST...) Use
   *          {@link com.android.volley.Request.Method}.
   */
  public CustomJsonRequest(int method, String url, String requestBody, Class<T> clazz, Type type, Boolean isLogin,
      Listener<T> listener, ErrorListener errorListener) {
    super(method, url, requestBody, listener, errorListener);
    this.type = type;
    this.clazz = clazz;
    this.isLogin = isLogin;
    this.requestBody = requestBody;
    gson = new Gson();
  }

  /**
   * @param context
   * @param method
   *          request method (GET, POST...) Use
   *          {@link com.android.volley.Request.Method}.
   * @param dataType
   *          the type of response: Contant.DATATYPE_JSON="json" or
   *          Contant.DATATYPE_PB="pb";
   * @param clazz
   *          if key=null，clazz=null
   */
  public CustomJsonRequest(Context context, int method, String url, String requestBody, String dataType, String key,
      Class<T> clazz, Type type, Boolean isLogin, Listener<T> listener, ErrorListener errorListener) {
    super(method, url/* + "?timestamp=" + new Random() */, requestBody, listener, errorListener);
    this.context = context;
    this.type = type;
    this.clazz = clazz;
    this.isLogin = isLogin;
    this.requestBody = requestBody;
    this.dataType = dataType;
    gson = new Gson();
    if (null != context) {
      sharedPreferences = context.getSharedPreferences(Constant.LOGIN_SET, Context.MODE_PRIVATE);
    }
  }

  // @Override
  // public RetryPolicy getRetryPolicy() {
  // RetryPolicy retryPolicy = new DefaultRetryPolicy(SOCKET_TIMEOUT,
  // DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
  // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
  // return retryPolicy;
  // }
  @Override
  public Map<String, String> getHeaders() throws AuthFailureError {
    Map<String, String> params = new HashMap<String, String>();
    if (Constant.DATATYPE_PB.equals(dataType)) {
      // the type of response is Protocol Buffer
      params.put("dataType", "pb");
      params.put("Content-Type", "application/x-protobuf; application/octet-stream");
    } else {
      // the type of response is Json
      params.put("dataType", "json");
      params.put("Content-Type", "application/json; charset=UTF-8");
    }
    params.put("x-form-id", "FAKEFORM");
    params.put("X-Requested-With", "XMLHttpRequest");
    // params.put("Cache-Control", "no-cache");
    // params.put("data", requestBody);
    if (!isLogin) {
      if (null != sharedPreferences) {
        cookie = sharedPreferences.getString(Constant.COOKIE, "");
        Log.i(TAG, "<getHeaders> -- lwx -- cookie = " + cookie);
      }
      if (!("").equals(cookie)) {
        params.put("Cookie", cookie);
      }
    }
    return params;
  }

  @Override
  protected Response<T> parseNetworkResponse(NetworkResponse response) {
    // parse the network response
    String json = null;
    try {
      json = new String(response.data, "utf-8");
    } catch (UnsupportedEncodingException e) {
    }
    if (Constant.DATATYPE_PB.equals(dataType)) {
      // the type of response is Protocol Buffer
      callBack.parse(response.data);

      Log.i(TAG, "<parseNetworkResponse> -- json = " + json);
      return (Response<T>) Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
    } else {
      // the type of response is json

      try {
//        String json = new String(response.data, "utf-8");
        // InitResponseData initResponseData = null;
        // try {
        // initResponseData = InitResponseData.parseFrom(response.data);
        // Log.i(TAG, "<parseNetworkResponse> -- initResponseData = " +
        // initResponseData);
        // } catch (InvalidProtocolBufferException e) {
        // Log.i(TAG,
        // "<parseNetworkResponse> -- InvalidProtocolBufferException");
        // }
        // try {
        // NodeExplorerResponseData nodeExplorerResponseData =
        // NodeExplorerResponseData.parseFrom(response.data);
        // Log.i(TAG, "<parseNetworkResponse> -- nodeExplorerResponseData = " +
        // nodeExplorerResponseData);
        // } catch (InvalidProtocolBufferException e) {
        // Log.i(TAG,
        // "<parseNetworkResponse> -- InvalidProtocolBufferException");
        // }

        Class responseType = type.getClass();
        if (type.getClass().isAssignableFrom(ProtoBufData.class)) {
          // the class of type is the sub-class of ProtoBufData

        }

        Log.i(TAG, "<parseNetworkResponse> -- json = " + json);
        if (isLogin) {

          // to get cookie from the header
          String STCookie = null; // start with "MS_SESSION_ID="
          if (null != response.apacheHeaders) {
            for (int i = 0; i < response.apacheHeaders.length; i++) {
              String key = response.apacheHeaders[i].getName();
              String value = response.apacheHeaders[i].getValue();
              Log.d(TAG, "<parseNetworkResponse> -- key = " + key + "; value = " + value);
              if (key.equalsIgnoreCase("Set-cookie") && value.startsWith("MS_SESSION_ID=")) {
                STCookie = value;
                String[] cookieArr = STCookie.split(";", -1);
                if (cookieArr.length > 0) {
                  STCookie = cookieArr[0];
                }
                Log.d(TAG, "<parseNetworkResponse> -- 11 -- STCookie = " + STCookie);
              }
            }
            cookie = STCookie;
          }
          if (null != cookie) {
            if (null != sharedPreferences) {
              sharedPreferences.edit().putString(Constant.COOKIE, cookie).commit();
            }
          }
        }

        // if (Constant.DATATYPE_PB.equals(dataType)) {
        // // dataType is Protocol Buffer
        // return (Response<T>) Response.success(type,
        // HttpHeaderParser.parseCacheHeaders(response));
        // } else {
        // dataType is Json
        json = json.replace("/", "-");// if contains "/", it will parse error
        T t = null;
        if (mKey == null) {
          t = JsonUtil.json2Any(json, type);
        } else {
          JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
          t = gson.fromJson(jsonObject.get(mKey), clazz);
        }
        return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
        // }
      } /*catch (UnsupportedEncodingException e) {
        return Response.error(new ParseError(e));
      } */catch (JsonSyntaxException e) {
        return Response.error(new ParseError(e));
      }
    }

  }

  public void setCallBack(ResponseCallBack callBack) {
    this.callBack = callBack;
  }

}