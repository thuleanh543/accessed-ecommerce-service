package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface PermissionRepository extends MongoRepository<Permission, String> {}
