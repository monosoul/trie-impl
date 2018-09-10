package com.github.monosoul.trie;

import static com.github.monosoul.trie.RatingToWord.of;
import static org.assertj.core.api.Assertions.assertThat;
import lombok.val;
import org.junit.jupiter.api.Test;

class TrieTest {

	@Test
	void contains() {
		val trie = new Trie();
		trie.addWord(of(100, "aaa"));
		trie.addWord(of(40, "aaaa"));

		assertThat(trie.contains("aaa")).isTrue();
	}

	@Test
	void doesNotContain() {
		val trie = new Trie();
		trie.addWord(of(100, "aaa"));
		trie.addWord(of(40, "aaaa"));

		assertThat(trie.contains("aa")).isFalse();
	}

	@Test
	void startsWith() {
		val trie = new Trie();
		trie.addWord(of(100, "aaa"));
		trie.addWord(of(40, "aaaa"));

		assertThat(trie.startsWith("aa")).isTrue();
	}

	@Test
	void doesNotStartWith() {
		val trie = new Trie();
		trie.addWord(of(100, "aaa"));
		trie.addWord(of(40, "aaaa"));

		assertThat(trie.startsWith("b")).isFalse();
	}

	@Test
	void getTopTenFor() {
		val trie = new Trie();

		trie.addWord(of(100, "aaa"));
		trie.addWord(of(40, "aaaa"));
		trie.addWord(of(70, "aba"));
		trie.addWord(of(50, "abc"));
		trie.addWord(of(90, "abb"));
		trie.addWord(of(90, "bbb"));
		trie.addWord(of(80, "bbbb"));
		trie.addWord(of(70, "bbba"));
		trie.addWord(of(70, "bbbc"));
		trie.addWord(of(60, "babc"));
		trie.addWord(of(90, "zzz"));

		assertThat(trie.getTopFor(10, "a")).containsExactly("aaa", "abb", "aba", "abc", "aaaa");
	}
}