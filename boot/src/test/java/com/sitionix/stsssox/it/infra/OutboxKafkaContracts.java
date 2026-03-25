package com.sitionix.stsssox.it.infra;

import com.app_afesox.events.Metadata;
import com.app_afesox.stsssox.events.kafka.AvroRecordDeserializer;
import com.app_afesox.stsssox.events.sitemeta.SiteCreatedEvent;
import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.sitionix.forgeit.kafka.api.KafkaContract;

public class OutboxKafkaContracts {

    public static final KafkaContract<SiteMetaEnvelope> SITE_META_CREATED_EVENT_KAFKA_CONTRACT =
            KafkaContract.consumerContract()
                    .topic("stsssox.it.site-meta.public.v1")
                    .groupId("forge-it-group")
                    .payloadDeserializer(AvroRecordDeserializer.class)
                    .defaultEnvelope(SiteMetaEnvelope.class)
                    .defaultExpectedPayload(SiteCreatedEvent.class, "defaultSiteCreatedEvent.json")
                    .defaultMetadata(Metadata.class, "defaultSiteCreatedMetadata.json")
                    .build();
}
