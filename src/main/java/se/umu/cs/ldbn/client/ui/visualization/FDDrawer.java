package se.umu.cs.ldbn.client.ui.visualization;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.i18n.I18N;
import se.umu.cs.ldbn.client.ui.visualization.color.ColorPalette;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

final class FDDrawer extends AbsolutePanel {

	private final static int VA_MARGIN = 4;

	private List<VisualAttribute> vatts;
	//private AttributeSet curAtts;
	private List<FD> curFds;
	private ColorPalette curPalette;
	private int curZoom;
	private Canvas canvas;
	private Context2d canvasContext;
	private boolean isType2Diagram;
	private boolean useForwardOrder;
	private boolean isCanvasSupported;
	private int curFDHeight;

	public FDDrawer() {
		super();
		isCanvasSupported = Canvas.isSupported();
		if (!isCanvasSupported) {
			this.add(new Label(I18N.constants().canvasNotSupported()));
		}
		DOM.setStyleAttribute(this.getElement(), "background", "white");
	}


	public void drawFDs(AttributeSet atts, List<FD> fds, ColorPalette cp,
			int zoomLevel, boolean isType2Diagram, boolean useForwardOrder) {
		if (!isCanvasSupported) {
			return;
		}
		this.clear();
		//curAtts = atts;
		curFds = fds;
		curPalette = cp;
		curZoom = zoomLevel;
		this.isType2Diagram = isType2Diagram;
		this.useForwardOrder = useForwardOrder;
		curFDHeight = (14 + 10 * curZoom);

		DomainTable domain = atts.domain();
		List<String> names = atts.getAttributeNames();
		vatts = new ArrayList<>(names.size());
		HorizontalPanel hp = new HorizontalPanel();

		for (String name : names) {
			int mask = domain.getAttIndex(name);
			VisualAttribute va = new VisualAttribute(name, mask);
			va.setZoomLevel(zoomLevel);
			hp.add(va);
			vatts.add(va);
		}
		this.add(hp, 10, 10);
		//calls initSizeAndCanvas after all VisualAttributes are attached
		Scheduler.get().scheduleDeferred(new Command() {
			public void execute() {
				initSizeAndCanvas();
				drawCanvas();
			}
		});
	}

	public void initSizeAndCanvas() {
		if (!isCanvasSupported) {
			return;
		}
		int totalWidth = VA_MARGIN; //margin
		int attHeight = 0;
		for (VisualAttribute va : vatts) {
			int h = va.getOffsetHeight();
			int w = va.getOffsetWidth();
			if(isType2Diagram) {
				int pos = totalWidth + (w >> 1); // /2
				va.setPositionArrowIn(pos);
				va.setPositionArrowOut(pos);
			} else {
				va.setPositionArrowIn(totalWidth + (w >> 1) + 7);
				va.setPositionArrowOut(totalWidth + (w >> 1) - 7);
			}


			totalWidth += w + (VA_MARGIN << 1); // * 2
			attHeight = h > attHeight ? h : attHeight;
		}
		attHeight += (VA_MARGIN  << 1);
		int toalHeight = attHeight + (25 + 10 * curZoom) + curFds.size() * curFDHeight;
		this.setPixelSize(totalWidth + 20, toalHeight);
		this.canvas = Canvas.createIfSupported();
		canvas.setCoordinateSpaceHeight(toalHeight);
		canvas.setCoordinateSpaceWidth(totalWidth);
		canvasContext = canvas.getContext2d();
		this.add(canvas, 10, 10+attHeight);
		if(curZoom > 2) {
			canvasContext.setLineWidth(2);
		}
	}

	public void drawCanvas() {
		if (!isCanvasSupported) {
			return;
		}
		CssColor color;
		int curH = (25 + 10 * curZoom) + (curFds.size()-1) * curFDHeight;
		if(isType2Diagram) {
			curH -= 15;
		}

		for (int i = useForwardOrder ? curFds.size() - 1 : 0;
			useForwardOrder ? i >= 0 : i < curFds.size(); ) {

			color  = curPalette.nextColor();
			canvasContext.setFillStyle(color);
			canvasContext.setStrokeStyle(color);
			FD fd = curFds.get(i);
			AttributeSet lhs = fd.getLHS();
			AttributeSet rhs = fd.getRHS();
			int wMin = Integer.MAX_VALUE;
			int wMax = Integer.MIN_VALUE;
			for (VisualAttribute va : vatts) {
				if(lhs.containsAtt(va.getAttIndex())) {
					wMin = Math.min(wMin, va.getPositionArrowOut());
					wMax = Math.max(wMax, va.getPositionArrowOut());
					if (isType2Diagram) {
						drawLine(va.getPositionArrowOut(), curH-curFDHeight+4, va.getPositionArrowOut(), curH);
					} else {
						drawLine(va.getPositionArrowOut(), 0, va.getPositionArrowOut(), curH);
					}

				}
				if(rhs.containsAtt(va.getAttIndex())) {
					wMin = Math.min(wMin, va.getPositionArrowIn());
					wMax = Math.max(wMax, va.getPositionArrowIn());
					if (isType2Diagram) {
						drawLine(va.getPositionArrowIn(), curH-curFDHeight+4, va.getPositionArrowIn(), curH);
						drawTriangle(va.getPositionArrowIn(), curH-curFDHeight+4);
					} else {
						drawLine(va.getPositionArrowIn(), 0, va.getPositionArrowIn(), curH);
						drawTriangle(va.getPositionArrowIn(), 0);

					}

				}
			}
			if (isType2Diagram) {
				drawLine(wMin, curH, wMax, curH);
			} else {
				drawConnectionLine(wMin, wMax, curH, fd);
			}

			for (VisualAttribute va : vatts) {
				if(lhs.containsAtt(va.getAttIndex())) {
					va.setHasOutgoingArrow(true);
					va.setLastOutcommingColor(color);
				}
				if(rhs.containsAtt(va.getAttIndex())) {
					va.setHasIncommingArrow(true);
					va.setLastIncommingColor(color);
				}
			}
			curH -= curFDHeight;
			if (useForwardOrder) i--; else i++;
		}
	}

	public void reDrawCanvas() {
		if (!isCanvasSupported) {
			return;
		}
		curPalette.reset();
		for (VisualAttribute va : vatts) {
			va.setHasIncommingArrow(false);
			va.setHasOutgoingArrow(false);
		}
		canvasContext.clearRect(0, 0,
			canvas.getCoordinateSpaceWidth(),
			canvas.getCoordinateSpaceHeight());
		drawCanvas();
	}

	private void drawLine(double x1, double y1, double x2, double y2) {
		canvasContext.beginPath();
		canvasContext.moveTo(x1+0.1, y1+0.1);
		canvasContext.lineTo(x2+0.1, y2+0.1);
		canvasContext.stroke();
	}

	private void drawTriangle(double x, double y) {
		x += 0.1; y += 0.1;
		canvasContext.save();
		canvasContext.setLineWidth(1);
		canvasContext.beginPath();
		canvasContext.moveTo(x, y);
		canvasContext.lineTo(x+5, y+10);
		canvasContext.lineTo(x-5, y+10);
		canvasContext.closePath();
		canvasContext.stroke();
		canvasContext.fill();
		canvasContext.restore();
	}

	private void drawConnectionLine(int x1, int x2, int h, FD f) {
		for (VisualAttribute va : vatts) { //must be sorted by position
			if (va.hasOutgoingArrow()) {
				int p = va.getPositionArrowOut();
				if (f.getLHS().containsAtt(va.getAttIndex())) {
					CssColor c2 = va.getLastOutcommingColor();
					drawConnection(p, h, c2);
				} else if (x1 < p && p < x2) {
					drawLine(x1, h, p - 4, h);
					drawArc(p, h);
					x1 = p + 4;
				}
			}
			if (va.hasIncommingArrow()) {
				int p = va.getPositionArrowIn();
				if (f.getRHS().containsAtt(va.getAttIndex())) {
					canvasContext.save();
					CssColor color = va.getLastIncommingColor();
					canvasContext.setFillStyle(color);
					canvasContext.setStrokeStyle(color);
					drawTriangle(p, h + 1);
					canvasContext.restore();
				} else {
					if (x1 < p && p < x2) {
						drawLine(x1, h, p - 4, h);
						drawArc(p, h);
						x1 = p + 4;
					}
				}
			}
		}
		drawLine(x1, h, x2, h);
	}

	private void drawArc (double x, double y) {
		canvasContext.beginPath();
		canvasContext.arc(x+0.1, y+0.1, 4, Math.PI, 0, false);
		canvasContext.stroke();
	}

	private void drawConnection (double x, double y, CssColor c2) {
		x += 0.1; y += 0.1;
		canvasContext.save();
		canvasContext.setFillStyle(c2);
		canvasContext.setStrokeStyle(c2);
		canvasContext.beginPath();
		canvasContext.arc(x, y, 3, 0, Math.PI, false);
		canvasContext.closePath();
		canvasContext.stroke();
		canvasContext.fill();
		canvasContext.restore();

		canvasContext.beginPath();
		canvasContext.arc(x, y, 3, Math.PI, 0, false);
		canvasContext.closePath();
		canvasContext.stroke();
		canvasContext.fill();
	}
}
