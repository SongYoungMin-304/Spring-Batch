package com.project.batch.sender;

import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import com.project.batch.model.AutoQueDto;

public abstract class AbstractQueWriter <T extends AutoQueDto>
implements ItemWriter<T>, StepExecutionListener{

}
