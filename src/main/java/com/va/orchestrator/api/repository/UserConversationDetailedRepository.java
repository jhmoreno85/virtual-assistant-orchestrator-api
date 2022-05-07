package com.va.orchestrator.api.repository;

import com.va.orchestrator.api.model.dao.UserConversationDetailed;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author huerta.jorge at gmail.com
 */
@Repository
public interface UserConversationDetailedRepository
    extends CrudRepository<UserConversationDetailed, Long> {}
