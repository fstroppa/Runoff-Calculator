package MiddleWare;

import BackEnd.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


public class Main extends Application {
    public TableView<RunoffCalculator> table;
    
    @Override
    public void start(Stage stage) throws Exception {
        SQLManager.loadValuesFromSQL();
        CacheOfState cache = new CacheOfState();
        cache.loadCacheState();
        Parent root = FXMLLoader.load(getClass().getResource("/FrontEnd/MainUserInterface.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //TODO A good way to access the table in InterfaceManager is due to be found
        //The cache with Redis would facilitate the implementation
        table = (TableView<RunoffCalculator>) root.lookup("#table");
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void stop(){
        CacheOfState cache = new CacheOfState();
        cache.selectedRow = table.getSelectionModel().getSelectedIndex();
        cache.saveCacheState();
    }      
}
