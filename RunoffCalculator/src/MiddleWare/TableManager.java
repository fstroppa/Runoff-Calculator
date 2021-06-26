
package MiddleWare;

import java.lang.reflect.Field;
import java.util.List;
import javafx.collections.*;

public class TableManager extends RunoffCalculator {
    RunoffCalculator runoff = new RunoffCalculator("Arezzo", 60, 2, 10, 1);
    private Field[] allFields = runoff.getClass().getDeclaredFields();
    public static ObservableList<TableManager> currentlyTableValues = FXCollections.observableArrayList();
    private String columnName;
    private String newValue;
    private Field field;


    public TableManager(List<Object> valuesOfRow) {
        super(valuesOfRow);
    }

    public TableManager(String city, double precipitationTime, double returnPeriod, double area, double runoffCoefficientC) {
        super(city, precipitationTime, returnPeriod, area, runoffCoefficientC);
    }
    
    public void updateCurrentlyTableValues(String columnName, String newValue){
        this.columnName = columnName;
        this.newValue = newValue;
        updateValue();
        calculateFlow();       
    }
    
    private void updateValue(){
        for (Field field : allFields){
            this.field = field;
            tryToUpdateValue();
        } 
    }
    
    private void tryToUpdateValue(){
            if (fieldMatchesColumnName()){
                tryToUpdateDouble();
                tryToUpdateString();
        }
    }
    
    private boolean fieldMatchesColumnName(){
        if (field.getName().matches(columnName))
            return true;
        return false;
    }
    
    private void tryToUpdateDouble(){
        if ("double".equals(field.getGenericType().getTypeName())){
            try {
                field.set(this, Double.parseDouble(newValue)); 
            } catch (Exception e) {
            }     
        }             
    }
    
    private void tryToUpdateString(){
        try {
           field.set(this, newValue); 
        } catch (Exception e) {
        } 
    }   
}
