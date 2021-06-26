package BackEnd;

import MiddleWare.RunoffCalculator;
import java.io.Serializable;

public class SerializableRunoff implements Serializable{
    public String city;
    public double precipitationTime;
    public double returnPeriod;
    public double area;
    public double runoffCoefficientC;
    public double precipitation;
    public double calculatedRunoff;

    public SerializableRunoff(RunoffCalculator runoff) {
        this.city = runoff.getCity();
        this.precipitationTime = Double.parseDouble(runoff.getPrecipitationTime());
        this.returnPeriod = Double.parseDouble(runoff.getReturnPeriod());
        this.area = Double.parseDouble(runoff.getArea());
        this.runoffCoefficientC = Double.parseDouble(runoff.getRunoffCoefficientC());
        this.precipitation = Double.parseDouble(runoff.getPrecipitation());
        this.calculatedRunoff = Double.parseDouble(runoff.getCalculatedRunoff());
    }
}
