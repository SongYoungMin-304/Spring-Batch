package com.project.batch.repository.tasklet;

import com.project.batch.domain.TaskletQueue;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.project.batch.domain.QTaskletQueue.taskletQueue;

@Transactional
public class TaskletRepositoryImpl implements TaskletRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public TaskletRepositoryImpl(EntityManager em){
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public JPAQuery<TaskletQueue> getTasklet() {
        return queryFactory
                .select(taskletQueue)
                .from(taskletQueue)
                .where(
                        taskletQueue.flag.eq("N")
                );
    }
}
