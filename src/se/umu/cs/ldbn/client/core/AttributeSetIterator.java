package se.umu.cs.ldbn.client.core;

import java.util.Iterator;

public interface AttributeSetIterator extends Iterator<String> {
	public long nextAttIndex();
	public String nextAttName();
}
