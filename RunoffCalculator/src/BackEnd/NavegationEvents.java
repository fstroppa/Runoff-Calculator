package BackEnd;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class NavegationEvents implements Serializable{
    private String nameOfApplication = "Runoff Calculator";
    private String eventTag;
    private String date;
    
    public NavegationEvents(ActionEvent event){
        Date unformattedDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.date = formatter.format(unformattedDate);
        Button buttonPressed = (Button) event.getSource();
        this.eventTag = buttonPressed.getId().toString();
    }
}
