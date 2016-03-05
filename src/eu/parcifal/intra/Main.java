package eu.parcifal.intra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.parcifal.intra.content.Content;
import eu.parcifal.intra.content.Corpus;
import eu.parcifal.intra.content.Pagina;
import eu.parcifal.intra.content.Redirection;
import eu.parcifal.intra.http.HTTPListener;
import eu.parcifal.plus.logic.Route;
import eu.parcifal.plus.logic.Router;

public class Main {

	private final static File CONFIGURATION_FILE = new File("./intra.xml");

	public static void main(String[] args) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			factory.setIgnoringElementContentWhitespace(true);

			DocumentBuilder builder = factory.newDocumentBuilder();

			FileInputStream stream = new FileInputStream(CONFIGURATION_FILE);

			Document document = builder.parse(stream);

			document.normalize();

			NodeList configuration = document.getElementsByTagName("host");

			Router hosts = Router.EMPTY;

			for (int i = 0; i < configuration.getLength(); i++) {
				Element host = (Element) configuration.item(i);
				String name = host.getAttribute("name");

				hosts.add(new Route(Main.compileHost(host), name));
			}

			new Thread(new HTTPListener(hosts)).start();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Host compileHost(Element host) {
		NodeList contextList = host.getElementsByTagName("context");
		NodeList statusList = host.getElementsByTagName("status");

		Router contextRouter = Router.EMPTY;
		Router statusRouter = Router.EMPTY;

		for (int i = 0; i < contextList.getLength(); i++) {
			if (contextList.item(i) != null) {
				Element context = (Element) contextList.item(i);
				String path = context.getAttribute("path");

				contextRouter.add(new Route(Main.compileContent((Element) contextList.item(i)), path));
			}
		}

		for (int i = 0; i < statusList.getLength(); i++) {
			if (statusList.item(i) != null) {
				Element status = (Element) statusList.item(i);
				String code = status.getAttribute("code");

				statusRouter.add(new Route(Main.compileContent((Element) statusList.item(i)), code));
			}
		}

		return new Host(contextRouter, statusRouter);
	}

	private static Content compileContent(Element content) {
		String root = content.getAttribute("root");

		Element type = null;

		if (content.getElementsByTagName("page").getLength() != 0) {
			type = (Element) content.getElementsByTagName("page").item(0);
		} else if (content.getElementsByTagName("file").getLength() != 0) {
			type = (Element) content.getElementsByTagName("file").item(0);
		} else if (content.getElementsByTagName("script").getLength() != 0) {
			type = (Element) content.getElementsByTagName("script").item(0);
		} else if (content.getElementsByTagName("redirection").getLength() != 0) {
			type = (Element) content.getElementsByTagName("redirection").item(0);
		} else {
			throw new IllegalArgumentException();
		}

		switch (type.getTagName()) {
		case "page": {
			String pageRoot = type.getAttribute("root");
			String location = type.getAttribute("location");

			return new Pagina(root + pageRoot + location);
		}
		case "file": {
			String fileRoot = type.getAttribute("root");
			String location = type.getAttribute("location");

			return new Corpus(root + fileRoot + location);
		}
		case "script": {
			// String scriptRoot =
			// type.getAttributes().getNamedItem("root").getTextContent();
			// String location =
			// type.getAttributes().getNamedItem("location").getTextContent();

			// context = new Script(root + scriptRoot +
			// location);
			break;
		}
		case "redirection": {
			String target = type.getAttribute("target");
			boolean temporary = Boolean.getBoolean(type.getAttribute("temporary"));

			return new Redirection(target, temporary);
		}
		}

		throw new IllegalArgumentException();
	}

}
