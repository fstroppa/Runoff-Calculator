package BackEnd;

import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.nio.file.*;
import javafx.event.ActionEvent;

public class XMLEvents {
    private static Path path = Paths.get("navegationEvents.xml");
    private static XStream xStream = new XStream();
    private static NavegationEvents navegationEvent;
    private static String XMLText;
        
    public static void saveEventToXML(ActionEvent event){
        navegationEvent = new NavegationEvents(event);
        setUpXStream();
        updateXMLfile();
    }
    
    private static void setUpXStream(){
        xStream.useAttributeFor(XMLEvents.class);
        xStream.alias("Events", BackEnd.NavegationEvents.class);
    }
       
    private static void updateXMLfile(){
        try {
            tryToupdateXMLfile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void tryToupdateXMLfile() throws IOException{
        //sendToRemoteServer();
        if (Files.exists(path)){
            appendXMLFile();
        }else{
            createXMLFile();
        }
    }
    
    private static void createXMLFile() throws IOException{
        XMLText = "<?xml version=\"1.0\"  encoding=\"UTF-8\"?>\n" + xStream.toXML(navegationEvent);
        Files.write(path, XMLText.getBytes());
    }
    
    private static void appendXMLFile() throws IOException{
        XMLText = "\n" + xStream.toXML(navegationEvent);
        Files.write(path, XMLText.getBytes(), StandardOpenOption.APPEND);
    }
    

//    private static void sendToRemoteServer(){
//        try (Socket socket = new Socket(IPAddress, IPPort);
//            DataOutputStream dataOutStream = new DataOutputStream(socket.getOutputStream());
//        ){
//            dataOutStream.writeUTF("<?xml version=\"1.0\"  encoding=\"UTF-8\"?>\n" + xStream.toXML(navegationEvent));
//        } catch (IOException ex) {
//            System.err.println(ex.getMessage());
//        }
//    }
        
}