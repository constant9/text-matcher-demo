package com.demo.termatcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@AllArgsConstructor
public class WordMatch {
	int lineOffset;
	int charOffset;
	String term;
}
