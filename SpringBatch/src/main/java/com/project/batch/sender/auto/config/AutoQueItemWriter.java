package com.project.batch.sender.auto.config;

import com.project.batch.domain.AutoQueue;
import com.project.batch.repository.AutoQueueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@StepScope
@Component
public class AutoQueItemWriter implements ItemWriter<AutoQueue> {

	private final AutoQueueRepository autoQueueRepository;

	public AutoQueItemWriter(AutoQueueRepository autoQueueRepository) {
		this.autoQueueRepository = autoQueueRepository;
	}

	@Override
	public void write(List<? extends AutoQueue> items) throws Exception {
		// TODO Auto-generated method stub

		log.info("사이즈 체크" + items.size());

		for(AutoQueue autoQueDto : items){
			log.info("테스트"+autoQueDto.toString());
			autoQueueRepository.updateAutoQueue(autoQueDto.getPollKey(), autoQueDto.getQueueId());
		}

		log.info("check Writer");
		
	}
}
