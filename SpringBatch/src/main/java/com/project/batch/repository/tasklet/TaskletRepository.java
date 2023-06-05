package com.project.batch.repository.tasklet;

import com.project.batch.domain.TaskletQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskletRepository extends JpaRepository<TaskletQueue, Long>, TaskletRepositoryCustom{

}
