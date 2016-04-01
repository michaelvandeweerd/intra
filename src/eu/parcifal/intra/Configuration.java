package eu.parcifal.intra;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import eu.parcifal.intra.content.Content;
import eu.parcifal.intra.content.File;
import eu.parcifal.intra.content.Page;
import eu.parcifal.intra.content.Redirection;
import eu.parcifal.intra.content.Script;
import eu.parcifal.intra.http.HTTPListener;
import eu.parcifal.plus.logic.Route;
import eu.parcifal.plus.logic.Router;
import eu.parcifal.plus.print.Console;

public class Configuration {

    private final static String DEFAULT_LOCATION = "./intra.xml";

    private static String LOCATION;

    private static Document DOCUMENT;

    private static XPath XPATH;
    
    public Configuration() {
        Configuration.LOCATION = Configuration.DEFAULT_LOCATION;
    }

    public void initialise() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            FileInputStream stream = new FileInputStream(LOCATION);

            DOCUMENT = builder.parse(stream);

            DOCUMENT.normalize();

            XPATH = XPathFactory.newInstance().newXPath();
        } catch (Exception exception) {
            Console.warn(exception);
        }
    }

    public HTTPListener[] listeners() {
        NodeList listenerNodes = null;

        try {
            listenerNodes = (NodeList) XPATH.evaluate("/server/exchangers/listener", DOCUMENT, XPathConstants.NODESET);
        } catch (XPathExpressionException exception) {
            Console.warn(exception);
        }

        HTTPListener[] listeners = new HTTPListener[listenerNodes.getLength()];

        for (int i = 0; i < listenerNodes.getLength(); i++) {
            listeners[i] = compileListener((Element) listenerNodes.item(i));
        }

        return listeners;
    }

    private static HTTPListener compileListener(Element listener) {
        int port = Integer.parseInt(listener.getAttribute("port"));

        NodeList hosts = listener.getElementsByTagName("host");

        Router hostRouter = new Router();

        for (int i = 0; i < hosts.getLength(); i++) {
            Element host = (Element) hosts.item(i);
            String name = host.getAttribute("name");

            hostRouter.add(new Route(compileHost(host), name));
        }

        return new HTTPListener(port, hostRouter);
    }

    private static Host compileHost(Element host) {
        NodeList contextList = host.getElementsByTagName("context");
        NodeList statusList = host.getElementsByTagName("status");

        Router contextRouter = new Router();
        Router statusRouter = new Router();

        for (int i = 0; i < contextList.getLength(); i++) {
            if (contextList.item(i) != null) {
                Element context = (Element) contextList.item(i);
                String path = context.getAttribute("path");

                contextRouter.add(new Route(compileContent((Element) contextList.item(i)), path));
            }
        }

        for (int i = 0; i < statusList.getLength(); i++) {
            if (statusList.item(i) != null) {
                Element status = (Element) statusList.item(i);
                String code = status.getAttribute("code");

                statusRouter.add(new Route(compileContent((Element) statusList.item(i)), code));
            }
        }

        return new Host(contextRouter, statusRouter);
    }

    private static Content compileContent(Element content) {
        String root = content.getAttribute("root");

        if (content.getElementsByTagName("page").getLength() != 0) {
            Element page = (Element) content.getElementsByTagName("page").item(0);

            String pageRoot = page.getAttribute("root");
            String location = page.getAttribute("location");

            return new Page(root + pageRoot + location);
        } else if (content.getElementsByTagName("file").getLength() != 0) {
            Element file = (Element) content.getElementsByTagName("file").item(0);

            String fileRoot = file.getAttribute("root");
            String location = file.getAttribute("location");

            return new File(root + fileRoot + location);
        } else if (content.getElementsByTagName("script").getLength() != 0) {
            Element script = (Element) content.getElementsByTagName("script").item(0);

            String scriptRoot = script.getAttributes().getNamedItem("root").getTextContent();
            String location = script.getAttributes().getNamedItem("location").getTextContent();

            return new Script(root + scriptRoot + location);
        } else if (content.getElementsByTagName("redirection").getLength() != 0) {
            Element redirection = (Element) content.getElementsByTagName("redirection").item(0);

            String target = redirection.getAttribute("target");
            boolean temporary = Boolean.getBoolean(redirection.getAttribute("temporary"));

            return new Redirection(target, temporary);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
