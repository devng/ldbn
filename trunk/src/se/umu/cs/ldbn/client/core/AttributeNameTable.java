package se.umu.cs.ldbn.client.core;

import java.util.ArrayList;
import java.util.List;

//max 64 atts
public final class AttributeNameTable {
	private List<String> attNames;
	private List<Long> attIndeces;
	
	private int index;
	
	public AttributeNameTable() {
		attNames = new ArrayList<String>();
		attIndeces = new ArrayList<Long>();
		index = 0;
	}
	
	public AttributeNameTable(String[] initAtts) {
		attNames = new ArrayList<String>(initAtts.length);
		attIndeces = new ArrayList<Long>(initAtts.length);
		int i = 0;
		long val = 0;
		for (; i < initAtts.length; i++) {
			val = 1L << i;
			attNames.add(initAtts[i]);
			attIndeces.add(new Long(val));
		}
		index = i;
	}
	
	public boolean addAtt(String attName) {
		if(index >= 64) return false;
		long val = 1L << index;
		if(attNames.contains(attName)) return false;
		attNames.add(attName);
		attIndeces.add(new Long(val));
		index++;
		return true;
	}
	
	public long getAttIndex(String name) {
		int i = attNames.indexOf(name);
		if(i >= 0) {
			return attIndeces.get(i).longValue();
		} else {
			return 0;
		}
	}
	
	public String getAttName(long attIndex) {
		int i = attIndeces.indexOf(new Long(attIndex));
		if(i >= 0) {
			return attNames.get(i);
		} else {
			return null;
		}
	}
	
	public boolean containsAttIndex(long attIndex) {
		int i = attIndeces.indexOf(new Long(attIndex));
		return i >= 0;
	}
	
	public int size() {
		return index;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < attNames.size(); i++) {
			sb.append(attNames.get(i));
			sb.append('\t');
			sb.append(attIndeces.get(i));
			sb.append('\n');
		}
		return sb.toString();
	}
	
	public String[] getAttNames() {
		String[] result = new String[attNames.size()];
		return attNames.toArray(result);
	}
}
