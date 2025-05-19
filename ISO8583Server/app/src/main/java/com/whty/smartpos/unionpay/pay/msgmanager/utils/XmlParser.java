
package com.whty.smartpos.unionpay.pay.msgmanager.utils;

import com.whty.smartpos.unionpay.pay.msgmanager.model.ParseElement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class XmlParser {

    public List<?> parseXML(InputStream is)
            throws ParserConfigurationException, SAXException, IOException {

        //得到 DOM 解析器的工厂实例   
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //从 DOM 工厂获得 DOM 解析器
        DocumentBuilder builder = factory.newDocumentBuilder();

//      一般来说，要把把要解析的 XML文档转化为输入流，以便 DOM 解析器解析它
//      InputStream is= new  FileInputStream("test1.xml");    

        //解析 XML 文档的输入流，得到一个 Document
        Document doc = builder.parse(is);
        //得到 XML 文档的根节点
        Element rootEl = doc.getDocumentElement();
        List<Object> list = new ArrayList<Object>();
        String type = rootEl.getAttribute("type");
        processParse(rootEl, list);

        // end

        return list;
    }

    private void processParse(Element rootEl, List<Object> list) {
        NodeList items = rootEl.getChildNodes();
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);
            if (item.getNodeName().equals("#text"))
                continue;
            ParseElement pe = parsePeElement(item);
            list.add(pe);
        }

    }

    /**
     * 递归查找元素
     *
     * @param item
     * @return
     */
    private ParseElement parsePeElement(Node item) {
        Element elt = (Element) item;
        NodeList props = elt.getChildNodes();
        ParseElement pe = new ParseElement();
        for (int j = 0; j < props.getLength(); j++) {
            Node property = props.item(j);
            String nodeName = property.getNodeName();
            if (nodeName.equals("#text"))
                continue;

            if (nodeName.equals("id")) {
                pe.setId(property.getFirstChild().getNodeValue());
            }

            if (nodeName.equals("len")) {
                pe.setLen(property.getFirstChild().getNodeValue());
            }

            if (nodeName.equals("tag")) {
                pe.setTag(property.getFirstChild().getNodeValue());
            }

            if (nodeName.equals("lllvar")) {
                pe.setLllvar(property.getFirstChild().getNodeValue());
            }

            if (nodeName.equals("code")) {
                pe.setCode(property.getFirstChild().getNodeValue());
            }

            if (nodeName.equals("comments")) {
                pe.setComments(property.getFirstChild().getNodeValue());
            }

            if (nodeName.equals("node")) {
                pe.getChildren().add(parsePeElement(property));
            }

            String targetClass = elt.getAttribute("class");

            if (targetClass != null && !targetClass.equals(""))
                pe.setTargetClass(targetClass);

            String parseAttr = elt.getAttribute("parseMethod");

            if (parseAttr != null && !parseAttr.equals(""))
                pe.setParseMethod(parseAttr);

            String buildAttr = elt.getAttribute("buildMethod");

            if (buildAttr != null && !buildAttr.equals(""))
                pe.setBuildMethod(buildAttr);

            String parseMethodParam = elt.getAttribute("parseMethodParam");

            if (parseMethodParam != null && !parseMethodParam.equals(""))
                pe.setParseMethodParam(parseMethodParam);

            String buildMethodParam = elt.getAttribute("buildMethodParam");

            if (buildMethodParam != null && !buildMethodParam.equals(""))
                pe.setBuildMethodParam(buildMethodParam);

        }
        return pe;
    }

    private void processParse_non_8583(Element rootEl, List<Object> list) {
        // 开始 解析文件结构
        NodeList items = rootEl.getElementsByTagName("node");
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);
            NodeList props = item.getChildNodes();
            ParseElement pe = new ParseElement();
            for (int j = 0; j < props.getLength(); j++) {
                Node property = props.item(j);
                String nodeName = property.getNodeName();
                if (nodeName.equals("#text"))
                    continue;

                if (nodeName.equals("id")) {
                    pe.setId(property.getFirstChild().getNodeValue());
                }

                if (nodeName.equals("len")) {
                    pe.setLen(property.getFirstChild().getNodeValue());
                }

                if (nodeName.equals("tag")) {
                    pe.setTag(property.getFirstChild().getNodeValue());
                }

                if (nodeName.equals("lllvar")) {
                    pe.setLllvar(property.getFirstChild().getNodeValue());
                }
            }
            list.add(pe);
        }
    }

    private void processParse_8583(Element rootEl, List<Object> list) {
        // 开始 解析文件结构
        NodeList items = rootEl.getElementsByTagName("node");
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);
            NodeList props = item.getChildNodes();
            ParseElement pe = new ParseElement();
            for (int j = 0; j < props.getLength(); j++) {
                Node property = props.item(j);
                String nodeName = property.getNodeName();
                if (nodeName.equals("#text"))
                    continue;

                if (nodeName.equals("id")) {
                    pe.setId(property.getFirstChild().getNodeValue());
                }

                if (nodeName.equals("len")) {
                    pe.setLen(property.getFirstChild().getNodeValue());
                }

                if (nodeName.equals("tag")) {
                    pe.setTag(property.getFirstChild().getNodeValue());
                }

                if (nodeName.equals("lllvar")) {
                    pe.setLllvar(property.getFirstChild().getNodeValue());
                }
            }
            list.add(pe);
        }
    }

}
