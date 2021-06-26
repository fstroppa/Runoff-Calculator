package MiddleWare;

import BackEnd.*;
import static MiddleWare.TableManager.currentlyTableValues;
import java.net.URL;
import java.util.*;
import javafx.application.Platform;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.*;


public class InterfaceManager implements Initializable {
    List<TableColumn<TableManager, String>> columnsConfiguration = new ArrayList<>();
    private XYChart.Series<String, Number> seriesOfCharts = new XYChart.Series();
    public static int selectedRow;
    
    @FXML
    private TableView<TableManager> table;
    @FXML
    private TableColumn<TableManager, String> city;
    @FXML
    private TableColumn<TableManager, String> precipitationTime;
    @FXML
    private TableColumn<TableManager, String> returnPeriod;
    @FXML
    private TableColumn<TableManager, String> area;
    @FXML
    private TableColumn<TableManager, String> runoffCoefficientC;
    @FXML
    private TableColumn<TableManager, String> precipitation;
    @FXML
    private TableColumn<TableManager, String> calculatedRunoff;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    NumberAxis xAxis;
    @FXML
    CategoryAxis yAxis;
                        
    
    @FXML
    private void exit(ActionEvent event) {     
        Platform.exit();
        XMLEvents.saveEventToXML(event);
    }
     
    @FXML
    private void addRow(ActionEvent event) {     
        currentlyTableValues.add(0, new TableManager("Arezzo", 60, 2, 10, 1));
        updateTableAndGraph();
        XMLEvents.saveEventToXML(event);
    }
    
    @FXML
    private void removeRow(ActionEvent event) {   
        currentlyTableValues.remove(table.getSelectionModel().getSelectedItem());
        updateTableAndGraph();
        XMLEvents.saveEventToXML(event);
    }
    
    @FXML
    private void savetoDataBase(ActionEvent event){   
        SQLManager.saveValuesToSQL();
        XMLResults.saveResultsToXML();
        XMLEvents.saveEventToXML(event);
    }
    
    @FXML
    private void loadfromDataBase(ActionEvent event) { 
        currentlyTableValues.clear();
        SQLManager.loadValuesFromSQL();
        updateTableAndGraph();
        XMLEvents.saveEventToXML(event);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createListOfFields();
        updateTableAndGraph();
        initiazeColumns();   
        table.getSelectionModel().select(selectedRow);
    }   
    
    private void updateTableAndGraph(){
        table.getItems().clear();
        table.getItems().addAll(currentlyTableValues);
        updateGraph();
    }
    
    
    //TODO substitute the hardcode values with fields from Java Reflection API
    private void createListOfFields(){
        if (columnsConfiguration.isEmpty())
            columnsConfiguration.addAll(Arrays.asList(city, precipitationTime, 
                    returnPeriod, area, runoffCoefficientC, precipitation, calculatedRunoff));
    }
        
    private void initiazeColumns(){
        columnsConfiguration.forEach(column -> initiazeSingleColumn(column));
    }
    
    private void initiazeSingleColumn(TableColumn<TableManager, String> column){
        populateCells(column);
        if (columnShouldBeEditable(column))
            makeColumnEditable(column);      
    }
    
    private void populateCells(TableColumn<TableManager, String> column){
        column.setCellValueFactory(new PropertyValueFactory<>(column.getId()));
    }

    private boolean columnShouldBeEditable(TableColumn<TableManager, String> column){
        if (column == calculatedRunoff || column == precipitation)
            return false;
        return true;
    }
    
    // TODO refactor: method is too big and hard to read
    private void makeColumnEditable(TableColumn<TableManager, String> column){
        column.setCellFactory(TextFieldTableCell.<TableManager>forTableColumn());
        column.setOnEditCommit((CellEditEvent<TableManager, String> t) -> {
                t.getTableView().getItems().get(t.getTablePosition().getRow())
                .updateCurrentlyTableValues(t.getTableColumn().getId(), t.getNewValue());
                updateTableAndGraph();
        });
    }
    
    private void updateGraph(){
        updateSeriesOfCharts();
        barChart.getData().clear();
        barChart.getData().add(seriesOfCharts);
    }
    
    private void updateSeriesOfCharts(){
        seriesOfCharts.getData().clear();
        String XYchartCity;
        Number XYchartPreciptation;
        int numberOfBars;
        if (currentlyTableValues.size() > 5)
            numberOfBars = 5;
        else
            numberOfBars = currentlyTableValues.size();
        for (int i = 0; i < numberOfBars; i++) {
            XYchartCity = currentlyTableValues.get(i).getCity() + "\nTR = " 
                    + currentlyTableValues.get(i).getReturnPeriod() + "\nt = " 
                    + currentlyTableValues.get(i).getPrecipitationTime();
            XYchartPreciptation = Double.parseDouble(currentlyTableValues.get(i).getPrecipitation());
            seriesOfCharts.getData().add(new XYChart.Data(XYchartCity, XYchartPreciptation));
        }
    }
}