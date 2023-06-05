package com.project.batch.sender.stepExample.config;

import com.project.batch.domain.StepQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Slf4j
@StepScope
@Component
public class StepItemWriter implements ItemWriter<StepQueue> {

    private final EntityManagerFactory emf;
    private final JpaItemWriter<StepQueue> stepQueueJpaItemWriter;

    public StepItemWriter(EntityManagerFactory emf) {
        this.emf = emf;
        this.stepQueueJpaItemWriter = new JpaItemWriter<>();

        stepQueueJpaItemWriter.setEntityManagerFactory(emf);
    }

    @Override
    public void write(List<? extends StepQueue> items) throws Exception {

        for (StepQueue item : items) {
            item.setFlag("Y");
        }
        // 변경 감지

        // 변경 감지 없이?
        stepQueueJpaItemWriter.write(items);
    }
}
