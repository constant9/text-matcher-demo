package com.demo.termatcher.messaging;

import com.demo.termatcher.model.Chunk;
import com.demo.termatcher.model.StageCompletedEvent;
import com.demo.termatcher.model.WordMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FlowController {
	private static final Logger logger = LoggerFactory.getLogger(FlowController.class);

	@Autowired
	ChunkConsumer chunkConsumer;

	@Autowired
	WordMatchConsumer wordMatchConsumer;

	@EventListener
	public void ingestionFinishedEventHandler(StageCompletedEvent event){
		logger.info("'{} stage finished' event received", event);
		switch (event){
			case TEXT_CHUNK_INPUT:
				chunkConsumer.accept(new Chunk(-1,null,-1));
				break;
			case MATCHING:
				wordMatchConsumer.accept(new WordMatch(-1,-1,null));
				break;
			case AGGREGATION:
				logger.info("closing application");
				System.exit(0);
		}
	}
}
