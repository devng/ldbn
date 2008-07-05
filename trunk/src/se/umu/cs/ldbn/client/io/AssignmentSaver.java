package se.umu.cs.ldbn.client.io;

import java.util.List;

import se.umu.cs.ldbn.client.Assignment;
import se.umu.cs.ldbn.client.Common;
import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.core.FD;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;

public final class AssignmentSaver {
	
	private AssignmentSaver() {}
	
	public static void sendToSaveScript(String xml, String name, String id) {
		RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,
			Config.get().getSaveScriptURL());
		rb.setHeader("Content-type", "application/x-www-form-urlencoded");
		String data = "id="+id+"&name="+name+"&xml="+xml;
		try {
			rb.sendRequest(data, new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					Log.error("Request Callback Error", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (Common.checkResponse(response)) {
						LdbnParser p = LdbnParser.get();
						if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.msg) {
							Window.alert(p.getMsgText());
						} else {
							Log.warn("Invalid response by the server:\n");
							Log.warn(response.getText());
						}
					}
				}
			});
		} catch (Exception e) {
			Log.error("Request failed", e);
		}

	}
	
	public static String buildXML(Assignment a) {
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
