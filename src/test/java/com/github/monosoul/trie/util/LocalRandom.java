package com.github.monosoul.trie.util;

import java.util.Random;

public class LocalRandom extends Random {

	public int nextIntBetween(final int origin, final int bound) {
		return nextInt(bound - origin) + origin;
	}

	public char nextCharBetween(final char origin, final char bound) {
		return (char) nextIntBetween(origin, bound);
	}

	public char nextChar() {
		return nextCharBetween('a', 'z');
	}
}
