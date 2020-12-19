package com.demo;

import com.demo.termatcher.messaging.ChunkConsumer;
import com.demo.termatcher.model.Chunk;
import com.demo.termatcher.model.WordMatch;
import com.demo.termatcher.providers.TextProvider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@SpringBootApplication
@Slf4j
//@Profile("!test")
public class DemoApplication {
private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Bean
	@Profile("!test")
	public CommandLineRunner commandLineRunner(ApplicationContext ctx)
	{
		return args ->
		{
			logger.info("starting demo app");
			TextProvider textProvider = ctx.getBean(TextProvider.class);
			ChunkConsumer chunkConsumer = ctx.getBean(ChunkConsumer.class);
			textProvider.readText(chunkConsumer);
		};
	}

/*	@EventListener(ApplicationReadyEvent.class)
	public void startTextReader(ApplicationReadyEvent readyEvent) {
		ConfigurableApplicationContext ctx = readyEvent.getApplicationContext();
		logger.info("starting demo app");
		TextProvider textProvider = ctx.getBean(TextProvider.class);
		ChunkConsumer chunkConsumer = ctx.getBean(ChunkConsumer.class);
		textProvider.readText(chunkConsumer);
	}*/

	@Bean
	public BlockingQueue<Chunk> textChunksQueue(){
		return new LinkedBlockingQueue<>();
	}

	@Bean
	public BlockingQueue<WordMatch> wordMatchQueue(){
		return new LinkedBlockingQueue<>();
	}
}
