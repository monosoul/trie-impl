package com.github.monosoul.trie;

import static com.github.monosoul.trie.RatingToWord.of;
import static java.util.stream.Stream.generate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import java.util.stream.Stream;
import com.github.monosoul.trie.util.LocalRandom;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

class TrieTest {

	private static final LocalRandom RANDOM = new LocalRandom();
	private static final int LIMIT = 10;

	@Mock
	private TrieNode root;

	@BeforeEach
	void setUp() {
		initMocks(this);
	}

	@ParameterizedTest
	@MethodSource("ratingToWordStream")
	void addWord(final RatingToWord ratingToWord) {
		when(root.addChild(anyChar(), anyInt())).thenReturn(root);

		new Trie(root).addWord(ratingToWord);

		val inOrder = inOrder(root);
		ratingToWord.getWord().chars().mapToObj(x -> (char) x).forEach(x ->
				inOrder.verify(root, calls(1)).addChild(x, ratingToWord.getRating())
		);
		inOrder.verify(root).isWord(true);
		inOrder.verifyNoMoreInteractions();
	}

	@ParameterizedTest
	@MethodSource("stringStream")
	void startsWith(final String word) {
		val subString = word.substring(0, RANDOM.nextIntBetween(1, word.length() - 1));

		when(root.getChild(anyChar())).thenReturn(root);

		val actual = new Trie(root).startsWith(subString);

		val inOrder = inOrder(root);
		subString.chars().forEach(x ->
				inOrder.verify(root, calls(1)).getChild((char) x)
		);
		inOrder.verifyNoMoreInteractions();

		assertThat(actual).isTrue();
	}

	@Test
	void doesNotContainIfWordIsEmpty() {
		when(root.getChild(anyChar())).thenReturn(root);

		val actual = new Trie(root).contains("");

		verifyZeroInteractions(root);

		assertThat(actual).isFalse();
	}

	@ParameterizedTest
	@MethodSource("stringStream")
	void doesNotContainIfNoSuchChild(final String word) {
		when(root.getChild(anyChar())).thenReturn(null);

		val actual = new Trie(root).contains(word);

		verify(root).getChild(word.charAt(0));
		verifyNoMoreInteractions(root);

		assertThat(actual).isFalse();
	}

	@ParameterizedTest
	@MethodSource("stringStream")
	void contains(final String word) {
		when(root.getChild(anyChar())).thenReturn(root);
		when(root.isWord()).thenReturn(true);

		val actual = new Trie(root).contains(word);

		val inOrder = inOrder(root);
		word.chars().forEach(x ->
				inOrder.verify(root, calls(1)).getChild((char) x)
		);
		inOrder.verify(root).isWord();
		inOrder.verifyNoMoreInteractions();

		assertThat(actual).isTrue();
	}

	private static Stream<String> stringStream() {
		return generate(RANDOM::nextAlphabeticString).limit(LIMIT);
	}

	private static Stream<RatingToWord> ratingToWordStream() {
		return generate(() -> of(RANDOM.nextInt(), RANDOM.nextAlphabeticString())).limit(LIMIT);
	}
}