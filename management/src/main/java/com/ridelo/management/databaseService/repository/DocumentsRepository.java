package com.ridelo.management.databaseService.repository;

import com.ridelo.management.entities.Documents;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Repository
public interface DocumentsRepository extends CrudRepository<Documents, UUID> {
}
