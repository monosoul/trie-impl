package com.github.monosoul.trie;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import lombok.NonNull;

public class TrieNodeSet implements Set<TrieNode> {

	private final SortedMap<Character, TrieNode> nodes;

	TrieNodeSet(@NonNull final SortedMap<Character, TrieNode> nodes) {
		this.nodes = nodes;
	}

	public TrieNodeSet() {
		this(new TreeMap<>());
	}

	@Override
	public int size() {
		return nodes.size();
	}

	@Override
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	@Override
	public boolean contains(final Object o) {
		if (TrieNode.class.isAssignableFrom(o.getClass())) {
			return nodes.containsKey(((TrieNode) o).getValue());
		}
		return nodes.containsValue(o);
	}

	@Override
	public Iterator<TrieNode> iterator() {
		return nodes.values().iterator();
	}

	@Override
	public Object[] toArray() {
		return nodes.values().toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return nodes.values().toArray(a);
	}

	@Override
	public boolean add(final TrieNode node) {
		return nodes.putIfAbsent(node.getValue(), node) != null;
	}

	@Override
	public boolean remove(final Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return nodes.values().containsAll(c);
	}

	@Override
	public boolean addAll(final Collection<? extends TrieNode> c) {
		c.forEach(this::add);
		return true;
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		nodes.clear();
	}
}
