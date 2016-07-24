package se.umu.cs.ldbn.client.io;

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

public final class AssignmentXmlParser {

	private static AssignmentXmlParser inst = null;

	public static AssignmentXmlParser get() {
		if(inst == null) {
			inst = new AssignmentXmlParser();
		}
		return inst;
	}

	//vars used for parsing a ldbn xml string with type=assignment
	private DomainTable currentDomain;
	private List<FD> currentFDs;
	private boolean isLHS;
	private String assignmentName;

	public Assignment parse(AssignmentDto dto) {
		if (dto == null) {
			return null;
		}

		Assignment assignment = parse(dto.getXml());
		if (assignment == null) {
			Log.error("Cannot parse assignment XML. AssignmentDto does not contain any XML data.");
			return null;
		}
		assignment.setName(dto.getName());
		assignment.setID(dto.getId());

		return assignment;
	}

	public Assignment parse(String xml) {
		if (xml == null) {
			return null;
		}
		clear();
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
		return assignment;
	}

	private void clear() {
		currentDomain = null;
		currentFDs = null;
		assignmentName = null;
		isLHS = false;
	}


	private Assignment parseAssignmentXML(Node ldbn) {
		currentDomain = new DomainTable();
		currentFDs = new ArrayList<>();
		visitElementNodes(ldbn);
		Assignment a = new Assignment(currentDomain, currentFDs);
		a.setName(assignmentName);
		currentDomain = null;
		currentFDs = null;
		isLHS = false;

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
}
