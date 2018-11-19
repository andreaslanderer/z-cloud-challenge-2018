package com.zuehlke.cloudchallenge.messagegransformer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadingConfiguration
{
	@Bean
	public TaskExecutor threadPoolTaskExecutor()
	{
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		executor.setQueueCapacity(200);
		executor.setCorePoolSize(4);
		executor.setMaxPoolSize(16);
		executor.setThreadNamePrefix("default_task_executor_thread");
		executor.initialize();

		return executor;
	}
}
