package com.project.batch.repository.auto;

import com.project.batch.domain.AutoQueue;
import com.project.batch.model.AutoQueSchdDto;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface AutoQueueRepositoryCustom {

    JPAQuery<AutoQueue> getAutoQueue(Long minSeq, Long maxSeq, String pollKey, String serverId);

    List<String> findList();

    long updatePreAutoQueue(String pollKey, String templateMsgId);

    List<AutoQueSchdDto> findBySchdByPollKey(String pollKey);
}
