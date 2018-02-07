package se.umu.cs.ldbn.client.io;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.utils.Common;
import se.umu.cs.ldbn.shared.core.Assignment;
import se.umu.cs.ldbn.shared.core.AttributeSet;
import se.umu.cs.ldbn.shared.core.DomainTable;
import se.umu.cs.ldbn.shared.core.FD;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;
import se.umu.cs.ldbn.shared.dto.AssignmentDto;

public final class LdbnParser {

	public static enum LDBN_TYPE {assignment, msg, assignment_list, session,
		comment, user_list, unknown};

	public static enum MSG_TYPE {ok, warn, error, unknown};

	private static LdbnParser inst = null;

	public static LdbnParser get() {
		if(inst == null) {
			inst = new LdbnParser();
		}
		return inst;
	}

	private LDBN_TYPE lastLdbnType;
	//vars used for parsing a ldbn xml string with type=assignment
	private DomainTable currentDomain;
	private List<FD> currentFDs;
	private boolean isLHS;
	private String assignmentName;
	private Assignment assignment;
	//vars used for parsing a ldbn xml string with type=msg
	//note such xml string can only contain one message
	private MSG_TYPE lastMsgType;
	private String lastMsg;

	//vars used for parsing a ldbn xml string with type assignment_list
	//map format: assignment id in the DB -> name
	private List<AssignmentDto> assignmentList;

	private List<UserListEntry> userList;

	//vars used for session
	private Integer userid;
	private boolean isAdmin;
	private boolean isSuperUser;
	private String sessionid;
	private String email;
	//vars used for comments
	private List<CommentListEntry> comments;
	//comments for which assignment
	private Integer assignmentIDComent;

	private LdbnParser() {
		lastLdbnType = LDBN_TYPE.unknown;
		lastMsgType = MSG_TYPE.unknown;
	}


	public void clear() {
		lastLdbnType = LDBN_TYPE.unknown;
		lastMsgType = MSG_TYPE.unknown;
		currentDomain = null;
		currentFDs = null;
		assignmentName = null;
		isLHS = false;
		assignment = null;
		assignmentList = null;
		userList = null;
		lastMsg = null;
		sessionid = null;
		userid = null;
		isAdmin = false;
		isSuperUser = false;
		email = null;
		assignmentIDComent = null;
		comments = null;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public List<AssignmentDto> getAssignmentList() {
		return assignmentList;
	}

	public List<UserListEntry> getUserList() {
		return userList;
	}

	public LDBN_TYPE getLastLdbnType() {
		return lastLdbnType;
	}

	public String getMsgText() {
		String msg = lastMsg.replaceAll("\\n", "\n");
		msg = msg.replaceAll("<BR>", "\n");
		msg = msg.replaceAll("<br>", "\n");
		return msg;
	}

	public MSG_TYPE getMsgType() {
		return lastMsgType;
	}

	public String getSessionId() {
		return sessionid;
	}

	public Integer getUserId() {
		return userid;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public boolean isSuperUser() {
		return isSuperUser;
	}

	public String getEmail() {
		return email;
	}

	public List<CommentListEntry> getComments() {
		return comments;
	}


	public Integer getAssignmentIDComent() {
		return assignmentIDComent;
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
			assignmentName = ((Element) ldbn).getAttribute("name");
			assignment = parseAssignmentXML(ldbn);
		} else if (type.equals(LDBN_TYPE.msg.toString())) {
			lastLdbnType = LDBN_TYPE.msg;
			visitElementNodes(ldbn);
		} else if (type.equals(LDBN_TYPE.assignment_list.toString())) {
			lastLdbnType = LDBN_TYPE.assignment_list;
			assignmentList = new ArrayList<>() ;
			visitElementNodes(ldbn);
		} else if (type.equals(LDBN_TYPE.user_list.toString())) {
			lastLdbnType = LDBN_TYPE.user_list;
			userList = new ArrayList<>() ;
			visitElementNodes(ldbn);
		} else if (type.equals(LDBN_TYPE.session.toString())) {
			lastLdbnType = LDBN_TYPE.session;
			visitElementNodes(ldbn);
		} else if (type.equals("comments_list")) {
			lastLdbnType = LDBN_TYPE.comment;
			comments = new ArrayList<>();
			assignmentIDComent = Integer.valueOf( ((Element) ldbn).getAttribute("assignment_id"));
			visitElementNodes(ldbn);
		}else {
			return LDBN_TYPE.unknown;
		}


		return lastLdbnType;
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
			} else if (tag.equals("user_entry")) {
				String id = el.getAttribute("id");
				String name = el.getAttribute("name");
				String isAdminString = el.getAttribute("is_admin");
				boolean isAdmin = false;
				if (isAdminString != null) {
					isAdmin = isAdminString.toLowerCase().equals("1");
				} else {
					System.out.println("isAdmin is null.");
				}
				String isSUnString = el.getAttribute("is_su");
				boolean isSU = false;
				if (isSUnString != null) {
					isSU = isSUnString.toLowerCase().equals("1");
				} else {
					System.out.println("isSU is null.");
				}
				UserListEntry data =
					new UserListEntry(id, name, isAdmin, isSU);
				if(name != null && id != null) {
					userList.add(data);
				}
			} else if (tag.equals("att")) {
				//TODO There can be a  null pointer exception, if the model
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
			} else if (tag.equals("session")) {
				sessionid = el.getAttribute("id");
			} else if (tag.equals("model")) {
				userid = Integer.valueOf(el.getAttribute("id"));
				isAdmin = "1".equals(el.getAttribute("is_admin"));
				isSuperUser = "1".equals(el.getAttribute("is_su"));
			} else if (tag.equals("email")) {
				email = el.getAttribute("val");
			} else if (tag.equals("comment")) {
				CommentListEntry cle = new CommentListEntry(
						el.getAttribute("id"),
						el.getAttribute("author_id"),
						el.getAttribute("author"),
						el.getAttribute("last_modified"),
						null);
				Node lastChild = el.getLastChild();
				if(lastChild != null) {
					String val = lastChild.getNodeValue().trim();
					val = val.replaceAll("\\s", "+");
					val = Common.base64decode(val);
					val = Common.escapeHTMLCharacters(val);
					cle.setCommentString(val);
					comments.add(cle);
				}
			}
			visitElementNodes(child);
		}
	}

}
