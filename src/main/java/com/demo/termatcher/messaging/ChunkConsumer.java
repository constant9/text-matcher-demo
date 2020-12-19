package com.demo.termatcher.messaging;

import com.demo.termatcher.model.Chunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

@Component
public class ChunkConsumer implements Consumer<Chunk> {
	private static final Logger logger = LoggerFactory.getLogger(ChunkConsumer.class);

	@Autowired
	private BlockingQueue<Chunk> chunksQueue;

	@Override
	public void accept(Chunk chunk) {
		try {
			chunksQueue.put(chunk);
		} catch (InterruptedException e) {
			logger.error("failed to produce chunk #"+ chunk.getChunkId(), e);
		}
	}
}
