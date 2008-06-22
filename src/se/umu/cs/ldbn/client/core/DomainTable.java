package se.umu.cs.ldbn.client.core;

import java.util.ArrayList;
import java.util.List;

//max 64 atts
public final class DomainTable {
	private List<String> attNames;
	private List<Long> attIndices;
	private List<DomainTableListener> listeners;
	private int index;
	
	public DomainTable() {
		attNames = new ArrayList<String>();
		attIndices = new ArrayList<Long>();
		listeners = new ArrayList<DomainTableListener>();
		index = 0;
	}
	
	public DomainTable(String[] initAtts) {
		attNames = new ArrayList<String>(initAtts.length);
		attIndices = new ArrayList<Long>(initAtts.length);
		listeners = new ArrayList<DomainTableListener>();
		int i = 0;
		long val = 0;
		for (; i < initAtts.length; i++) {
			val = 1L << i;
			attNames.add(initAtts[i]);
			attIndices.add(new Long(val));
		}
		index = i;
	}
	
	public void setNewNames(List<String> names) {
		if (names == null) {
			return;
		}
		attNames.clear();
		attIndices.clear();
		int i = 0;
		long val = 0;
		for (; i < names.size(); i++) {
			val = 1L << i;
			attNames.add(names.get(i));
			attIndices.add(new Long(val));
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
		if(index >= 64) return false;
		long val = 1L << index;
		if(containsNameCanseInsensitive(attName)) return false;
		attNames.add(attName);
		attIndices.add(new Long(val));
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
	
	public long getAttIndex(String name) {
		int i = indexNameCaseInsensitive(name);
		if(i >= 0) {
			return attIndices.get(i).longValue();
		} else {
			return 0;
		}
	}
	
	public String getAttName(long attIndex) {
		int i = attIndices.indexOf(new Long(attIndex));
		if(i >= 0) {
			return attNames.get(i);
		} else {
			return null;
		}
	}
	
	public boolean containsAttIndex(long attIndex) {
		int i = attIndices.indexOf(new Long(attIndex));
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
	
	public Long[] getAttIndices() {
		Long[] result = new Long[attIndices.size()];
		return attIndices.toArray(result);
	}
	
	public long getAttIndicesAsLong() {
		long result = 0L;
		for (Long l : attIndices) {
			result |= l;
		}
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
