package com.demo.termatcher.providers;

import com.demo.termatcher.model.Chunk;
import lombok.NonNull;


import java.util.function.Consumer;

public interface TextProvider {
	void readText(@NonNull Consumer<Chunk> chunkConsumer);
}
