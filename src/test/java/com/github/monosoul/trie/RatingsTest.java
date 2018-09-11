package com.github.monosoul.trie;

import static java.util.Collections.emptySet;
import static java.util.stream.Stream.generate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import com.github.monosoul.trie.util.LocalRandom;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class RatingsTest {

	private static final LocalRandom RANDOM = new LocalRandom();
	private static final int LIMIT = 10;

	@Mock(answer = RETURNS_DEEP_STUBS)
	private SortedMap<Integer, Set<TrieNode>> internalMap;
	@InjectMocks
	private Ratings ratings;

	@BeforeEach
	void setUp() {
		initMocks(this);
	}

	@SuppressWarnings("unchecked")
	@ParameterizedTest
	@MethodSource("ratingAndTrieNodeStream")
	void put(final Integer rating, final TrieNode node) {
		val trieNodeSet = mock(TrieNodeSet.class);
		val funCaptor = forClass(Function.class);

		doReturn(trieNodeSet).when(internalMap).computeIfAbsent(anyInt(), any(Function.class));

		ratings.put(rating, node);

		verify(internalMap).computeIfAbsent(eq(rating), funCaptor.capture());
		verify(trieNodeSet).add(node);
		verifyNoMoreInteractions(internalMap, trieNodeSet);

		assertThat(funCaptor.getValue().apply(RANDOM.nextInt())).isInstanceOf(TrieNodeSet.class);
	}

	@SuppressWarnings("unchecked")
	@ParameterizedTest
	@MethodSource("intStream")
	void get(final Integer rating) {
		val entrySet = mock(Set.class);

		doReturn(entrySet).when(internalMap).getOrDefault(anyInt(), anySet());

		val actual = ratings.get(rating);

		verify(internalMap).getOrDefault(rating, emptySet());
		verifyNoMoreInteractions(internalMap);

		assertThat(actual).isSameAs(entrySet);
	}

	@SuppressWarnings("unchecked")
	@Test
	void entrySet() {
		val entrySet = mock(Set.class);

		when(internalMap.entrySet()).thenReturn(entrySet);

		val actual = ratings.entrySet();

		verify(internalMap).entrySet();
		verifyNoMoreInteractions(internalMap);

		assertThat(actual).isSameAs(entrySet);
	}

	@Test
	void isEmpty() {
		ratings.isEmpty();

		verify(internalMap).isEmpty();
		verifyNoMoreInteractions(internalMap);
	}

	private static IntStream intStream() {
		return RANDOM.ints(LIMIT);
	}

	private static Stream<Arguments> ratingAndTrieNodeStream() {
		return generate(() -> (Arguments) () -> {
			val node = mock(TrieNode.class);
			when(node.getValue()).thenReturn(RANDOM.nextChar());

			return new Object[]{RANDOM.nextInt(), node};
		}).limit(LIMIT);
	}
}