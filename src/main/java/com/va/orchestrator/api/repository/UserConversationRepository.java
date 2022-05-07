package com.va.orchestrator.api.repository;

import com.va.orchestrator.api.model.dao.UserConversation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author huerta.jorge at gmail.com
 */
@Repository
public interface UserConversationRepository extends CrudRepository<UserConversation, String> {}
