package com.demo.termatcher.aggregators;

import com.demo.termatcher.model.WordMatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcurrentMapAggregator implements MatchesAggregator {
	//private ConcurrentMap<String,List<WordMatch>> wordMatchesMap = new ConcurrentHashMap<>();
	ListMultimap<String, WordMatch> wordMatchesMap = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());

	@Override
	public void put(WordMatch wordMatch) {
		wordMatchesMap.put(wordMatch.getTerm(), wordMatch);
	}

	@Override
	public List<WordMatch> get(String word) {
		return wordMatchesMap.get(word);
	}
}
