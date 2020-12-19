package com.demo.termatcher.aggregators;

import com.demo.termatcher.model.WordMatch;

import java.util.List;

public interface MatchesAggregator {
	void put(WordMatch wordMatch);
	List<WordMatch> get(String word);
}
