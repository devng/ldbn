package se.umu.cs.ldbn.shared.core;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Assignment {

	private final DomainTable domain;
	private final List<FD> fds;
	private final Integer id;
	private final String name;

	public Assignment(final DomainTable domain, final List<FD> fds, final Integer id, final String name) {
		this.domain = domain;
		this.fds = Collections.unmodifiableList(fds);
		this.id = id;
		this.name = name;
	}

	public List<FD> getFDs() {
		return fds;
	}

	public DomainTable getDomain() {
		return domain;
	}

	public Integer getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Assignment)) return false;
		Assignment that = (Assignment) o;
		return Objects.equals(getDomain(), that.getDomain()) &&
				Objects.equals(fds, that.fds) &&
				Objects.equals(id, that.id) &&
				Objects.equals(getName(), that.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getDomain(), fds, id, getName());
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Assignment{");
		sb.append("domain=").append(domain);
		sb.append(", fds=").append(fds);
		sb.append(", id=").append(id);
		sb.append(", name='").append(name).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
