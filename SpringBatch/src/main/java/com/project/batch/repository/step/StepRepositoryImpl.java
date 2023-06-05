package com.project.batch.repository.step;

import com.project.batch.domain.StepQueue;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.project.batch.domain.QStepQueue.stepQueue;

@Transactional
public class StepRepositoryImpl implements StepRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public StepRepositoryImpl(EntityManager em){
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public JPAQuery<StepQueue> getStepQueue() {
        return queryFactory
                .select(stepQueue)
                .from(stepQueue)
                .where(
                        stepQueue.flag.eq("N")
                );
    }
}
