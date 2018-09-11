package com.github.monosoul.trie;

import static java.util.Collections.emptySet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import lombok.NonNull;

public class Ratings {

	private final SortedMap<Integer, Set<TrieNode>> ratings;

	public Ratings(@NonNull final SortedMap<Integer, Set<TrieNode>> ratings) {
		this.ratings = ratings;
	}

	public Ratings put(final Integer rating, final TrieNode node) {
		ratings.computeIfAbsent(rating, x -> new TrieNodeSet()).add(node);
		return this;
	}

	public Set<TrieNode> get(final Integer rating) {
		return ratings.getOrDefault(rating, emptySet());
	}

	public Set<Entry<Integer, Set<TrieNode>>> entrySet() {
		return ratings.entrySet();
	}

	public boolean isEmpty() {
		return ratings.isEmpty();
	}
}
