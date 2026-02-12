package com.sitionix.stsssox.mongodb.mongo;

import com.sitionix.stsssox.mongodb.entity.site.SiteEntity;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SiteMongoRepository extends MongoRepository<SiteEntity, UUID> {
}
