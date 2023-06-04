package com.project.batch.sender.processor;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;

import com.project.batch.model.AutoQueDto;

public abstract class AbstractQueProcessor <T extends AutoQueDto> implements
ItemProcessor<T, T>, StepExecutionListener{

}
