package com.sitionix.stsssox.it.infra;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "forge_outbox_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MongoOutboxEventEntity {

    @Id
    private String id;

    private String eventType;
    private String payload;
    private String status;
    private Integer attempts;
    private Instant nextAttemptAt;
    private String lastError;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lockUntil;
}
