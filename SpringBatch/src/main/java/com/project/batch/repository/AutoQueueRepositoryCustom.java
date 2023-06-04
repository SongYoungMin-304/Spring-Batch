package com.project.batch.repository;

import com.project.batch.domain.AutoQueue;
import com.querydsl.jpa.impl.JPAQuery;

public interface AutoQueueRepositoryCustom {

    JPAQuery<AutoQueue> getAutoQueue(Long minSeq, Long maxSeq, String pollKey, String serverId);

}
