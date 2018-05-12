package com.sofo.springjwtauth.auth;

/**
 * Created by georges on 5/10/18.
 */
public interface SecurityConstants {
    /**
     * The constant SECRET.
     */
    String SECRET = "sucurityapp";
    /**
     * The constant EXPIRATION_TIME.
     */
    long EXPIRATION_TIME = 180000; // MINUTES
    /**
     * The constant HEADER_STRING.
     */
    String HEADER_STRING = "Token";
    /**
     * The constant SIGN_UP_URL.
     */
    String SIGN_UP_URL = "/api/users";
}
