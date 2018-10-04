package cn.sjj.study.format_resource_name;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class DOM4JTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new File("C:\\Users\\sjj\\.gradle\\caches\\transforms-1\\files-1.1\\appcompat-v7-26.0.2.aar\\87193e658e92abd1bac368e29baa204d\\res\\values/values.xml"));
            Element bookStore = document.getRootElement();
            scanElement(bookStore);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private static void scanElement(Element element) {
        Element parent = element.getParent();
        if (element.getName().equals("style")) {
            System.out.println(element.getName());
            List<Attribute> attributes = element.attributes();
            for (Attribute attr : attributes) {
                System.out.println("    " + attr.getName() + " = " + attr.getValue());
            }
        } else if (parent != null) {
            if (parent.getName().equals("declare-styleable")) {
                System.out.println(element.getName());
                List<Attribute> attributes = element.attributes();
                for (Attribute attr : attributes) {
                    System.out.println("    " + attr.getName() + " = " + attr.getValue());
                }
            }
        }

//        System.out.println("    " + attributes);
        Iterator iterator = element.elementIterator();
        while (iterator.hasNext()) {
            scanElement((Element) iterator.next());
        }
    }
}