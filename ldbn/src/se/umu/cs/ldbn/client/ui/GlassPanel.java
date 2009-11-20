package se.umu.cs.ldbn.client.ui;

import se.umu.cs.ldbn.client.Main;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
* This panel is positioned absolutely and covers the main panel area.
* 
* @see se.umu.cs.ldbn.client.Main  
*/
public class GlassPanel extends Composite {

   private HorizontalPanel panel;
   private AbsolutePanel parent;
   private Image loading;

   public GlassPanel(AbsolutePanel parent){
	   super();
	   panel = new HorizontalPanel();
	   panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	   loading = new Image("img/loading.gif");
	   panel.add(loading);
       initWidget(panel);
       panel.setSize(Main.WIDTH_PX+"px", "10000px");
       this.parent = parent;
       //this.parent = RootPanel.get();
       panel.setStylePrimaryName("glassPanel");
   }
   
   public void setLoadingAnimationNextTime(boolean useAnimation) {
	   loading.setVisible(useAnimation);
   }

   public void show(){
       // Override the styles explicitly, because it's needed every
	   // time the widget is detached
       DOM.setStyleAttribute(panel.getElement(), "left", "0");
       DOM.setStyleAttribute(panel.getElement(), "top", "0");
       DOM.setStyleAttribute(panel.getElement(), "position", "absolute");

       parent.add(this);
   }

   public void hide(){
	   loading.setVisible(false);
       parent.remove(this);
   }
} 
