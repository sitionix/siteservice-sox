package com.sitionix.stsssox.it;

import com.sitionix.forge.outbox.core.port.ForgeOutboxWorker;
import com.sitionix.forge.outbox.testkit.postgres.contract.ForgeOutboxPostgresDbContracts;
import com.sitionix.forgeit.core.test.IntegrationTest;
import com.sitionix.stsssox.it.infra.OutboxKafkaContracts;
import com.sitionix.stsssox.it.infra.TestManager;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class OutboxWorkerIT {

    @Autowired
    private ForgeOutboxWorker forgeOutboxWorker;

    @Autowired
    private TestManager testManager;

    @Test
    @DisplayName("given pending outbox event when worker starts then publish site created event")
    void givenPendingOutboxEvent_whenDispatchPendingEvents_thenPublishSiteCreatedEvent() {
        //given
        this.testManager.postgresql()
                .create()
                .to(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT
                        .withJson("forgeOutboxSiteCreatedPending.json"))
                .build();

        //when
        this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .assertPayload()
                .assertMetadata(envelope -> {
                });
    }

    @Test
    @DisplayName("given sent outbox event when worker starts then ignore event")
    void givenSentOutboxEvent_whenDispatchPendingEvents_thenIgnoreEvent() {
        //given
        this.testManager.postgresql()
                .create()
                .to(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT
                        .withJson("forgeOutboxSiteCreatedSent.json"))
                .build();

        //when
        this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .await(Duration.ofSeconds(3))
                .assertNone();
    }

    @Test
    @DisplayName("given failed outbox event when worker starts then publish retry event")
    void givenFailedOutboxEvent_whenDispatchPendingEvents_thenPublishRetryEvent() {
        //given
        this.testManager.postgresql()
                .create()
                .to(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT
                        .withJson("forgeOutboxSiteCreatedFailed.json"))
                .build();

        //when
        this.forgeOutboxWorker.dispatchPendingEvents();

        //then
        this.testManager.kafka()
                .consume(OutboxKafkaContracts.SITE_META_CREATED_EVENT_KAFKA_CONTRACT)
                .assertPayload("outboxSiteCreatedEventFromFailed.json")
                .assertMetadata("outboxSiteCreatedMetadataFromFailed.json");
    }
}
