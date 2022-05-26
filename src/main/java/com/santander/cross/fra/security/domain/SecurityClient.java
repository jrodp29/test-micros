package com.santander.cross.fra.security.domain;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "security.client.authentication.fra")
public interface SecurityClient {

    String locale();
    String apiconsumerid();
    String apiconsumersecret();
}
