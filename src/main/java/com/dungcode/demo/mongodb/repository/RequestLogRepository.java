package com.dungcode.demo.mongodb.repository;

import com.dungcode.demo.mongodb.model.RequestLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestLogRepository extends MongoRepository<RequestLog, String> {

}
