package com.project.batch.repository.step;

import com.project.batch.domain.StepQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRepository extends JpaRepository<StepQueue, Long> , StepRepositoryCustom{

}
