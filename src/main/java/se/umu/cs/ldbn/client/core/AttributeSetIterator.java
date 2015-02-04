package se.umu.cs.ldbn.client.core;

import java.util.Iterator;

public interface AttributeSetIterator extends Iterator<String> {
	public int nextAttIndex();
	public String nextAttName();
}
