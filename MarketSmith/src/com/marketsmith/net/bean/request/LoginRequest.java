/*
 * Project:        MarketSmith
 * File Name:      LoginRequest.java
 * Class Name:     LoginRequest
 */
package com.marketsmith.net.bean.request;

/**
 * Class Name: LoginRequest Description: TODO
 * 
 * @author Wendy
 * 
 */
public class LoginRequest /* extends BaseRequest */{
  /**
   * dt=iPhone&di=N/A&uui=MarketSmithHKUID-BA59ADC3-FF94-4029-B20E-40B76745AC04&
   * os=iPhone%20OS8.0.2&v=2.8&pf=iPhone5,2
   */
  // private static String DEVICETYPE = "dt";
  // private static String DEVICEID = "di";
  // private static String UUID = "uui";
  // private static String DEVICEOS = "os";
  // private static String APPVERSION = "v";
  // private static String PHONEMODE = "pf";
  private String deviceType;
  private String deviceId;
  private String uuid;
  private String deviceOS;
  private String appVersion;
  private String phoneMode;

  public LoginRequest(String deviceType, String deviceId, String uuid, String deviceOS, String appVersion,
      String phoneMode) {
    // try {
    // put(DEVICETYPE, deviceType);
    // put(DEVICEID, deviceId);
    // put(UUID, uuid);
    // put(DEVICEOS, deviceOS);
    // put(APPVERSION, appVersion);
    // put(PHONEMODE, phoneMode);
    // } catch (JSONException e) {
    // }
    this.deviceType = deviceType;
    this.deviceId = deviceId;
    this.uuid = uuid;
    this.deviceOS = deviceOS;
    this.appVersion = appVersion;
    this.phoneMode = phoneMode;

  }

  /**
   * Description: TODO
   * 
   */
  public String getRequestBody() {
    String requestBody = "dt=" + deviceType + "&di=" + deviceId + "&uui=" + uuid + "&os=" + deviceOS + "&v="
        + appVersion + "&pf=" + phoneMode;
    requestBody = requestBody.replaceAll(" ", "_");
    return requestBody;
  }
}
