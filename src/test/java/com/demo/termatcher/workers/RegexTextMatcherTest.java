package com.demo.termatcher.workers;

import com.demo.termatcher.model.Chunk;
import com.demo.termatcher.model.WordMatch;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class RegexTextMatcherTest {

	Map<Integer, Chunk> chunkMap = new HashMap<Integer, Chunk>(){{
		put(0,new Chunk(1, "123 qwe\n456 rty", 0));
		put(1,new Chunk(3, "789 aze\n0123 rty", 1));
	}};

	@Test
	public void testMatching() {
		new RegexTextMatcher(chunkMap.get(0), Arrays.asList("rty","789"), match -> {
			WordMatch expected = new WordMatch(2, 5, "rty");
			Assert.assertEquals(expected, match);
		});

		new RegexTextMatcher(chunkMap.get(1), Arrays.asList("aze","rty"), match -> {
			Map<String, WordMatch> matchMap = new HashMap<String, WordMatch>(){{
				put("rty",new WordMatch(4, 5, "rty"));
				put("aze",new WordMatch(3, 5, "aze"));
			}};
			WordMatch expected = matchMap.get(match.getTerm());
			Assert.assertEquals(expected, match);
		});
	}
}