package com.github.monosoul.test;

import static com.github.monosoul.trie.RatingToWord.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.generate;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.stream.Stream;
import com.github.monosoul.trie.RatingToWord;
import com.github.monosoul.trie.Trie;
import com.github.monosoul.trie.util.LocalRandom;
import com.github.monosoul.trie.util.Timer;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TrieTestIT {

	private static final LocalRandom RANDOM = new LocalRandom();
	private static final int LIMIT = 10;

	private Trie trie;

	@BeforeEach
	void setUp() {
		trie = new Trie();
	}

	@ParameterizedTest
	@MethodSource("ratingToWordListStream")
	void contains(final List<RatingToWord> words) {
		val toFind = RANDOM.getRandomItem(words).getWord();

		words.forEach(trie::addWord);

		assertThat(trie.contains(toFind)).isTrue();
	}

	@ParameterizedTest
	@MethodSource("ratingToWordListStream")
	void doesNotContain(final List<RatingToWord> words) {
		val toFind = RANDOM.nextAlphabeticString();

		words.forEach(trie::addWord);

		assertThat(trie.contains(toFind)).isFalse();
	}

	@ParameterizedTest
	@MethodSource("ratingToWordListStream")
	void startsWith(final List<RatingToWord> words) {
		val word = RANDOM.getRandomItem(words).getWord();
		val toFind = word.substring(0, RANDOM.nextIntBetween(1, word.length()));

		words.forEach(trie::addWord);

		assertThat(trie.startsWith(toFind)).isTrue();
	}

	@ParameterizedTest
	@MethodSource("ratingToWordListStream")
	void doesNotStartWith(final List<RatingToWord> words) {
		val toFind = RANDOM.nextAlphabeticString();

		words.forEach(trie::addWord);

		assertThat(trie.startsWith(toFind)).isFalse();
	}

	@ParameterizedTest(name = "Iteration #{index}")
	@MethodSource("ratingToWordListStream")
	void getTopTenFor(final List<RatingToWord> words) {
		val top = 10;
		val prefix = Character.toString(RANDOM.nextChar());

		words.forEach(trie::addWord);

		val expectedTimer = new Timer();
		val expected = getTopFor(top, prefix, words);
		val passedPassed = expectedTimer.passedMillis();
		System.out.println("Calculation time of expected list: " + passedPassed);

		val actualTimer = new Timer();
		val actual = trie.getTopFor(top, prefix);
		val actualPassed = actualTimer.passedMillis();
		System.out.println("Calculation time of actual list: " + actualPassed);

		assertThat(actual).containsExactly(expected.toArray(new String[0]));
	}

	private static List<String> getTopFor(final int top, final String prefix, final List<RatingToWord> words) {
		return words.stream()
				.filter(x -> x.getWord().startsWith(prefix))
				.sorted((x, y) -> y.getRating().compareTo(x.getRating()) * 100 + x.getWord().compareTo(y.getWord()))
				.limit(top)
				.map(RatingToWord::getWord)
				.collect(toList());
	}

	private static Stream<RatingToWord> ratingToWordStream() {
		return generate(() -> of(RANDOM.nextIntBetween(0, 100), RANDOM.nextAlphabeticString()))
				.limit(100000);
	}

	private static Stream<List<RatingToWord>> ratingToWordListStream() {
		return generate(() -> ratingToWordStream().collect(toList())).limit(LIMIT);
	}
}
