package com.demo.termatcher.messaging;

import com.demo.termatcher.model.WordMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

@Component
public class WordMatchConsumer implements Consumer<WordMatch> {
	private static final Logger logger = LoggerFactory.getLogger(WordMatchConsumer.class);

	@Autowired
	BlockingQueue<WordMatch> wordMatchBlockingQueue;

	@Override
	public void accept(WordMatch wordMatch) {
		try {
			wordMatchBlockingQueue.put(wordMatch);
		} catch (InterruptedException e) {
			logger.error("failed to produce wordMatch "+ wordMatch.toString(), e);
		}
	}
}
