package com.marketsmith.net;

import org.apache.http.conn.ssl.SSLSocketFactory;

import net.tsz.afinal.FinalHttp;

/**
 * 类名: FinalHttpFactory 描述: TODO
 * 
 * @author Wendy
 * 
 */
public class FinalHttpFactory {
  private static FinalHttp intance;
  private static final int TIME_OUT = 1000 * 10;

  public synchronized static FinalHttp getIntance() {
    if (intance != null) {
      return intance;
    }
    intance = new FinalHttp();
    intance.configTimeout(TIME_OUT);
    intance.addHeader("Accept-Encoding", "gzip, deflate");

    intance.configSSLSocketFactory(SSLSocketFactory.getSocketFactory());
    return intance;
  }
}
