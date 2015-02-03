package com.marketsmith.net;

import java.io.File;
import java.io.UnsupportedEncodingException;

import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.inject.PrivateBinder;
import com.marketsmith.constant.Constant;

/**
 * 类名: CommAction 描述: TODO
 * 
 * @author Wendy
 * 
 */
public abstract class CommAction {
  private String TAG = "CommAction";
  private String contentType;

  private String action;

  private String json;

  private StringEntity entity;

  private Object object;

  HttpCallBack callBack;

  public CommAction(Context context, Object object) {
    this.object = object;
    setJson();
    setEntity();
  }

  public void sendPost() {
    Log.e(TAG, json);
    FinalHttpFactory.getIntance().post(Constant.URL + action, entity, contentType, new AjaxCallBack<Object>() {
      @Override
      public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
      }

      @Override
      public void onSuccess(Object t) {
        // TODO Auto-generated method stub
        super.onSuccess(t);
        if (!t.toString().equals(null)) {
          parse(t.toString());
          Log.e(TAG, t.toString());
        }
      }

      @Override
      public void onLoading(long count, long current) {
        super.onLoading(count, current);
      }

      @Override
      public void onFailure(Throwable t, int errorNo, String strMsg) {
        super.onFailure(t, errorNo, strMsg);
        Log.e(TAG, "fail");
        callBack.httpstate(Constant.NET_FAIL);
      }
    });
  }

  public void sendGet() {
    FinalHttpFactory.getIntance().get(Constant.URL + action, null, new AjaxCallBack<Object>() {
      @Override
      public void onSuccess(Object t) {
        super.onSuccess(t);
        if (!t.toString().equals(null)) {
          Log.i(TAG, t.toString());
          parse(t.toString());
          callBack.httpstate(Constant.GET_SUCCESS);
        }
      }

      @Override
      public void onFailure(Throwable t, int errorNo, String strMsg) {
        super.onFailure(t, errorNo, strMsg);
        callBack.httpstate(Constant.FAIL);
      }
    });
  }

  public void sendDownLoad() {
    Log.e(TAG, Constant.URL + action);
    FinalHttpFactory.getIntance().download(Constant.URL + action, Constant.SAVE_APK_PATH, true,
        new AjaxCallBack<File>() {
          @Override
          public void onLoading(long count, long current) {
            super.onLoading(count, current);
            callBack.httpstate(Constant.APP_DOWM_LOADING);
            Log.e(TAG, current + "/" + count);
          }

          @Override
          public void onSuccess(File t) {
            super.onSuccess(t);
            callBack.httpstate(Constant.DOWN_SUCCESS);
          }

          @Override
          public void onFailure(Throwable t, int errorNo, String strMsg) {
            super.onFailure(t, errorNo, strMsg);
            callBack.httpstate(Constant.APP_DOWM_FAIl);
          }
        });
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  private void setJson() {
    Gson gson = new Gson();
    this.json = gson.toJson(object);
  }

  private void setEntity() {
    try {
      this.entity = new StringEntity(json, HTTP.UTF_8);
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void parse(String string) {
  }

  public HttpCallBack getCallBack() {
    return callBack;
  }

  public void setCallBack(HttpCallBack callBack) {
    this.callBack = callBack;
  }
}
