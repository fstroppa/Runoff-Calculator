package BackEnd;

import MiddleWare.*;
import static MiddleWare.TableManager.currentlyTableValues;
import java.io.*;


public class CacheOfState implements Serializable{
    public SerializableListOfRunoffs serializableListofRunoffs = new SerializableListOfRunoffs();
    public int selectedRow;
    
    public CacheOfState() {
    }

    public void loadCacheState(){ 
        CacheOfState cache;
        try (ObjectInputStream objectInStream = 
                new ObjectInputStream(new FileInputStream("cacheOfState.bin"))) {
            cache = (CacheOfState)objectInStream.readObject();
        } catch (Exception ex) {
            cache = null;
            System.out.println(ex.getMessage());
        }
        if(cache == null){
            return;
        }
        updateCurrentlyTableValues(cache);
        InterfaceManager.selectedRow = cache.selectedRow;
    }
    
    private static void updateCurrentlyTableValues(CacheOfState cache){
        currentlyTableValues.clear();
        for (SerializableRunoff runoff : cache.serializableListofRunoffs.serializableRunoffs) {
            currentlyTableValues.add(new TableManager(runoff.city, runoff.precipitationTime,
                    runoff.returnPeriod, runoff.area, runoff.runoffCoefficientC));
        }
    }
    
    public void saveCacheState(){
        try (ObjectOutputStream objectOutStream = 
                new ObjectOutputStream(new FileOutputStream("cacheOfState.bin"))) {
            objectOutStream.writeObject(this);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}