/*
 * Project:        MarketSmith
 * File Name:      TestWebViewActivity.java
 * Class Name:     TestWebViewActivity
 */
package com.marketsmith.activities;

import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import com.marketsmith.R;

/**
 * Class Name: TestWebViewActivity Description: TODO
 * 
 * @author Wendy
 * 
 */
public class TestWebViewActivity extends BaseRoboActivity {
  private static final String TAG = "TestWebViewActivity";
  private Context mContext;
  @InjectView(R.id.btn_activity_back)
  Button mBtnActivityBack;
  @InjectView(R.id.btn_webview_back)
  Button mBtnWebViewBack;
  @InjectView(R.id.btn_webview_forward)
  Button mBtnWebViewForward;
  @InjectView(R.id.webview)
  WebView mWebView;
  private WebSettings mWebSettings;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_webview);
    initWebView();
    initViews();
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void initWebView() {
    mWebSettings = mWebView.getSettings();
    mWebSettings.setJavaScriptEnabled(true);
    mWebSettings.setDefaultTextEncodingName("utf-8");
    mWebSettings.setSupportZoom(true);
    mWebSettings.setBuiltInZoomControls(true);
    mWebSettings.setLoadsImagesAutomatically(true);
    mWebSettings.setDomStorageEnabled(true);
    mWebView.loadUrl("http://www.baidu.com");
    mWebView.setWebViewClient(new WebViewClient() {

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
      }

    });
  }

  private void initViews() {
    mBtnActivityBack.setOnClickListener(this);
    mBtnWebViewBack.setOnClickListener(this);
    mBtnWebViewForward.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    super.onClick(v);
    switch (v.getId()) {
      case R.id.btn_activity_back:
        toActivity(LoginActivity.class);
        break;

      case R.id.btn_webview_back:
        if (mWebView.canGoBack()) {
          mWebView.goBack();
        }
        break;
      case R.id.btn_webview_forward:
        if (mWebView.canGoForward()) {
          mWebView.goForward();
        }
        break;
      default:
        break;
    }

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mWebView.destroy();
  }

}
