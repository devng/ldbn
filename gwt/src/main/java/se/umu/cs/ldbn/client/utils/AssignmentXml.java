package se.umu.cs.ldbn.client.utils;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.xml.client.*;
import com.google.gwt.xml.client.impl.DOMParseException;
import se.umu.cs.ldbn.shared.core.Assignment;
import se.umu.cs.ldbn.shared.core.AttributeSet;
import se.umu.cs.ldbn.shared.core.DomainTable;
import se.umu.cs.ldbn.shared.core.FD;
import se.umu.cs.ldbn.shared.dto.AssignmentDto;

import java.util.ArrayList;
import java.util.List;

public final class AssignmentXml {

	private static AssignmentXml inst = null;

	private static AssignmentXml get() {
		if(inst == null) {
			inst = new AssignmentXml();
		}
		return inst;
	}

	//vars used for parsing a ldbn xml string with type=assignment
	private DomainTable currentDomain;
	private List<FD> currentFDs;
	private boolean isLHS;
	private String name;
	private Integer id;

	public static Assignment parse(final AssignmentDto dto) {
		if (dto == null) {
			return null;
		}
		get().clear();
		get().name = dto.getName();
		get().id = dto.getId();


		Assignment assignment = get().parse(dto.getXml());
		if (assignment == null) {
			Log.error("Cannot parse assignment XML. AssignmentDto does not contain any XML data.");
			return null;
		}

		return assignment;
	}

	private Assignment parse(final String xml) {
		if (xml == null) {
			return null;
		}
		Document doc;
		try {
			doc = XMLParser.parse(xml);
		} catch (DOMParseException e) {
			Log.error("Cannot parse assignment XML.", e);
			return null;
		}

		Node ldbn = doc.getElementsByTagName("ldbn").item(0);
		if(ldbn == null) {
			Log.error("Cannot find '<ldbn>' element in assignment XML.");
			return null;
		}

		String type = ((Element) ldbn).getAttribute("type");
		if (!"assignment".equals(type)) {
			Log.error("Cannot parse assignment XML. Type of '<ldbn>' element in not 'assignment'.");
			return null;
		}

		Assignment assignment = parseAssignmentXML(ldbn);
		clear();
		return assignment;
	}

	private void clear() {
		currentDomain = null;
		currentFDs = null;
		isLHS = false;
		name = null;
		id = null;
	}

	private Assignment parseAssignmentXML(final Node ldbn) {
		currentDomain = new DomainTable();
		currentFDs = new ArrayList<>();
		visitElementNodes(ldbn);
		Assignment a = new Assignment(currentDomain, currentFDs, id, name);
		return a;
	}

	private void visitElementNodes(final Node node) {
		final NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			final Element el = (Element) child;
			final String tag = el.getTagName();
			if ("att".equals(tag)) {
				String val = el.getLastChild().getNodeValue();
				currentDomain.addAtt(val);
			} else if ("fd".equals(tag)) {
				FD fd = new FD(currentDomain);
				currentFDs.add(fd);
			} else if ("lhs".equals(tag)) {
				isLHS = true;
			} else if ("rhs".equals(tag)) {
				isLHS = false;
			} else if ("fdatt".equals(tag)) {
				String val = el.getLastChild().getNodeValue();
				FD fd = currentFDs.get(currentFDs.size() - 1);
				AttributeSet fdatt;
				if (isLHS) {
					fdatt = fd.getLHS();
				} else {
					fdatt = fd.getRHS();
				}
				fdatt.addAtt(val);
			} else {
				Log.warn("Unknown assignment tag: " + tag);
			}
			visitElementNodes(child);
		}
	}

	public static String toXML(final Assignment a) {
		if (a ==  null) {
			Log.error("Cannot create an XML for an empty assignment.");
			return null;
		}
		Document doc = XMLParser.createDocument();
		Element ldbn = doc.createElement("ldbn");
		ldbn.setAttribute("type", "assignment");
		doc.appendChild(ldbn);
		DomainTable domain = a.getDomain();
		String[] atts = domain.getAttNames();
		for (String name : atts) {
			Element att = doc.createElement("att");
			ldbn.appendChild(att);
			Text txt = doc.createTextNode(name);
			att.appendChild(txt);
		}
		List<FD> fds = a.getFDs();
		for (FD fd : fds) {
			Element fdEl = doc.createElement("fd");
			ldbn.appendChild(fdEl);
			Element lhsEl = doc.createElement("lhs");
			fdEl.appendChild(lhsEl);
			AttributeSet lhsAS = fd.getLHS();
			List<String> lhsAttNames = lhsAS.getAttributeNames();
			for (String str : lhsAttNames) {
				Element fdatt = doc.createElement("fdatt");
				Text txt = doc.createTextNode(str);
				lhsEl.appendChild(fdatt);
				fdatt.appendChild(txt);
			}
			Element rhsEl = doc.createElement("rhs");
			fdEl.appendChild(rhsEl);
			AttributeSet rhsAS = fd.getRHS();
			List<String> rhsAttNames = rhsAS.getAttributeNames();
			for (String str : rhsAttNames) {
				Element fdatt = doc.createElement("fdatt");
				Text txt = doc.createTextNode(str);
				rhsEl.appendChild(fdatt);
				fdatt.appendChild(txt);
			}

		}
		return doc.toString();
	}
}
