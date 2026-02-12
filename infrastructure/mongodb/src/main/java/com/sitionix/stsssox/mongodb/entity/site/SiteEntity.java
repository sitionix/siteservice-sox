package com.sitionix.stsssox.mongodb.entity.site;

import com.sitionix.stsssox.domain.SiteStatus;
import com.sitionix.stsssox.domain.SiteType;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sites")
@CompoundIndex(name = "idx_sites_user_updated", def = "{'userId': 1, 'updatedAt': -1}")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiteEntity {

    @Id
    private UUID siteId;

    private Long userId;

    private String name;

    private SiteStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    private SiteType type;

    private String description;
}
