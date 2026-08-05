package com.acme.modres.security;

/**
 * Service class with SecurityManager usage removed for Java 11 compatibility.
 * SecurityManager is deprecated in Java 11 and removed in later versions.
 */
public class Service {
  public static final String OPERATION = "my-operation";

  public void operation() {
    // SecurityManager is deprecated in Java 11 and should not be used
    // The checkMemberAccess method was removed in Java 11
    // Modern applications should use proper access control mechanisms
    // instead of SecurityManager
    System.out.println("Operation is executed");
  }
}
