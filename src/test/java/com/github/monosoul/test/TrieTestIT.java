package com.github.monosoul.test;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.generate;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.stream.Stream;
import com.github.monosoul.trie.Trie;
import com.github.monosoul.trie.util.LocalRandom;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TrieTestIT {

	private static final LocalRandom RANDOM = new LocalRandom();
	private static final int LIMIT = 10;

	private Trie trie;

	@BeforeEach
	void setUp() {
		trie = new Trie();
	}

	@ParameterizedTest
	@MethodSource("stringListStream")
	void contains(final List<String> words) {
		val toFind = RANDOM.getRandomItem(words);

		words.forEach(trie::addWord);

		assertThat(trie.contains(toFind)).isTrue();
	}

	@ParameterizedTest
	@MethodSource("stringListStream")
	void doesNotContain(final List<String> words) {
		val toFind = RANDOM.nextAlphabeticString();

		words.forEach(trie::addWord);

		assertThat(trie.contains(toFind)).isFalse();
	}

	@ParameterizedTest
	@MethodSource("stringListStream")
	void startsWith(final List<String> words) {
		val word = RANDOM.getRandomItem(words);
		val toFind = word.substring(0, RANDOM.nextIntBetween(1, word.length()));

		words.forEach(trie::addWord);

		assertThat(trie.startsWith(toFind)).isTrue();
	}

	@ParameterizedTest
	@MethodSource("stringListStream")
	void doesNotStartWith(final List<String> words) {
		val toFind = RANDOM.nextAlphabeticString();

		words.forEach(trie::addWord);

		assertThat(trie.startsWith(toFind)).isFalse();
	}

	private static Stream<String> stringStream() {
		return generate(RANDOM::nextAlphabeticString).limit(LIMIT);
	}

	private static Stream<List<String>> stringListStream() {
		return generate(() -> stringStream().collect(toList())).limit(LIMIT);
	}
}
