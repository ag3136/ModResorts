package com.acme.modres.security;

// The com.sun.net.ssl.* packages were removed in Java 11
// Use javax.net.ssl.* or jakarta.* equivalents instead
// import javax.net.ssl.SSLContext;
// import javax.net.ssl.TrustManager;

import java.util.logging.Logger;

/**
 * SSLUtils class - com.sun.* packages are not available in Java 11+
 * This code has been commented out as it uses internal APIs that were removed.
 * For Java 11+, use standard javax.net.ssl.* or jakarta.* APIs instead.
 */
public class SSLUtils {
  private static final Logger logger = Logger.getLogger(SSLUtils.class.getName());
  
  // The following code uses com.sun.* internal APIs that are not available in Java 11+
  // To fix this, use javax.net.ssl.SSLContext and javax.net.ssl.TrustManager instead
  // private SSLContext getContext() throws Exception {
  //   try {
  //     SSLContext sc = SSLContext.getInstance("SSL");
  //     sc.init(null, // we don't need KeyManager
  //         new TrustManager[]{new FakeX509TrustManager()},
  //         new java.security.SecureRandom());
  //     return sc;
  //   } catch (Exception exc) {
  //     throw new Exception("Some error");
  //   }
  // }
}
