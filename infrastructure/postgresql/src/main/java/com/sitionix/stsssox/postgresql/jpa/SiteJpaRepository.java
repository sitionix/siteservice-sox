package com.sitionix.stsssox.postgresql.jpa;

import com.sitionix.stsssox.postgresql.entity.site.SiteEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteJpaRepository extends JpaRepository<SiteEntity, UUID> {
}
