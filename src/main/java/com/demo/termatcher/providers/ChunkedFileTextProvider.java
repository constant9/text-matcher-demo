package com.demo.termatcher.providers;

import com.demo.termatcher.model.Chunk;
import com.demo.termatcher.model.StageCompletedEvent;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.Scanner;
import java.util.function.Consumer;

@Component
public class ChunkedFileTextProvider implements TextProvider {
	private static final Logger logger = LoggerFactory.getLogger(ChunkedFileTextProvider.class);
	private int maxChunkLines;
	private String filePath;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public ChunkedFileTextProvider(
			@Value("${chunked.text.provider.max.lines:1000}") int maxChunkLines,
			@Value("${chunked.text.provider.path}") String filePath) {
		this.maxChunkLines = maxChunkLines;
		this.filePath = filePath;
	}

	@Override
	public void readText(Consumer<Chunk> chunkConsumer) {
		logger.info("Starting scan of file {}", filePath);
		try (FileInputStream inputStream = new FileInputStream(filePath);
			Scanner sc = new Scanner(inputStream, "UTF-8")) {
			int globalLineCounter = 1; //offset start is 1 based
			int chunkCounter = 0;
			while (sc.hasNextLine()) {
				int chunkOffset = globalLineCounter;
				val stringBuilder = new StringBuilder();
				for(int lineCounter=0;lineCounter<maxChunkLines && sc.hasNextLine();lineCounter++, globalLineCounter++){
					String line = sc.nextLine();
					stringBuilder.append(line).append(System.lineSeparator());
				}
				stringBuilder.deleteCharAt(stringBuilder.length()-1);
				val chunk = new Chunk(chunkOffset, stringBuilder.toString(), chunkCounter++);
				logger.info("Built chunk #{} with total {} lines", chunkCounter, globalLineCounter - chunk.getLineOffset());
				chunkConsumer.accept(chunk);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error consuming text from file " + filePath, e);
		}
		applicationEventPublisher.publishEvent(StageCompletedEvent.TEXT_CHUNK_INPUT);
	}
}
