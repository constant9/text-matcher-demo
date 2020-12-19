package com.demo.termatcher.services;

import com.demo.termatcher.model.Chunk;
import com.demo.termatcher.model.StageCompletedEvent;
import com.demo.termatcher.workers.RegexTextMatcher;
import com.demo.termatcher.model.WordMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component
public class MatcherService {
	private static final Logger logger = LoggerFactory.getLogger(MatcherService.class);

	private ExecutorService executorService;

	@Value("${matcher.service.match.terms}")
	private List<String> terms;

	@Autowired
	private BlockingQueue<Chunk> textChunksQueue;

	@Autowired
	private Consumer<WordMatch> wordMatchConsumer;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	Thread queueConsumerThread;

	public MatcherService(@Value("${workers.splitter.number}") int workers){
		executorService = Executors.newFixedThreadPool(workers);
		logger.info("initialized {} splitter workers", workers);
	}

	@PostConstruct
	public void init(){
		queueConsumerThread = new Thread( () -> {
			while (true) {
				try {
					Chunk chunk = textChunksQueue.take();
					boolean isPoisonPill = chunk.getLineOffset() == -1;
					if (isPoisonPill) {
						logger.info("shutting down splitter workers");
						executorService.shutdown();
						executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
						applicationEventPublisher.publishEvent(StageCompletedEvent.MATCHING);
						break;
					} else {
						executorService.execute(new RegexTextMatcher(chunk, terms, wordMatchConsumer));
					}
				} catch (InterruptedException e) {
					logger.error("error", e);
					break;
				}
			}
			logger.info("shutting down consumer queueConsumerThread for splitters");
		});
		//queueConsumerThread.setDaemon(true);
		queueConsumerThread.start();
	}

}