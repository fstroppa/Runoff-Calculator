package BackEnd;

import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.nio.file.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.w3c.dom.Document;

public class XMLResults {
    private static XStream xStream = new XStream();
    private static SerializableListOfRunoffs runoffs = new SerializableListOfRunoffs();
    private static String XMLText;
    
    public static void saveResultsToXML(){
        configurateXStream();
        generateXMLString();
        passXMLStringToXMLFile();
        validation();
    }       
    
    private static void configurateXStream(){
        xStream.useAttributeFor(SerializableListOfRunoffs.class, "serializableRunoffs");
        xStream.alias("allresults", SerializableListOfRunoffs.class);
        xStream.aliasField("Toscana", SerializableListOfRunoffs.class, "serializableRunoffs");
        xStream.alias("results", SerializableRunoff.class);
    }
    
    private static void generateXMLString(){
        XMLText = "<?xml version=\"1.0\"  encoding=\"UTF-8\"?>\n" + xStream.toXML(runoffs);
    }
    
    private static void passXMLStringToXMLFile(){
        try {
            Files.write(Paths.get("runoff.xml"), XMLText.getBytes());
        } catch (Exception e) {
            e.printStackTrace();}
    }
    
    private static void validation(){
        try{
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document document = docBuilder.parse(new File("runoff.xml"));
            Schema schema = schemaFactory.newSchema(new StreamSource(new File("runoff.xsd")));
            schema.newValidator().validate(new DOMSource(document));
        } catch(Exception e) {
                System.err.println(e.getMessage());
        }
    }
    
    
    
    
}


