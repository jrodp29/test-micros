package com.santander.cross.fra.security.domain;

public interface Key {
    String kid();
    String kty();
    String n();
    String e();
}
