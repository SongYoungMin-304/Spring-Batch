package com.project.batch.sender.stepExample.config;

import com.project.batch.domain.StepQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
public class StepItemProcessor implements ItemProcessor<StepQueue, StepQueue> {

    @Override
    public StepQueue process(StepQueue item) throws Exception {

        log.info("item check {}", item.toString());
        Thread.sleep(1000);

        return item;
    }
}
