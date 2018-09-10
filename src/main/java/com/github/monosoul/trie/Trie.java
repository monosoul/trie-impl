package com.github.monosoul.trie;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import java.util.List;
import java.util.Optional;
import lombok.ToString;
import lombok.val;
import lombok.var;

@ToString
public class Trie {

	private final TrieNode root;

	public Trie() {
		this.root = new TrieNode();
	}

	Trie(final TrieNode root) {
		this.root = root;
	}

	public void addWord(final RatingToWord ratingToWord) {
		val word = ratingToWord.getWord();
		val rating = ratingToWord.getRating();

		val lastChar = word.length() - 1;
		var curNode = root;
		for (var i = 0; i < word.length(); i++) {
			curNode = curNode.addChild(word.charAt(i), rating);
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

	public List<String> getTopFor(final int top, final CharSequence prefix) {
		val nodeOptional = find(root, prefix, false);

		return nodeOptional.map(node ->
				node.getTopChildren(top).stream()
						.map(x -> x.insert(0, prefix))
						.map(StringBuilder::toString)
						.collect(toList())
		)
				.orElse(emptyList());
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
