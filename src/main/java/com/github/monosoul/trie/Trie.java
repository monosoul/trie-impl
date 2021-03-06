package com.github.monosoul.trie;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import lombok.ToString;
import lombok.val;
import lombok.var;
import java.util.Optional;

@ToString
public class Trie {

	private final TrieNode root;

	public Trie() {
		this.root = new TrieNode();
	}

	Trie(final TrieNode root) {
		this.root = root;
	}

	public void addWord(final CharSequence word) {
		val lastChar = word.length() - 1;
		var curNode = root;
		for (var i = 0; i < word.length(); i++) {
			curNode = curNode.addChild(word.charAt(i));
			if (i == lastChar) {
				curNode.isWord(true);
			}
		}
	}

	public boolean startsWith(final CharSequence prefix) {
		return find(root, prefix, false).isPresent();
	}

	public boolean contains(final CharSequence word) {
		return find(root, word, true).isPresent();
	}

	private Optional<TrieNode> find(final TrieNode node, final CharSequence word, final boolean wordsOnly) {
		if (word.length() == 0) {
			return empty();
		}

		val child = node.getChild(word.charAt(0));
		if (child == null) {
			return empty();
		}
		if (word.length() == 1 && (!wordsOnly || child.isWord())) {
			return of(child);
		}

		return find(child, word.subSequence(1, word.length()), wordsOnly);
	}
}
