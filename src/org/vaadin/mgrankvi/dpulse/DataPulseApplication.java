package org.vaadin.mgrankvi.dpulse;

import org.vaadin.mgrankvi.dpulse.data.connector.HtmlConnector;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class DataPulseApplication extends Application {

	private static final long serialVersionUID = -5873948681165609537L;

	@Override
	public void init() {
		

		final Window mainWindow = new Window("Connected");
		setMainWindow(mainWindow);

		final DataPulse connectionTester = new DataPulse();
		connectionTester.setItemsInLine(7);
		connectionTester.setPollInterval(5000);

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

		connectionTester.addConnection(new HtmlConnector("http://localhost:8080/DPulse/random/", "Random result"));
		connectionTester.addConnection(new HtmlConnector("http://localhost:8080/DPulse/random/", "Random result"));
		connectionTester.addConnection(new HtmlConnector("http://localhost:8080/DPulse/random/", "Random result"));
		connectionTester.addConnection(new HtmlConnector("http://localhost:8080/DPulse/random/", "Random result"));
		connectionTester.addConnection(new HtmlConnector("http://localhost:8080/DPulse/random/", "Random result"));
		connectionTester.addConnection(new HtmlConnector("http://localhost:8080/DPulse/random/", "Random result"));
		connectionTester.addConnection(new HtmlConnector("http://localhost:8080/DPulse/random/", "Random result"));
		connectionTester.addConnection(new HtmlConnector("http://localhost:8080/DPulse/random/", "Random result"));
		connectionTester.addConnection(new HtmlConnector("http://localhost:8080/DPulse/random/", "Random result"));

		mainWindow.addComponent(controller);
		mainWindow.addComponent(connectionTester);
	}
}
