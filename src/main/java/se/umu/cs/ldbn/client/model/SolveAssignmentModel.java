package se.umu.cs.ldbn.client.model;

import se.umu.cs.ldbn.shared.core.Assignment;
import se.umu.cs.ldbn.shared.core.AttributeSet;
import se.umu.cs.ldbn.shared.core.DomainTable;
import se.umu.cs.ldbn.shared.core.FD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SolveAssignmentModel {

	private Assignment assignment;

	private DomainTable domain;
	private AttributeSet domainAsAttSet;
	private List<FD> fds;

	private List<AttributeSet> originalKeys;
	private Integer id;
	private String name;
	//cache
	/**
	 * Holds the partial FD cover for every model created relation.
	 * The key is the hash code  of the relation attribute set
	 */
	private Map<Integer, List<FD>> cacheFD;
	private Map<Integer, List<AttributeSet>> cacheKeys;
	private List<FD> minCoverF;

	SolveAssignmentModel() {
		super();
		//this are not used, before an assignment has been loaded,  but
		//if they are not set a NullPointer or IllegalArgument exception
		//can be thrown
		domain = new DomainTable();
		fds = new ArrayList<>();
		originalKeys = new ArrayList<>();
		minCoverF = new ArrayList<>();
		cacheFD = new HashMap<>();
		cacheKeys = new HashMap<>();
		id = 1; // we always start with a cached version of the DB assignment with id = 1
	}

	public void init(final Assignment a) {
		clear();
		if (assignment == null) {
			return;
		}
		this.assignment = a;
		this.name = a.getName();
		this.id = a.getID();
		if (a.getDomain() != null) {
			this.domain = a.getDomain();
			this.domainAsAttSet = a.getDomain().createAttributeSet();
		}
		if (a.getFDs() != null) {
			fds.addAll(a.getFDs());
		}
	}

	public void clear() {
		id = null;
		name = null;
		domain = null;
		domainAsAttSet = null;
		fds.clear();
		originalKeys.clear();
		minCoverF.clear();
		cacheKeys.clear();
		cacheFD.clear();
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public DomainTable getDomain() {
		return domain;
	}

	public AttributeSet getDomainAsAttSet() {
		return domainAsAttSet;
	}

	public List<FD> getFds() {
		return fds;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<FD> getMinCoverF() {
		return minCoverF;
	}

	public List<AttributeSet> getOriginalKeys() {
		return originalKeys;
	}

	public Map<Integer, List<FD>> getCacheFD() {
		return cacheFD;
	}

	public Map<Integer, List<AttributeSet>> getCacheKeys() {
		return cacheKeys;
	}
}
