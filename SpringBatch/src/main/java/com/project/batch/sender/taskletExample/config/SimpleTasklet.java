package com.project.batch.sender.taskletExample.config;

import com.project.batch.domain.TaskletQueue;
import com.project.batch.repository.tasklet.TaskletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SimpleTasklet implements Tasklet {

    private TaskletRepository taskletRepository;

    public SimpleTasklet(TaskletRepository taskletRepository){
        this.taskletRepository = taskletRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        List<TaskletQueue> taskletQueues = taskletRepository.getTasklet().fetch();

        taskletQueues.stream().forEach(o -> o.setFlag("Y"));

        return RepeatStatus.FINISHED;
    }
}
