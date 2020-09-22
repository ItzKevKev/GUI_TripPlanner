import java.io.*;
import java.util.ArrayList;

public class StopEditor {
    public static ArrayList <Cities> citiesList = new ArrayList<>();
    public static File f;
    public static boolean emptyF;

    public static boolean checkF(){
        if (f.exists() && f.length() ==0){
            emptyF = true;
        }else{
            emptyF = false;
        }
        return emptyF;
    }
    public static void initCitiesArrayList(){
        citiesList.add(new Cities("Chicago", "IL",41,53,87,38));
        citiesList.add(new Cities("Dallas", "TX",32,51,96, 51));
        citiesList.add(new Cities("Detroit","MI", 42, 25,  83, 1));
        citiesList.add(new Cities("Houston","TX", 29, 59, 95, 22));
        citiesList.add(new Cities("Los Angeles","CA", 33, 56, 118, 24));
        citiesList.add(new Cities("New York City","NY", 40, 47, 73, 58));
        citiesList.add(new Cities("Philadelphia","PA", 39, 53, 75, 15));
        citiesList.add(new Cities("Phoenix", "AZ",33, 26, 112, 1));
        citiesList.add(new Cities("San Antonio","TX", 29, 32, 98, 28));
        citiesList.add(new Cities("San Diego","CA", 32, 44, 117, 10));

    }
    public static void saveList (String fileName) {
        try {
            f = new File(fileName);
            FileOutputStream fos = new FileOutputStream(f);
            DataOutputStream dos = new DataOutputStream(fos);
            for (int i = 0; i < citiesList.size(); i++) {
                dos.writeUTF(citiesList.get(i).getCityName());
                dos.writeUTF(citiesList.get(i).getStateName());
                dos.writeInt(citiesList.get(i).getLatDeg());
                dos.writeInt(citiesList.get(i).getLatMin());
                dos.writeInt(citiesList.get(i).getLongDeg());
                dos.writeInt(citiesList.get(i).getLongMin());
            }
            dos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void loadList (String fileName){
        try{
            f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);
            citiesList.clear();

            while(fis.available() > 0 ){
                citiesList.add(new Cities(dis.readUTF(),dis.readUTF(),dis.readInt(),dis.readInt(),dis.readInt(),dis.readInt()));
            }

        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    public static void editList(Cities newCity, int editNumberCity){
        boolean breakCheck = false;
        citiesList.remove(editNumberCity);
        for(int i = 0; i<=citiesList.size()-2;){
            if (i == 0 && newCity.getCityName().compareTo(citiesList.get(i).getCityName())<0){
                citiesList.add(0,newCity);
                break;

            }else if (breakCheck == true){
                break;

            }
            for (int j = i+1; j<=citiesList.size()-1; j++){
                if(citiesList.get(i).getCityName().compareTo(newCity.getCityName()) < 0 && citiesList.get(j).getCityName().compareTo(newCity.getCityName()) > 0){
                    citiesList.add(j,newCity);
                    breakCheck = true;
                    break;
                }else if (j == citiesList.size()-1){
                    citiesList.add(newCity);
                    breakCheck = true;
                    break;
                }
                i++;
            }
        }
    }

    public static void addList(Cities newCity){
        boolean breakCheck = false;
        for(int i = 0; i<=citiesList.size()-2;){
            if (i == 0 && newCity.getCityName().compareTo(citiesList.get(i).getCityName())<0){
                citiesList.add(0,newCity);
                break;

            }else if (breakCheck == true){
                break;

            }
            for (int j = i+1; j<=citiesList.size()-1; j++){
                if(citiesList.get(i).getCityName().compareTo(newCity.getCityName()) < 0 && citiesList.get(j).getCityName().compareTo(newCity.getCityName()) > 0){
                    citiesList.add(j,newCity);
                    breakCheck = true;
                    break;
                }else if (j == citiesList.size()-1){
                    citiesList.add(newCity);
                    breakCheck = true;
                    break;
                }
                i++;
            }
        }
    }

    public static void removeList(int removeCityNumber){
        citiesList.remove(removeCityNumber);

    }
}
