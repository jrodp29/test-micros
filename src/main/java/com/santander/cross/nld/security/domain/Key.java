package com.santander.cross.nld.security.domain;

public interface Key {
    String kid();
    String kty();
    String n();
    String e();
}
