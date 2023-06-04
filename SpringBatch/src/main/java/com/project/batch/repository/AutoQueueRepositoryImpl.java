package com.project.batch.repository;

import com.project.batch.domain.AutoQueue;
import com.project.batch.domain.QAutoQueue;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional
public class AutoQueueRepositoryImpl implements AutoQueueRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public AutoQueueRepositoryImpl(EntityManager em){
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public JPAQuery<AutoQueue> getAutoQueue(Long minSeq, Long maxSeq, String pollKey, String serverId) {
        return queryFactory.select(QAutoQueue.autoQueue)
                .from(QAutoQueue.autoQueue);
    }
}
