package com.demo.termatcher.workers;

import com.demo.termatcher.model.Chunk;
import com.demo.termatcher.model.WordMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegexTextMatcher implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(RegexTextMatcher.class);

	private Chunk chunk;
	private Pattern findWordPattern; //could be optimised through static field with thread safe (singleton) initializer
	private Consumer<WordMatch> matchConsumer;

	public RegexTextMatcher(Chunk chunk, List<String> matchTerms, Consumer<WordMatch> matchConsumer){
		this.chunk = chunk;
		this.matchConsumer = matchConsumer;
		Set<String> escapedTerms = matchTerms.stream() //escapes for regex
				.map(t -> "\\Q" + t + "\\E").collect(Collectors.toSet());
		findWordPattern = Pattern.compile(String.join("|", escapedTerms));
	}

	@Override
	public void run() {
		try {
			logger.debug("processing chunk #{}", chunk.getChunkId());
			MutableInt lineCounter = new MutableInt(chunk.getLineOffset());
			Pattern.compile("\\R").splitAsStream(chunk.getText())
				.map(line ->  matchWords(line, lineCounter.getAndIncr()))
				.flatMap(Collection::stream)
				.forEach(matchConsumer::accept);
		} catch (Exception e) {
			logger.error("error splitting " + chunk.getChunkId(), e);
		}
	}

	private List<WordMatch> matchWords(String line, int lineOffset){
		Matcher matcher = findWordPattern.matcher(line);
		List<WordMatch> result = new ArrayList<>();
		while(matcher.find()) {
			int offsetStart = matcher.start() + 1; //offset start is 1 based
			result.add(new WordMatch(lineOffset, offsetStart, matcher.group()));
		}
		return result;
	}

	//could be done with single value array, but this is more descriptive
	private static class MutableInt{
		int value;
		public MutableInt(int startVal){
			value = startVal;
		}
		public int getAndIncr(){
			return value++;
		}
	}
}
