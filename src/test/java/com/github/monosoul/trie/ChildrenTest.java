package com.github.monosoul.trie;

import static java.util.stream.Stream.generate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import com.github.monosoul.trie.util.LocalRandom;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ChildrenTest {

	private static final char ALPHABET_START = 'a';
	private static final char ALPHABET_END = 'z';
	private static final int LIMIT = 10;
	private static final LocalRandom RANDOM = new LocalRandom();

	private Children children;

	@BeforeEach
	void setUp() {
		children = new Children(ALPHABET_START, ALPHABET_END);
	}

	@ParameterizedTest
	@MethodSource("trieNodeStream")
	void put(final TrieNode node) {
		children.put(node);

		verify(node).getValue();
		verifyNoMoreInteractions(node);
	}

	@SuppressWarnings("unchecked")
	@ParameterizedTest
	@MethodSource("nodeAndCharStream")
	void computeIfExists(final TrieNode node, final char character) {
		children.put(node);

		val nodeProvider = mock(Function.class);

		val actual = children.computeIfAbsent(character, nodeProvider);

		verifyZeroInteractions(nodeProvider);

		assertThat(actual).isSameAs(node);
	}

	@SuppressWarnings("unchecked")
	@ParameterizedTest
	@MethodSource("nodeAndCharStream")
	void computeIfAbsent(final TrieNode node, final char character) {
		val nodeProvider = mock(Function.class);
		when(nodeProvider.apply(anyChar())).thenReturn(node);

		val actual = children.computeIfAbsent(character, nodeProvider);

		verify(nodeProvider).apply(character);
		verifyNoMoreInteractions(nodeProvider);

		assertThat(actual).isSameAs(node);
		assertThat(children.get(character)).isNotNull()
				.isSameAs(node);
	}

	@ParameterizedTest
	@MethodSource("nodeAndCharStream")
	void get(final TrieNode node, final char character) {
		children.put(node);

		val actual = children.get(character);

		assertThat(actual).isSameAs(node);
	}

	@ParameterizedTest
	@MethodSource("nodeAndCharStream")
	void remove(final TrieNode node, final char character) {
		children.put(node);

		children.remove(character);

		val actual = children.get(character);

		assertThat(actual).isNull();
	}

	private static Stream<TrieNode> trieNodeStream() {
		return generate(() -> nodeMockFor(RANDOM.nextCharBetween(ALPHABET_START, ALPHABET_END))).limit(LIMIT);
	}

	private static Stream<Arguments> nodeAndCharStream() {
		return generate(() -> (Arguments) () -> {
			val character = RANDOM.nextCharBetween(ALPHABET_START, ALPHABET_END);
			return new Object[]{nodeMockFor(character), character};
		}).limit(LIMIT);
	}

	private static TrieNode nodeMockFor(final char character) {
		val trieNode = mock(TrieNode.class);
		doReturn(character).when(trieNode).getValue();

		return trieNode;
	}
}