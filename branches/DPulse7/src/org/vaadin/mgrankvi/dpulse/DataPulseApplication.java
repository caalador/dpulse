package org.vaadin.mgrankvi.dpulse;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

public class DataPulseApplication extends Application {

	private static final long serialVersionUID = -5873948681165609537L;

	@Override
	public void init() {

		final Window mainWindow = new Window("Connected");
		setMainWindow(mainWindow);

		final DataPulse connectionTester = new DataPulse();
		connectionTester.setItemsInLine(7);
		connectionTester.setPollInterval(20000);

		final Controller controller = new Controller(connectionTester);

		// connectionTester.addConnection(new
		// HtmlConnector("http://www.yesasia.com", "Yes asia"));
		// connectionTester.addConnection(new HtmlConnector("http://www.hs.fi",
		// "Helsingin sanomat"));
		// connectionTester.addConnection(new
		// HtmlConnector("http://www.amazon.co.jp", "Amazon japan"));
		// connectionTester.addConnection(new
		// HtmlConnector("http://www.kauppalehti.fi", "Kauppalehti"));
		// connectionTester.addConnection(new
		// HtmlConnector("http://docs.oracle.com/", "Java documentation"));
		// connectionTester.addConnection(new
		// HtmlConnector("http://www.ebookjapan.jp/", " EBook Japan"));

		// connectionTester.addConnection(new
		// HtmlConnector("http://localhost/DPulse/random/", "Random result 1"));
		// connectionTester.addConnection(new
		// HtmlConnector("http://mikael2.virtuallypreinstalled.com/DPulse/random/",
		// "Random result 2"));
		// connectionTester.addConnection(new
		// HtmlConnector("http://mikael2.virtuallypreinstalled.com/DPulse/random/",
		// "Random result 3"));
		// connectionTester.addConnection(new
		// HtmlConnector("http://mikael2.virtuallypreinstalled.com/DPulse/random/",
		// "Random result 4"));
		// connectionTester.addConnection(new
		// HtmlConnector("http://mikael2.virtuallypreinstalled.com/DPulse/random/",
		// "Random result 5"));
		// connectionTester.addConnection(new
		// HtmlConnector("http://mikael2.virtuallypreinstalled.com/DPulse/random/",
		// "Random result 6"));
		// connectionTester.addConnection(new
		// HtmlConnector("http://mikael2.virtuallypreinstalled.com/DPulse/random/",
		// "Random result 7"));
		// connectionTester.addConnection(new
		// HtmlConnector("http://mikael2.virtuallypreinstalled.com/DPulse/random/",
		// "Random result 8"));
		// connectionTester.addConnection(new
		// HtmlConnector("http://mikael2.virtuallypreinstalled.com/DPulse/random/",
		// "Random result 9"));

		mainWindow.addComponent(controller);
		mainWindow.addComponent(connectionTester);
		mainWindow.addComponent(getExplanation());
	}

	private Label getExplanation() {
		final Label text = new Label("", Label.CONTENT_XHTML);

		text.setValue("<b>HTML Connector</b> uses target urls of type <b><i>http://mikael2.virtuallypreinstalled.com/</i></b><br/>"
				+ "<b>ECHO connector</b> uses <b><i>mikael2.virtuallypreinstalled.com</i></b><br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<b>Note!</b> most servers don't accept connections to the echo port. (Error in echo to server.Connection refused)<br/>"
				+ "<b>Is Reachable Connector</b> also uses <b><i>mikael2.virtuallypreinstalled.com</i></b> and has a better chance of not being blocked.");

		return text;
	}
}
