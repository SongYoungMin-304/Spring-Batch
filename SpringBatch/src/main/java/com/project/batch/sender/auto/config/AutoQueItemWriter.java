package com.project.batch.sender.auto.config;

import com.project.batch.domain.AutoQueue;
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
public class AutoQueItemWriter implements ItemWriter<AutoQueue> {

	private final EntityManagerFactory emf;
	private final JpaItemWriter<AutoQueue> autoQueueJpaItemWriter;

	public AutoQueItemWriter(EntityManagerFactory emf) {
		this.emf = emf;
		this.autoQueueJpaItemWriter = new JpaItemWriter<>();

		autoQueueJpaItemWriter.setEntityManagerFactory(emf);
	}

	@Override
	public void write(List<? extends AutoQueue> items) throws Exception {
		// TODO Auto-generated method stub

		log.info("사이즈 체크" + items.size());

		for(AutoQueue autoQueDto : items){
			log.info("테스트"+autoQueDto.toString());
			autoQueDto.setFlag("Y");
		}

		// 변경 감지로 인해서 별 처리 필요 X
		//autoQueueJpaItemWriter.write(items);

		log.info("check Writer");
		
	}
}
