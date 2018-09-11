package com.github.monosoul.trie;

import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.stream.Stream;
import com.github.monosoul.trie.util.LocalRandom;
import lombok.val;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TrieNodeTest {

	private static final int LIMIT = 10;
	private static final LocalRandom RANDOM = new LocalRandom();

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
	@MethodSource("charAndRatingStream")
	void addChild(final char character, final int rating) {
		val node = new TrieNode();
		val child = node.addChild(character, rating);

		assertThat(child.getValue()).isEqualTo(character);
		assertThat(node.getWithRating(rating)).first().isSameAs(child);
	}

	@Test
	void addMultipleChildrenWithTheSameRating() {
		val rating = RANDOM.nextInt();
		val char1 = RANDOM.nextChar();
		val char2 = getRandomButNot(char1);

		val node = new TrieNode();
		val child1 = node.addChild(char1, rating);
		val child2 = node.addChild(char2, rating);

		val nodeSet = node.getWithRating(rating);

		assertThat(nodeSet).containsExactlyInAnyOrder(child1, child2);
	}

	@ParameterizedTest
	@MethodSource("characterStream")
	void getChild(final Character character) {
		val node = new TrieNode();
		val expected = node.addChild(character, RANDOM.nextInt());

		val actual = node.getChild(character);

		assertThat(actual).isNotNull()
				.isSameAs(expected);
		assertThat(actual.getValue()).isEqualTo(character);
	}

	private static char getRandomButNot(final char notChar) {
		var randomChar = RANDOM.nextChar();
		while(randomChar == notChar) {
			randomChar = RANDOM.nextChar();
		}

		return randomChar;
	}

	private static Stream<Character> characterStream() {
		return RANDOM.ints('a', 'z')
				.limit(LIMIT)
				.mapToObj(x -> (char) x);
	}

	private static Stream<Arguments> charAndRatingStream() {
		return range(0, LIMIT).mapToObj(x -> () ->
				new Object[]{RANDOM.nextChar(), RANDOM.nextInt()}
		);
	}
}