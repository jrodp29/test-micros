package com.santander.cross.nld.security.domain;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import java.util.Set;

@StaticInitSafe
@ConfigMapping(prefix = "security.authentication.nld")
public interface SecurityKeys {

    @WithName("keys")
    Set<Key> keys();
}

