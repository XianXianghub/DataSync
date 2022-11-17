package com.meferi.sync;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilderFactory;

/* loaded from: classes2.dex */
public class PropertyMap {
    public static LinkedHashMap<String, String> initEmptyProperty(String str) {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        try {
            NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(MainApplication.getInstance().getAssets().open(str)).getDocumentElement().getElementsByTagName("property");
            for (int i = 0; i < elementsByTagName.getLength(); i++) {
                linkedHashMap.put(((Element) elementsByTagName.item(i)).getAttribute("id"), "\u001d");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linkedHashMap;
    }

    public static PropertiesBean initProperties(String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        try {
            NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(MainApplication.getInstance().getAssets().open(str)).getDocumentElement().getElementsByTagName("property");
            for (int i = 0; i < elementsByTagName.getLength(); i++) {
                Element element = (Element) elementsByTagName.item(i);
                linkedHashMap.put(element.getAttribute("id"), element.getAttribute("name"));
                linkedHashMap2.put(element.getAttribute("id"), "\u001d");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PropertiesBean(linkedHashMap, linkedHashMap2);
    }

    public static LinkedHashMap<String, String> initProperty(String str, String str2) {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        try {
            NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(MainApplication.getInstance().getAssets().open(str)).getDocumentElement().getElementsByTagName(str2);
            for (int i = 0; i < elementsByTagName.getLength(); i++) {
                Element element = (Element) elementsByTagName.item(i);
                linkedHashMap.put(element.getAttribute("id"), element.getAttribute("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linkedHashMap;
    }
}
