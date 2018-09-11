package com.github.monosoul.trie;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import com.github.monosoul.trie.util.LocalRandom;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Stream;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class TrieNodeSetTest {

	private static final LocalRandom RANDOM = new LocalRandom();
	private static final int LIMIT = 10;

	@Mock(answer = RETURNS_DEEP_STUBS)
	private SortedMap<Character, TrieNode> nodes;
	@InjectMocks
	private TrieNodeSet trieNodeSet;

	@BeforeEach
	void setUp() {
		initMocks(this);
	}

	@Test
	void size() {
		val size = RANDOM.nextInt();

		when(nodes.size()).thenReturn(size);

		val actual = trieNodeSet.size();

		verify(nodes).size();
		verifyNoMoreInteractions(nodes);

		assertThat(actual).isEqualTo(size);
	}

	@Test
	void isEmpty() {
		val isEmpty = RANDOM.nextBoolean();

		when(nodes.isEmpty()).thenReturn(isEmpty);

		val actual = trieNodeSet.isEmpty();

		verify(nodes).isEmpty();
		verifyNoMoreInteractions(nodes);

		assertThat(actual).isEqualTo(isEmpty);
	}

	@RepeatedTest(value = LIMIT)
	void containsTrieNode() {
		val trieNode = mock(TrieNode.class);
		val value = RANDOM.nextChar();
		val contains = RANDOM.nextBoolean();

		when(trieNode.getValue()).thenReturn(value);
		when(nodes.containsKey(value)).thenReturn(contains);

		val actual = trieNodeSet.contains(trieNode);

		verify(nodes).containsKey(value);
		verifyNoMoreInteractions(nodes);

		assertThat(actual).isEqualTo(contains);
	}

	@RepeatedTest(value = LIMIT)
	void containsOtherClassInstance() {
		val instance = mock(Object.class);
		val contains = RANDOM.nextBoolean();

		when(nodes.containsValue(instance)).thenReturn(contains);

		val actual = trieNodeSet.contains(instance);

		verify(nodes).containsValue(instance);
		verifyNoMoreInteractions(nodes);

		assertThat(actual).isEqualTo(contains);
	}

	@SuppressWarnings("unchecked")
	@Test
	void iterator() {
		val iterator = mock(Iterator.class);

		when(nodes.values().iterator()).thenReturn(iterator);

		val actual = trieNodeSet.iterator();

		verify(nodes.values()).iterator();
		verifyNoMoreInteractions(nodes.values());

		assertThat(actual).isSameAs(iterator);
	}

	@Test
	void toArray() {
		val array = new Object[0];

		when(nodes.values().toArray()).thenReturn(array);

		val actual = trieNodeSet.toArray();

		verify(nodes.values()).toArray();
		verifyNoMoreInteractions(nodes.values());

		assertThat(actual).isSameAs(array);
	}

	@Test
	void toTypedArray() {
		val resultArr = new TrieNode[0];

		when(nodes.values().toArray(any(TrieNode[].class))).thenReturn(resultArr);

		val actual = trieNodeSet.toArray(new TrieNode[0]);

		verify(nodes.values()).toArray(any(TrieNode[].class));
		verifyNoMoreInteractions(nodes.values());

		assertThat(actual).isSameAs(resultArr);
	}

	@Test
	void add() {
		val trieNode = mock(TrieNode.class);
		val value = RANDOM.nextChar();

		when(trieNode.getValue()).thenReturn(value);

		val actual = trieNodeSet.add(trieNode);

		verify(nodes).putIfAbsent(value, trieNode);
		verifyNoMoreInteractions(nodes);

		assertThat(actual).isTrue();
	}

	@Test
	void remove() {
		assertThatThrownBy(() -> trieNodeSet.remove(mock(Object.class)))
				.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void containsAll() {
		val arg = mock(Collection.class);
		val contains = RANDOM.nextBoolean();

		when(nodes.values().containsAll(anyCollection())).thenReturn(contains);

		val actual = trieNodeSet.containsAll(arg);

		verify(nodes.values()).containsAll(arg);
		verifyNoMoreInteractions(nodes.values());

		assertThat(actual).isSameAs(contains);
	}

	@ParameterizedTest
	@MethodSource("nodeListStream")
	void addAll(final List<TrieNode> nodeList) {
		trieNodeSet.addAll(nodeList);

		nodeList.forEach(item -> verify(nodes).putIfAbsent(item.getValue(), item));
		verifyNoMoreInteractions(nodes);
	}

	@Test
	void retainAll() {
		assertThatThrownBy(() -> trieNodeSet.retainAll(mock(Collection.class)))
				.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void removeAll() {
		assertThatThrownBy(() -> trieNodeSet.removeAll(mock(Collection.class)))
				.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void clear() {
		trieNodeSet.clear();

		verify(nodes).clear();
		verifyNoMoreInteractions(nodes);
	}

	private static Stream<TrieNode> nodeStream() {
		return range(0, LIMIT).mapToObj(x -> mock(TrieNode.class));
	}

	private static Stream<List<TrieNode>> nodeListStream() {
		return range(0, LIMIT).mapToObj(x -> nodeStream().collect(toList()));
	}
}