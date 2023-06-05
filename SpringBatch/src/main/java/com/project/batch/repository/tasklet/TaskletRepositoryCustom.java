package com.project.batch.repository.tasklet;

import com.project.batch.domain.TaskletQueue;
import com.querydsl.jpa.impl.JPAQuery;

public interface TaskletRepositoryCustom {

    JPAQuery<TaskletQueue> getTasklet();
}
