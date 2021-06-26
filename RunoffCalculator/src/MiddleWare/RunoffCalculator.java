package MiddleWare;

import java.io.*;
import java.util.List;

public class RunoffCalculator{
    protected String city;
    protected double precipitationTime;
    protected double returnPeriod;
    protected double area;
    protected double runoffCoefficientC;
    protected double precipitation;
    protected double calculatedRunoff;
    
    public RunoffCalculator(String city, double precipitationTime, double returnPeriod, double area, double runoffCoefficientC) {
        this.city = city;
        this.precipitationTime = precipitationTime;
        this.returnPeriod = returnPeriod;
        this.area = area;
        this.runoffCoefficientC = runoffCoefficientC;
        calculateFlow();
    }
    
    
    public RunoffCalculator(List<Object> valuesOfRow) {
        this.city = (String) valuesOfRow.get(0);
        this.precipitationTime = (Double) valuesOfRow.get(1);
        this.returnPeriod = (Double) valuesOfRow.get(2);
        this.area = (Double) valuesOfRow.get(3);
        this.runoffCoefficientC = (Double) valuesOfRow.get(4);
        this.precipitation = (Double) valuesOfRow.get(5);
        this.calculatedRunoff = (Double) valuesOfRow.get(6);
    }

    
    //Unfortunatly we need to have all this gets in order to TableView works.
    public String getCity(){
        return city;
    }

    public String getReturnPeriod() {
        return String.valueOf(returnPeriod);
    }

    public String getArea() {
        return String.valueOf(area);
    }

    public String getRunoffCoefficientC() {
        return String.valueOf(runoffCoefficientC);
    }

    public String getPrecipitation() {
        return String.valueOf(precipitation);
    }

    public String getCalculatedRunoff() {
        return String.valueOf(calculatedRunoff);
    }

    public String getPrecipitationTime() {
        return String.valueOf(precipitationTime);
    }
        
    public void calculateFlow(){
        calculatePrecipitation();
        this.calculatedRunoff = precipitation * area * runoffCoefficientC / 3600 /(precipitationTime/60);
    }
    
    private void calculatePrecipitation(){
        try {
            tryToCalculatePrecipitation();
        } catch (IOException ex) {
            //senza robusteza
        }
    }
    
    private void tryToCalculatePrecipitation() throws FileNotFoundException, IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader("src\\BackEnd\\Precipitations.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (lineContainsCorrectCityandReturnPeriod(line))
                    applyPrecipitationEquation(line);
            }
        }
    }
    
    private boolean lineContainsCorrectCityandReturnPeriod(String line){
        return this.getCity().equals(line.split(",")[0]) && this.getReturnPeriod().equals(line.split(",")[1]);
    }
    
    private void applyPrecipitationEquation(String line){
        this.precipitation = Double.parseDouble(line.split(",")[2]) * Math.pow(precipitationTime/60, Double.parseDouble(line.split(",")[3]));
    }
}
