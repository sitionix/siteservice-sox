package com.sitionix.stsssox.config;

import com.sitionix.forge.outbox.core.model.EnumForgeOutboxEventTypes;
import com.sitionix.forge.outbox.core.model.ForgeOutboxEventTypes;
import com.sitionix.stsssox.domain.event.SiteMetaEventType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OutboxEventTypeConfiguration {

    @Bean
    public ForgeOutboxEventTypes forgeOutboxEventTypes() {
        return new EnumForgeOutboxEventTypes<>(SiteMetaEventType.class);
    }
}
