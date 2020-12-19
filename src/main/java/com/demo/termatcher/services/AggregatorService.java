package com.demo.termatcher.services;

import com.demo.termatcher.aggregators.MatchesAggregator;
import com.demo.termatcher.model.StageCompletedEvent;
import com.demo.termatcher.model.WordMatch;
import lombok.val;
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

import static java.lang.String.*;

@Component
public class AggregatorService {
	private static final Logger logger = LoggerFactory.getLogger(AggregatorService.class);

	@Value("${matcher.service.match.terms}")
	List<String> terms;

	@Autowired
	private BlockingQueue<WordMatch> wordMatchesQueue;

	@Autowired
	private MatchesAggregator matchesAggregator;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private ExecutorService executorService;

	Thread queueConsumerThread;

	private final static String MATCH_PATTERN = "[lineOffset=%s, charOffset=%s],";

	public AggregatorService(@Value("${workers.aggregator.number}") int workersNumber) {
		executorService = Executors.newFixedThreadPool(workersNumber);
		logger.info("initialized {} aggregator workersNumber", workersNumber);
	}

	@PostConstruct
	public void init(){
		queueConsumerThread = new Thread( () -> {
			while (true) {
				try {
					val wordMatch = wordMatchesQueue.take();
					boolean isPoisonPill = wordMatch.getLineOffset() == -1;
					if (isPoisonPill) {
						logger.info("shutting down aggregation workers");
						executorService.shutdown();
						executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
						break;
					} else {
						executorService.execute(()-> matchesAggregator.put(wordMatch));
					}
				} catch (InterruptedException e) {
					logger.error("error", e);
					break;
				}
			}

			logger.info("Printing aggregation results");
			terms.forEach(term -> {
				StringBuilder stringBuilder = new StringBuilder(term + " --> [");
				matchesAggregator.get(term)
						.forEach(match -> stringBuilder.append(format(MATCH_PATTERN, match.getLineOffset(), match.getCharOffset())));
				if(stringBuilder.charAt(stringBuilder.length()-1)==',')
					stringBuilder.deleteCharAt(stringBuilder.length()-1);//removing the trailing comma
				stringBuilder.append("]");
				System.out.println(stringBuilder.toString());
			});

			logger.info("shutting down consumer queueConsumerThread for matches");
			logger.info("notifying app");
			applicationEventPublisher.publishEvent(StageCompletedEvent.AGGREGATION);
		});
		//queueConsumerThread.setDaemon(true);
		queueConsumerThread.start();
	}

}