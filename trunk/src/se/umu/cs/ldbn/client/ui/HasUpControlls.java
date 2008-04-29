package se.umu.cs.ldbn.client.ui;

/**
 * This interface is used by the DisclosureWidget. If a widget implements this
 * interface the DisclosureWidget will automatically add the widgets returned 
 * by the getUpControlls() method in the upper right corner of the 
 * DisclosureWidget. Note that this is only true for the widget, which is 
 * passed as an argument to the DisclosureWidget constructor. 
 * 
 * @author Nikolay Georgiev (ens07ngv@cs.umu.se)
 *
 */
public interface HasUpControlls {
	/**
	 * Returns controls for a specific widget which will be displayed in the 
	 * upper right corner of a DisclosureWidget.
	 * 
	 * @return controls for a specific widget which will be displayed in the 
	 * upper right corner of a DisclosureWidget.
	 */
	public com.google.gwt.user.client.ui.Widget[] getUpControlls();
}
