package com.github.monosoul.trie;

import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import lombok.ToString;
import lombok.val;
import lombok.var;

@ToString
public class TrieNode {

	private static final int ALPHABET_SIZE = 'z' - 'a' + 1;

	private final Character value;
	private final TrieNode[] children;
	private final Ratings ratings;
	private boolean isWord;

	public TrieNode(final Character value) {
		this.value = value;
		this.children = new TrieNode[ALPHABET_SIZE];
		this.ratings = new Ratings(new TreeMap<>(reverseOrder()));
		this.isWord = false;
	}

	public TrieNode() {
		this(null);
	}

	public boolean isRoot() {
		return value == null;
	}

	public Character getValue() {
		return value;
	}

	public boolean isWord() {
		return isWord;
	}

	public TrieNode isWord(final boolean isWord) {
		this.isWord = isWord;
		return this;
	}

	public TrieNode addChild(final char value, final int rating) {
		val child = addChild(value);
		ratings.put(rating, child);

		return child;
	}

	private TrieNode addChild(final char value) {
		val index = getIndex(value);
		var child = children[index];
		if (child == null) {
			child = new TrieNode(value);
			children[index] = child;
		}

		return child;
	}

	public TrieNode getChild(final char value) {
		return children[getIndex(value)];
	}

	private static int getIndex(final char value) {
		return value - 'a';
	}

	Set<TrieNode> getWithRating(final int rating) {
		return ratings.get(rating);
	}

	public Collection<StringBuilder> getTopChildren(final int top) {
		return ratings.entrySet().stream()
				.limit(top)
				.flatMap(entry ->
						entry.getValue().stream().flatMap(node ->
								getForRating(new ArrayList<>(), new StringBuilder(), entry.getKey(), node).stream()
						)
				)
				.limit(top)
				.collect(toList());
	}

	private List<StringBuilder> getForRating(
			final List<StringBuilder> topItems, final StringBuilder sb, final int rating, final TrieNode node
	) {
		sb.append(node.getValue());

		if (node.ratings.isEmpty()) {
			topItems.add(sb);
		} else {
			node.ratings.get(rating).forEach(child ->
					getForRating(topItems, new StringBuilder(sb), rating, child)
			);
		}

		return topItems;
	}
}

