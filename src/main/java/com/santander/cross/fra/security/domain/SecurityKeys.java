package com.santander.cross.fra.security.domain;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import javax.enterprise.inject.Default;
import java.util.Set;

@StaticInitSafe
@ConfigMapping(prefix = "security.authentication.fra")
public interface SecurityKeys {

    @WithName("keys")
    Set<Key> keys();
}

