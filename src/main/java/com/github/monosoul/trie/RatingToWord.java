package com.github.monosoul.trie;

import lombok.Value;

@Value
public class RatingToWord {

	Integer rating;
	String word;

	public static RatingToWord of(final Integer rating, final String word) {
		return new RatingToWord(rating, word);
	}
}