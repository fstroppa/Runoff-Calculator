package BackEnd;

import MiddleWare.*;
import static MiddleWare.TableManager.currentlyTableValues;
import java.sql.*;
import java.util.*;

public final class SQLManager {
    private static List<Object> runoffParameters = new ArrayList<>();
    private static ResultSet resultSet;
    private static Connection connection;
    private static PreparedStatement preparedStatement;
    
    public static void loadValuesFromSQL(){
        try {
            getResultSet();
            passResultSetToObservableList();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private static void getResultSet() throws SQLException{
            tryToConnectToDatabase();
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM runoff.runoffdb");
    }
    
    private static void tryToConnectToDatabase() throws SQLException{
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/runoff", "root","");
    }
    
    private static void passResultSetToObservableList() throws SQLException{
        while (resultSet.next())
            passValueToObservableList();
    }
    
    private static void passValueToObservableList() throws SQLException{
        getValueFromResultSet();
        currentlyTableValues.addAll(new TableManager(runoffParameters));
        runoffParameters.clear();
    }
    
    //TODO refactor this method - Too big and repetitive
    private static void getValueFromResultSet() throws SQLException{
        for (int columnID = 1; columnID <= resultSet.getMetaData().getColumnCount(); columnID++) {
            if ("VARCHAR".equals(resultSet.getMetaData().getColumnTypeName(columnID)))
                runoffParameters.add(resultSet.getString(columnID));
            if ("DOUBLE".equals(resultSet.getMetaData().getColumnTypeName(columnID)))
                runoffParameters.add(resultSet.getDouble(columnID));
        }
    }
      
    public static void saveValuesToSQL(){
        try {
            tryToSaveValuesToSQL();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
   
    private static void tryToSaveValuesToSQL() throws SQLException{
        tryToConnectToDatabase();
        cleanCurrentSQLTable();
        prepareSaveStatement();
        saveAllRows();
    }   
        
    private static void prepareSaveStatement() throws SQLException{
        preparedStatement = connection.prepareStatement("INSERT INTO `runoff`.`runoffdb` "
                + "(`key`, `city`, `precipitationTime`, `returnPeriod`, `area`, "
                + "`runoffCoefficientC`, `precipitation`, `calculatedRunoff`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
    }
    
    //Solution without considering any robustness! definitely an wrong idea in real world applications.
    private static void cleanCurrentSQLTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM runoff.runoffdb");
    }
    
    private static void saveAllRows() throws SQLException{
        int index = 0;
        for (TableManager runoff : currentlyTableValues) {
            preparedStatement.setInt(1, index);
            preparedStatement.setString(2, runoff.getCity());
            preparedStatement.setDouble(3, Double.valueOf(runoff.getPrecipitationTime()));
            preparedStatement.setDouble(4, Double.valueOf(runoff.getReturnPeriod()));
            preparedStatement.setDouble(5, Double.valueOf(runoff.getArea()));
            preparedStatement.setDouble(6, Double.valueOf(runoff.getRunoffCoefficientC()));
            preparedStatement.setDouble(7, Double.valueOf(runoff.getPrecipitation()));
            preparedStatement.setDouble(8, Double.valueOf(runoff.getCalculatedRunoff()));   
            preparedStatement.executeUpdate();
            index++;
        }
    } 
}
