package com.github.monosoul.trie;

import lombok.ToString;
import lombok.val;
import lombok.var;

@ToString
public class TrieNode {

	private static final int ALPHABET_SIZE = 'z' - 'a' + 1;

	private final Character value;
	private final TrieNode[] children;
	private boolean isWord;

	public TrieNode(final Character value) {
		this.value = value;
		this.children = new TrieNode[ALPHABET_SIZE];
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

	public TrieNode addChild(final char value) {
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
}

