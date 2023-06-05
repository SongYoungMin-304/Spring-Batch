package com.project.batch.repository.step;

import com.project.batch.domain.StepQueue;
import com.querydsl.jpa.impl.JPAQuery;

public interface StepRepositoryCustom {

    JPAQuery<StepQueue> getStepQueue();
}
