package com.github.monosoul.trie;

import static org.assertj.core.api.Assertions.assertThat;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Random;
import java.util.stream.Stream;

class TrieNodeTest {

	private final static int LIMIT = 10;

	@Test
	void isRoot() {
		val actual = new TrieNode().isRoot();

		assertThat(actual).isTrue();
	}

	@ParameterizedTest
	@MethodSource("characterStream")
	void isNotRoot(final Character character) {
		val actual = new TrieNode(character).isRoot();

		assertThat(actual).isFalse();
	}

	@Test
	void isNotWord() {
		val actual = new TrieNode().isWord();

		assertThat(actual).isFalse();
	}

	@Test
	void isWord() {
		val node = new TrieNode().isWord(true);

		val actual = node.isWord();

		assertThat(actual).isTrue();
	}

	@ParameterizedTest
	@MethodSource("characterStream")
	void getValue(final Character character) {
		val actual = new TrieNode(character).getValue();

		assertThat(actual).isEqualTo(character);
	}

	@ParameterizedTest
	@MethodSource("characterStream")
	void addChild(final Character character) {
		val node = new TrieNode();
		val child = node.addChild(character);

		assertThat(child.getValue()).isEqualTo(character);
	}

	@ParameterizedTest
	@MethodSource("characterStream")
	void getChild(final Character character) {
		val node = new TrieNode();
		val expected = node.addChild(character);

		val actual = node.getChild(character);

		assertThat(actual).isNotNull()
				.isEqualTo(expected);
		assertThat(actual.getValue()).isEqualTo(character);
	}

	private static Stream<Character> characterStream() {
		return new Random().ints('a', 'z')
				.limit(LIMIT)
				.mapToObj(x -> (char) x);
	}
}