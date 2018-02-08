package se.umu.cs.ldbn.shared.core;

import java.util.Iterator;

public interface AttributeSetIterator extends Iterator<String> {
	int nextAttIndex();
	String nextAttName();
}
