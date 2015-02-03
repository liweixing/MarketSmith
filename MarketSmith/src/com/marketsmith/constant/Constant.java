package com.marketsmith.constant;

import android.annotation.SuppressLint;

public class Constant {
  /**
   * network request URL
   */
  public static final String URL = "http://172.17.17.159:8080/msi_services/";
  public static final String LOGIN = URL + "user/login.json"; // 登录
  public static final String INIT = URL + "user/init.pb"; 
  public static final String NODEEXPLORE = URL + "list/1/5/nodeexplore.xml?";// lang=zh-Hans&di=MarketSmithHKUID-0EB20F4B-0909-4256-9D03-E858E1945CA1
  /**
   * network response
   */
  public static final String SESSION_TIME_OUT = "SESSION_TIME_OUT";

  public static final int POST_SUCCESS = 2001;

  public static final int GET_SUCCESS = 2002;

  public static final int DOWN_SUCCESS = 2003;

  public static final int FAIL = 2004;

  public static final int APP_DOWM_FAIl = 2005;

  public static final int FAST_SEND_SUCCESS = 2006;

  public static final int APP_DOWM_LOADING = 2007;

  public final static int MSG_GET_ORDER_LOGISTICS_OK = 10010;

  public final static int MSG_GET_ORDER_LOGISTICS_FAIL = 10011;

  public final static int MSG_FIND_AVAILABLE_CITY_NEW = 10012;

  public final static int MSG_FIND_AVAILABLE_CITY_OLD = 10013;

  public final static int MSG_FIND_AVAILABLE_CITYV2_NEW = 10012;

  public final static int MSG_FIND_AVAILABLE_CITYV2_OLD = 10013;

  public final static int MSG_NET_START = 1001;

  public final static int MSG_NET_ONLOAD = 1002;

  public final static int MSG_NET_SUCCESS = 1003;

  public final static int NET_FAIL = 1004;

  @SuppressLint("SdCardPath")
  public static final String SAVE_APK_PATH = "/mnt/sdcard/hy/hy.apk";

  @SuppressLint("SdCardPath")
  public static final String APP_PATH = "/mnt/sdcard/hy/";
  /**
   * apk name
   */
  public static final String APK_NAME = "MarketSmith.apk";
  /**
   * shared preference for cookie
   */
  public static final String LOGIN_SET = "login_set";
  public static final String COOKIE = "cookie";
  /**
   * shared preference for device id
   */
  public static final String PREFS_FILE = "device_id.xml";
  public static final String PREFS_DEVICE_ID = "device_id";
  public static final String DATATYPE_JSON = "json";
  public static final String DATATYPE_PB = "pb";
}
