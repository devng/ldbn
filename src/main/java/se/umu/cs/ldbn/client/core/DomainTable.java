package se.umu.cs.ldbn.client.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Max 32 attributes.
 *  
 * Ray Cromwell at Google IO 05.29.2008 - GWT - Extreme Performance and 
 * Flexibility (watch at position 0:30:35):
 * Javascript has only one numeric data type, which is the double. Double has 
 * only 53 bits of precision. So if you want a "real" long integer 64 bit, it 
 * has to be emulated. GWT introduced this emulation in 1.5 , and for the most 
 * part it is not going to hurt the performance, but if you have methods that 
 * relay upon long, like I do, those long slow the performance.
 *
 */
public final class DomainTable {
	private List<String> attNames;
	private List<Integer> attIndices;
	private List<DomainTableListener> listeners;
	private int index;
	
	public DomainTable() {
		attNames = new ArrayList<String>();
		attIndices = new ArrayList<Integer>();
		listeners = new ArrayList<DomainTableListener>();
		index = 0;
	}
	
	public DomainTable(String[] initAtts) {
		attNames = new ArrayList<String>(initAtts.length);
		attIndices = new ArrayList<Integer>(initAtts.length);
		listeners = new ArrayList<DomainTableListener>();
		int i = 0;
		int val = 0;
		for (; i < initAtts.length; i++) {
			val = 1 << i;
			attNames.add(initAtts[i]);
			attIndices.add(new Integer(val));
		}
		index = i;
	}
	
	public List<DomainTableListener> getListers() {
		return listeners;
	}
	
	public void setNewNames(List<String> names) {
		if (names == null) {
			return;
		}
		attNames.clear();
		attIndices.clear();
		int i = 0;
		int val = 0;
		for (; i < names.size(); i++) {
			val = 1 << i;
			attNames.add(names.get(i));
			attIndices.add(new Integer(val));
		}
		index = i;
		notifyListeners();
	}
	
	public void addListener(DomainTableListener l) {
		if(l != null)
			listeners.add(l);
	}
	
	public void removeListener(DomainTableListener l) {
		if(l != null)
			listeners.remove(l);
	}
	
	public boolean addAtt(String attName) {
		if(index >= 32) return false;
		int val = 1 << index;
		if(containsNameCanseInsensitive(attName)) return false;
		attNames.add(attName);
		attIndices.add(new Integer(val));
		index++;
		notifyListeners();
		return true;
	}
	
	public boolean removeAtt(String attName) {
		if(index < 1) return false;
		int i = indexNameCaseInsensitive(attName);
		if(i >= 0) {
			attNames.remove(i);
			attIndices.remove(i);
			index--;
			notifyListeners();
			return true;
		}
		return false;
	}
	
	public void loadDomainTable(DomainTable that) {
		attNames.clear();
		attIndices.clear();
		index = 0;
		this.attNames = that.attNames;
		this.attIndices = that.attIndices;
		this.index = that.index;
		notifyListeners();
	}
	
	public void clearData() {
		attNames.clear();
		attIndices.clear();
		index = 0;
		notifyListeners();
	}
	
	public boolean renameAtt(String oldAttName, String newAttName) {
		if(index < 1) return false;
		int i = indexNameCaseInsensitive(oldAttName);
		if(i >= 0) {
			attNames.remove(i);
			attNames.add(i, newAttName);
			notifyListeners();
			return true;
		}
		return false;
	}
	
	public int getAttIndex(String name) {
		int i = indexNameCaseInsensitive(name);
		if(i >= 0) {
			return attIndices.get(i).intValue();
		} else {
			return 0;
		}
	}
	
	public String getAttName(int attIndex) {
		int i = attIndices.indexOf(new Integer(attIndex));
		if(i >= 0) {
			return attNames.get(i);
		} else {
			return null;
		}
	}
	
	public boolean containsAttIndex(int attIndex) {
		int i = attIndices.indexOf(new Integer(attIndex));
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
			sb.append(attIndices.get(i));
			sb.append('\n');
		}
		return sb.toString();
	}
	
	public String[] getAttNames() {
		String[] result = new String[attNames.size()];
		return attNames.toArray(result);
	}
	
	public Integer[] getAttMasks() {
		Integer[] result = new Integer[attIndices.size()];
		return attIndices.toArray(result);
	}
	
	public int getAttIndicesAsInteger() {
		int result = 0;
		for (Integer l : attIndices) {
			result |= l;
		}
		return result;
	}
	
	/**
	 * Creates attribute set with all attributes within the domain. 
	 */
	public AttributeSet createAttributeSet() {
		AttributeSet result = new AttributeSet(this);
		result.setMask(getAttIndicesAsInteger());
		return result;
	}
	
	private int indexNameCaseInsensitive(String name) {
		if (name == null) return -1;
		int i = 0;
		for (String attName : attNames) {
			if(attName.toLowerCase().trim().equals(name.toLowerCase().trim())) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	private boolean containsNameCanseInsensitive(String name) {
		return indexNameCaseInsensitive(name) != -1;
	}
	
	
	private void notifyListeners() {
		for (DomainTableListener l : listeners) {
			l.onDomainChange();
		}
	}
}
