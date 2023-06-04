package com.project.batch.config;

import com.project.batch.domain.AutoQueue;
import com.project.batch.repository.AutoQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final AutoQueueRepository autoQueueRepository;

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.데이터10000건();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void 데이터10000건(){

            for(int a = 0; a < 1; a++){
                AutoQueue autoQueue = new AutoQueue();
                autoQueue.setQueueName("queue네임");
                autoQueue.setFlag("N");
                autoQueue.setTemplateMsgId("ABCD");

                em.persist(autoQueue);
            }
        }

    }
}
