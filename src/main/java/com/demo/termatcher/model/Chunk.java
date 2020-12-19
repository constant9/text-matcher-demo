package com.demo.termatcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Chunk {
	int lineOffset;
	String text;
	int chunkId;
}
