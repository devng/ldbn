package se.umu.cs.ldbn.client.i18n;

import com.google.gwt.core.client.GWT;

public class I18N {

	private static I18N _inst;
	
	private LDBNConstants constants;
	private LDBNMessages messages;
	
	private I18N() {
		constants = GWT.create(LDBNConstants.class);
		messages = GWT.create(LDBNMessages.class);
	}
	
	public static I18N get() {
		if (_inst == null) {
			_inst = new I18N();
		}
		return _inst;
	}
 	
	public static LDBNConstants constants() {
		return I18N.get().constants;
	}
	
	public static LDBNMessages messages() {
		return I18N.get().messages;
	}
	
}
