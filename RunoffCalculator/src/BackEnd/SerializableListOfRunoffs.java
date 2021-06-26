package BackEnd;

import static MiddleWare.TableManager.currentlyTableValues;
import java.io.Serializable;
import java.util.ArrayList;

public class SerializableListOfRunoffs implements Serializable {
    public ArrayList<SerializableRunoff> serializableRunoffs = new ArrayList<>();

    public SerializableListOfRunoffs() {
        currentlyTableValues.forEach(runoff -> serializableRunoffs.add(new SerializableRunoff(runoff)));  
    }
}


