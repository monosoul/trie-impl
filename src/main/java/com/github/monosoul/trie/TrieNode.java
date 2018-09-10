package com.github.monosoul.trie;

import static java.util.Comparator.reverseOrder;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import lombok.ToString;
import lombok.experimental.var;
import lombok.val;

@ToString
public class TrieNode {

	private static final int ALPHABET_SIZE = 'z' - 'a' + 1;

	private final Character value;
	private final TrieNode[] children;
	private final SortedMap<Integer, TrieNode> ratings;
	private boolean isWord;

	public TrieNode(final Character value) {
		this.value = value;
		this.children = new TrieNode[ALPHABET_SIZE];
		this.ratings = new TreeMap<>(reverseOrder());
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

	public Collection<StringBuilder> getTopChildren(final int top) {
		val topItems = new TreeMap<Integer, StringBuilder>(reverseOrder());

		ratings.entrySet().stream()
				.limit(top)
				.forEach(
						x -> getForRating(topItems, x.getKey(), x.getValue())
				);

		return topItems.values();
	}

	private void getForRating(final SortedMap<Integer, StringBuilder> topItems, final int rating, final TrieNode node) {
		addChar(topItems, rating, node);

		val child = node.ratings.get(rating);
		if (child != null) {
			getForRating(topItems, rating, child);
		}
	}

	private static void addChar(final SortedMap<Integer, StringBuilder> topItems, final int rating, final TrieNode node) {
		topItems.computeIfAbsent(rating, x -> new StringBuilder()).append(node.getValue());
	}
}

