package com.project.batch.repository.auto;

import com.project.batch.domain.AutoQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoQueueRepository extends JpaRepository<AutoQueue, Long>, AutoQueueRepositoryCustom{
}
