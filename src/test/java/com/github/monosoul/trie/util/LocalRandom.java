package com.github.monosoul.trie.util;

import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Iterables.size;
import static java.util.stream.IntStream.range;
import java.util.Random;
import lombok.val;

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

	public String nextAlphabeticString(final int minLength, final int maxLength) {
		return range(minLength, maxLength).mapToObj(x -> nextChar())
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
	}

	public String nextAlphabeticString() {
		return nextAlphabeticString(10, 100);
	}

	public <T> T getRandomItem(final Iterable<T> iterable) {
		val size = size(iterable);
		val itemIndex = nextIntBetween(0, size);

		return get(iterable, itemIndex);
	}
}
