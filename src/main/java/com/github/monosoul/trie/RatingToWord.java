package com.github.monosoul.trie;

import java.util.Map.Entry;
import lombok.Value;

@Value
public class RatingToWord implements Entry<Integer, String> {

	Integer rating;
	String word;

	@Override
	public Integer getKey() {
		return rating;
	}

	@Override
	public String getValue() {
		return word;
	}

	@Override
	public String setValue(final String value) {
		throw new UnsupportedOperationException();
	}

	public static RatingToWord of(final Integer rating, final String word) {
		return new RatingToWord(rating, word);
	}
}