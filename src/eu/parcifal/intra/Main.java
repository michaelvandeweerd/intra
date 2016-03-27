package eu.parcifal.intra;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

public class Main {

    private final static java.io.File CONFIGURATION_FILE = new java.io.File("./cfg/intra.xml");

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            FileInputStream stream = new FileInputStream(CONFIGURATION_FILE);

            Document document = builder.parse(stream);

            document.normalize();

            NodeList listeners = document.getElementsByTagName("listener");

            for (int i = 0; i < listeners.getLength(); i++) {
                Element listener = (Element) listeners.item(i);

                new Thread(Main.compileListener(listener)).start();
            }
        } catch (Exception exception) {
            Console.warn(exception);
        }
    }

    private static HTTPListener compileListener(Element listener) {
        int port = Integer.parseInt(listener.getAttribute("port"));

        NodeList hosts = listener.getElementsByTagName("host");

        Router hostRouter = Router.EMPTY;

        for (int i = 0; i < hosts.getLength(); i++) {
            Element host = (Element) hosts.item(i);
            String name = host.getAttribute("name");

            hostRouter.add(new Route(Main.compileHost(host), name));
        }

        return new HTTPListener(port, hostRouter);
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
