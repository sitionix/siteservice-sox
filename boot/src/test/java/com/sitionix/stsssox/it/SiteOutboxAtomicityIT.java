package com.sitionix.stsssox.it;

import com.sitionix.forge.outbox.core.port.ForgeOutbox;
import com.sitionix.forge.outbox.core.port.ForgeOutboxPayload;
import com.sitionix.forge.outbox.testkit.postgres.contract.ForgeOutboxPostgresDbContracts;
import com.sitionix.forgeit.core.test.IntegrationTest;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.it.infra.ControllerEndpoint;
import com.sitionix.stsssox.it.infra.DatabaseContract;
import com.sitionix.stsssox.it.infra.TestManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
class SiteOutboxAtomicityIT {

    @Autowired
    private TestManager testManager;

    @MockBean
    private ForgeOutbox<ForgeOutboxPayload> forgeOutbox;

    @Test
    @DisplayName("given outbox enqueue fails when create site then rollback site and outbox state")
    void givenOutboxEnqueueFails_whenCreateSite_thenRollbackSiteAndOutboxState() {
        //given
        doThrow(new IllegalStateException("Outbox unavailable"))
                .when(this.forgeOutbox)
                .send(any(SiteCreatedPayload.class));

        //when
        assertThatThrownBy(() -> this.testManager.mockMvc()
                .ping(ControllerEndpoint.createSite())
                .assertDefault())
                .hasRootCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("Outbox unavailable");

        //then
        this.testManager.postgresql()
                .assertEntities(DatabaseContract.SITE_ENTITY_DB_CONTRACT)
                .hasSize(0);
        this.testManager.postgresql()
                .assertEntities(ForgeOutboxPostgresDbContracts.FORGE_OUTBOX_EVENT_ENTITY_DB_CONTRACT)
                .hasSize(0);
    }
}
