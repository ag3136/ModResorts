package com.acme.modres.security;

import java.util.logging.Logger;

public class Service {
  private static final Logger logger = Logger.getLogger(Service.class.getName());
  public static final String OPERATION = "my-operation";

  public void operation() {
    // SecurityManager is deprecated for removal in Java 17 and removed in Java 18+
    // Removed SecurityManager usage as it's no longer recommended
    // Modern applications should use alternative security mechanisms like:
    // - Spring Security
    // - Jakarta Security
    // - Custom authorization frameworks
    
    logger.info("Operation is executed");
    System.out.println("Operation is executed");
  }
}
