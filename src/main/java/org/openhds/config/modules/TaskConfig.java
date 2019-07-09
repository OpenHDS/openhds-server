package org.openhds.config.modules;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync()
@ComponentScan(basePackages= {"org.openhds.task"})
public class TaskConfig {
	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor task = new ThreadPoolTaskExecutor();
		task.setMaxPoolSize(5);
		task.setQueueCapacity(25);
		return new ThreadPoolTaskExecutor();
	}
}
