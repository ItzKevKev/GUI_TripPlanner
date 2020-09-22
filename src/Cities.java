public class Cities {
    private String cityName;
    private String stateName;
    private int latDeg;
    private int latMin;
    private int longDeg;
    private int longMin;
;

    public Cities (String initCityName, String initState,
                   int initLatDeg, int initLatMin,
                   int initLongDeg, int initLongMin){

        cityName = initCityName;
        stateName = initState;
        latDeg = initLatDeg;
        latMin = initLatMin;
        longDeg = initLongDeg;
        longMin = initLongMin;
    }

    public String getCityName (){
        return cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public int getLatDeg (){
        return latDeg;
    }

    public void setLatDeg(int newLatDeg){
        this.latDeg = newLatDeg;
    }

    public int getLatMin (){
        return latMin;
    }

    public void setLatMin(int newLatMin){
        this.latMin = newLatMin;
    }

    public int getLongDeg(){
        return longDeg;
    }

    public void setLongDeg(int newLongDeg){
        this.longDeg = newLongDeg;
    }

    public int getLongMin(){
        return longMin;
    }

    public void setLongMin(int newLongMin){
        this.longMin = newLongMin;
    }



    public String toString(){
        return cityName+", "+stateName;

    }
    public String getInfo(){
        return cityName+" "+stateName+" "+latDeg+" "+latMin+" "+longDeg+" "+longMin;

    }}
