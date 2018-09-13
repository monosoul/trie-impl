package com.github.monosoul.trie;

import java.util.function.Function;
import lombok.NonNull;
import lombok.ToString;
import lombok.var;

@ToString
public class Children {

	private final TrieNode[] childrenArr;
	private final Function<Character, Integer> getIndex;

	public Children(final char alphabetStart, final char alphabetEnd) {
		this.childrenArr = new TrieNode[alphabetEnd - alphabetStart + 1];
		this.getIndex = (x) -> x - alphabetStart;
	}

	public void put(final TrieNode node) {
		childrenArr[getIndex.apply(node.getValue())] = node;
	}

	public TrieNode computeIfAbsent(final char character, @NonNull final Function<Character, TrieNode> nodeProvider) {
		var node = childrenArr[getIndex.apply(character)];
		if (node == null) {
			var newNode = nodeProvider.apply(character);
			if (newNode != null) {
				childrenArr[getIndex.apply(character)] = newNode;
				return newNode;
			}
		}
		return node;
	}

	public TrieNode get(final char character) {
		return childrenArr[getIndex.apply(character)];
	}

	public void remove(final char character) {
		childrenArr[getIndex.apply(character)] = null;
	}
}
