package hu.oe.nik.szfmv.automatedcar.visualization;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Utils {

    public static Map<String, Point> LoadReferencePointsFromXml(String URI) throws ParserConfigurationException, IOException, SAXException {
        Map<String,Point> referencesP=new HashMap<>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(URI);

        NodeList nodes = document.getElementsByTagName("Image");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element image = (Element) nodes.item(i);
            String imageName = image.getAttribute("name");
            Element refPoint = (Element) image.getChildNodes().item(1);

            int x = Integer.parseInt(refPoint.getAttribute("x"));
            int y = Integer.parseInt(refPoint.getAttribute("y"));
            Point p = new Point(x, y);

            referencesP.put(imageName+".png", p);
        }

        return referencesP;
    }
}
