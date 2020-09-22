import java.io.*;
import java.util.ArrayList;

public class TripEditor {
    public static ArrayList<Cities> tripAList = new ArrayList<>();
    public static int totDistance;
    public static File sTrip;


    public static int distanceCalculate() {
        if (tripAList.size() < 1) {
            totDistance = 0;
            return totDistance;
        } else {
            totDistance = 0;
            double RADIAN_FACTOR = (180 / Math.PI);
            double EARTH_RADIUS = 6371.000;
            double lat1, lat2, long1, long2;
            for (int i = 0; i <= tripAList.size() - 2; ) {
                for (int j = i + 1; j <= tripAList.size() - 1; j++) {
                    lat1 = tripAList.get(i).getLatDeg() + (tripAList.get(i).getLatMin() / 60.000);
                    lat2 = tripAList.get(j).getLatDeg() + (tripAList.get(j).getLatMin() / 60.000);
                    long1 = tripAList.get(i).getLongDeg() + (tripAList.get(i).getLongMin() / 60.000);
                    long2 = tripAList.get(j).getLongDeg() + (tripAList.get(j).getLongMin() / 60.000);
                    double x = (Math.sin(lat1 / RADIAN_FACTOR) * Math.sin(lat2 / RADIAN_FACTOR))
                            + (Math.cos(lat1 / RADIAN_FACTOR) * Math.cos(lat2 / RADIAN_FACTOR) * Math.cos((long2 / RADIAN_FACTOR) - (long1 / RADIAN_FACTOR)));
                    double distance = EARTH_RADIUS * Math.atan((Math.sqrt(1 - Math.pow(x, 2)) / x));
                    totDistance += distance;
                    i++;
                }
            }
            return totDistance;
        }
    }
    public static void saveTrip(){
        try {
            //sTrip = new File(tripName);
            FileOutputStream fos = new FileOutputStream(sTrip);
            DataOutputStream dos = new DataOutputStream(fos);
            for (int i = 0; i < tripAList.size(); i++) {
                dos.writeUTF(tripAList.get(i).getCityName());
                dos.writeUTF(tripAList.get(i).getStateName());
                dos.writeInt(tripAList.get(i).getLatDeg());
                dos.writeInt(tripAList.get(i).getLatMin());
                dos.writeInt(tripAList.get(i).getLongDeg());
                dos.writeInt(tripAList.get(i).getLongMin());
            }
            dos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        /*try{
            //sTrip = new File (tripName);
            trips = new PrintWriter(sTrip);
            for (int i = 0; i<tripAList.size();i++){
                trips.println(tripAList.get(i).getInfo());
            }
            trips.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }*/

    }
    public static void loadTrip(){
        try{
            FileInputStream fis = new FileInputStream(sTrip);
            DataInputStream dis = new DataInputStream(fis);
            tripAList.clear();

            while(fis.available() > 0 ){
                tripAList.add(new Cities(dis.readUTF(),dis.readUTF(),dis.readInt(),dis.readInt(),dis.readInt(),dis.readInt()));
            }

        }catch (Exception e ){
            System.out.println("The file you are trying to access does not exist");
            return;
        }
    }
}


