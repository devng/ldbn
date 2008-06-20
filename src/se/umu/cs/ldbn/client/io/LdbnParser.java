package se.umu.cs.ldbn.client.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.umu.cs.ldbn.client.Assignment;
import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.core.FD;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;

public final class LdbnParser {
	
	public static enum LDBN_TYPE {assignment, msg, assignment_list, unknown};
	
	public static enum MSG_TYPE {ok, warn, error, unknown};
	
	private static LdbnParser inst = null;
	
	private LDBN_TYPE lastLdbnType;
	
	//vars used for parsing a ldbn xml string with type=assignment
	private DomainTable currentDomain;
	private List<FD> currentFDs;
	private boolean isLHS;
	private Assignment assignment;
	//vars used for parsing a ldbn xml string with type=msg
	//note such xml string can only contain one message
	private MSG_TYPE lastMsgType;
	private String lastMsg;
	//vars used for parsing a ldbn xml string with type assignment_list
	//map format: assignment id in the DB -> name
	private Map<String, String> assignmentList;
	
	private LdbnParser() {
		lastLdbnType = LDBN_TYPE.unknown;
		lastMsgType = MSG_TYPE.unknown;
	}
	
	public static LdbnParser get() {
		if(inst == null) {
			inst = new LdbnParser();
		}
		return inst;
	}
	
	
	public LDBN_TYPE getLastLdbnType() {
		return lastLdbnType;
	}
	
	public LDBN_TYPE parse(String xml) {
		clear();
		Document doc;
		try {
			doc = XMLParser.parse(xml);
		} catch (DOMParseException e) {
			
			return LDBN_TYPE.unknown;
		}
		
		Node ldbn = doc.getElementsByTagName("ldbn").item(0);
		if(ldbn == null) return LDBN_TYPE.unknown;
		String type = ((Element) ldbn).getAttribute("type");
		if (type == null) return LDBN_TYPE.unknown;
		if (type.equals(LDBN_TYPE.assignment.toString())) {
			lastLdbnType = LDBN_TYPE.assignment;
			assignment = parseAssignmentXML(ldbn);
		} else if (type.equals(LDBN_TYPE.msg.toString())) {
			lastLdbnType = LDBN_TYPE.msg;
			visitElementNodes(ldbn);
		} else if (type.equals(LDBN_TYPE.assignment_list.toString())) {
			lastLdbnType = LDBN_TYPE.assignment_list;
			assignmentList = new HashMap<String, String>();
			visitElementNodes(ldbn);
		} else {
			return LDBN_TYPE.unknown;
		}
		
		
		return lastLdbnType;
	}
	
	public MSG_TYPE getMsgType() {
		return lastMsgType;
	}
	
	public String getMsgText() {
		return lastMsg;
	}
	
	public Map<String, String> getAssignmentList() {
		return assignmentList;
	}
	
	public Assignment getAssignment() {
		return assignment;
	}
	
	public void clear() {
		lastLdbnType = LDBN_TYPE.unknown;
		lastMsgType = MSG_TYPE.unknown;
		currentDomain = null;
		currentFDs = null;
		isLHS = false;
		assignment = null;
		assignmentList = null;
		lastMsg = null;
	}
	
	
	private Assignment parseAssignmentXML(Node ldbn) {
		currentDomain = new DomainTable();
		currentFDs = new ArrayList<FD>();
		visitElementNodes(ldbn);
		Assignment a = new Assignment(currentDomain, currentFDs);
		currentDomain = null;
		currentFDs = null;
		isLHS = false;
		return a;
	}

	private void visitElementNodes(Node node) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element el = (Element) child;
			String tag = el.getTagName();
			if (tag.equals("msg")) {
				String type = el.getAttribute("type");
				if(type == null) {
					lastMsgType = MSG_TYPE.unknown;
				} else if (type.equals("error")) {
					lastMsgType = MSG_TYPE.error;
				} else if (type.equals("warn")) {
					lastMsgType = MSG_TYPE.warn;
				} else if (type.equals("ok")) {
					lastMsgType = MSG_TYPE.ok;
				} else {
					lastMsgType = MSG_TYPE.unknown;
				}
				String val = el.getLastChild().getNodeValue();
				if(val == null) {
					lastMsg = "";
				} else {
					lastMsg = val;
				}
			} else if (tag.equals("entry")) {
				String id = el.getAttribute("id");
				String name = el.getAttribute("name");
				if(name != null && id != null) {
					assignmentList.put(id, name);
				}
			} else if (tag.equals("att")) { 
				//TODO There can be a  null pointer exception, if the user
				//is trying to parse ldbn of type msg with an att tag in it,
				//which is not a valid ldbn xml, but we cannot control, what is
				//being passed from the server side script
				String val = el.getLastChild().getNodeValue();
				currentDomain.addAtt(val);
			} else if (tag.equals("fd")) {
				FD fd = new FD(currentDomain);
				currentFDs.add(fd);
			} else if (tag.equals("lhs")) {
				isLHS = true;
			} else if (tag.equals("rhs")) {
				isLHS = false;
			} else if (tag.equals("fdatt")) {
				String val = el.getLastChild().getNodeValue();
				FD fd = currentFDs.get(currentFDs.size() - 1);
				AttributeSet fdatt;
				if (isLHS) {
					fdatt = fd.getLHS();
				} else {
					fdatt = fd.getRHS();
				}
				fdatt.addAtt(val);
			}
			visitElementNodes(child);
		}
	}
}
