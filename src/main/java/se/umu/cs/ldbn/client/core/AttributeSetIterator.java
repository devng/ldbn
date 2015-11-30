package se.umu.cs.ldbn.client.core;

import java.util.Iterator;

public interface AttributeSetIterator extends Iterator<String> {
	int nextAttIndex();
	String nextAttName();
}
