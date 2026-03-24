package com.sitionix.stsssox.application.event;

import com.sitionix.forge.outbox.core.port.ForgeOutbox;
import com.sitionix.forge.outbox.core.port.ForgeOutboxPayload;
import com.sitionix.stsssox.domain.event.Event;
import com.sitionix.stsssox.domain.event.SiteMetaEventPublisher;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteMetaPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class SiteMetaOutboxPublisher implements SiteMetaEventPublisher {

    private final ForgeOutbox<ForgeOutboxPayload> forgeOutbox;

    @Override
    public void publish(final Event<SiteMetaPayload> event) {
        if (isNull(event) || isNull(event.getPayload())) {
            return;
        }
        if (event.getPayload() instanceof final SiteCreatedPayload payload) {
            this.forgeOutbox.send(payload);
            return;
        }
        throw new IllegalArgumentException("Unsupported site meta payload type: "
                + event.getPayload().getClass().getName());
    }
}
