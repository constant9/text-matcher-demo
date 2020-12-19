package com.demo.termatcher.providers;

import com.demo.termatcher.model.Chunk;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
class ChunkedFileTextProviderTest {

	@Autowired
	ChunkedFileTextProvider chunkedFileTextProvider;

	Map<Integer, Chunk> chunkMap = new HashMap<Integer, Chunk>(){{
		put(0,new Chunk(1, "123 qwe\n456 rty", 0));
		put(1,new Chunk(3, "789 aze\n0123 rty", 1));
	}};

	@Test
	void readText() {
		chunkedFileTextProvider.readText(chunk -> {
			Assert.assertNotNull(chunk);
			Chunk example = chunkMap.get(chunk.getChunkId());
			Assert.assertNotNull(example);
			Assert.assertEquals(example, chunk);
		});
	}
}