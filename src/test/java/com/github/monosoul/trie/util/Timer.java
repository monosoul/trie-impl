package com.github.monosoul.trie.util;

import static java.lang.System.currentTimeMillis;

public class Timer {

	private final long millis;

	public Timer() {
		this.millis = currentTimeMillis();
	}

	public long passedMillis() {
		return currentTimeMillis() - millis;
	}
}
