package se.umu.cs.ldbn.client.i18n;

import se.umu.cs.ldbn.client.ClientInjector;

public final class I18N {

	public static I18NConstants constants() {
		return ClientInjector.INSTANCE.getI18NConstants();
	}

	public static I18NMessages messages() {
		return ClientInjector.INSTANCE.getI18NMessages();
	}

}
