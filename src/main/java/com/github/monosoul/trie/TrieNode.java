package com.github.monosoul.trie;

import lombok.ToString;

@ToString
public class TrieNode {

	private final Character value;
	private final Children children;
	private boolean isWord;

	public TrieNode(final Character value) {
		this.value = value;
		this.children = new Children('a', 'z');
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
		return children.computeIfAbsent(value, x -> new TrieNode(value));
	}

	public TrieNode getChild(final char value) {
		return children.get(value);
	}

	public void removeChild(final char value) {
		children.remove(value);
	}

	boolean hasChildren() {
		return !children.isEmpty();
	}
}

