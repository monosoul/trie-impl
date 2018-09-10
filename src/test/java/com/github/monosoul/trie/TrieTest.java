package com.github.monosoul.trie;

import static java.util.stream.IntStream.rangeClosed;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import java.util.Random;
import java.util.stream.Stream;

class TrieTest {

	private final static int LIMIT = 10;

	@Mock
	private TrieNode root;

	@BeforeEach
	void setUp() {
		initMocks(this);
	}

	@ParameterizedTest
	@MethodSource("stringStream")
	void addWord(final String word) {
		when(root.addChild(anyChar())).thenReturn(root);

		new Trie(root).addWord(word);

		val inOrder = inOrder(root);
		word.chars().forEach(x ->
				inOrder.verify(root, calls(1)).addChild((char) x)
		);
		inOrder.verify(root).isWord(true);
		inOrder.verifyNoMoreInteractions();
	}

	@ParameterizedTest
	@MethodSource("stringStream")
	void startsWith(final String word) {
		val subString = word.substring(0, (new Random().nextInt(word.length() - 1) + 1));

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

	private static Stream<Character> characterStream() {
		return new Random().ints('a', 'z')
				.limit(LIMIT)
				.mapToObj(x -> (char) x);
	}

	private static Stream<String> stringStream() {
		return rangeClosed(0, LIMIT).mapToObj(x ->
				characterStream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString()
		);
	}
}