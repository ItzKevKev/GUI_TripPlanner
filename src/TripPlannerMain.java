import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

public class TripPlannerMain extends Application {
    final String FILE_PROTOCOL = "file:";
    final String IMAGES_PATH = "./Image/";
    final String USA_MAP_IMAGE = FILE_PROTOCOL + IMAGES_PATH + "usa_map.jpg";
    Image usaMapImage = loadImage(USA_MAP_IMAGE);
    ImageView mapImage = new ImageView();
    BorderPane appPane = new BorderPane();
    Pane leftMapPane = new Pane();
    Scene windowScene = new Scene(appPane);

    TextField city = new TextField();
    TextField state = new TextField();
    TextField latDeg = new TextField();
    TextField latMin = new TextField();
    TextField longDeg = new TextField();
    TextField longMin = new TextField();

    ListView<Cities> cityList;
    Label possibleStops = new Label("List of Possible Stops: ");
    Label tripDistance;
    String fileName = "List of Stops";
    int cityNumberClicked;
    int tripNumberClicked;
    private ObservableList <Cities> cityListObs;
    private ObservableList <Cities> cityTripObs;
    ListView<Cities> tripList;
    public String tripName;

    Button removeCity;
    Button addCity;
    Button updBut;
    Button addTStop;
    Button removeTStop;

    Stage window;


    public void start(Stage primaryStage) {
        //topPane
        HBox topPane = new HBox();
        topPane.setSpacing(20);
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        Button btNew = new Button ("New");
        Button btSave = new Button ("Save");
        Button btLoad = new Button ("Load");
        btNew.setMinSize(150,50);
        btSave.setMinSize(150,50);
        btLoad.setMinSize(150,50);
        topPane.getChildren().addAll(btNew,btSave,btLoad);
        topPane.setStyle("-fx-border-color: black;"+"-fx-background-color: #2f4f4f;");

        btNew.setOnAction (e-> newFileName());
        btSave.setOnAction (e-> saveFile());
        btLoad.setOnAction (e-> loadFile());
        appPane.setTop(topPane);

        //bottomPane
        StopEditor.f = new File(fileName);
        if(StopEditor.checkF()){
            StopEditor.initCitiesArrayList();
            StopEditor.saveList(fileName);
        }else{
            StopEditor.loadList(fileName);
        }
        HBox bottomPane = new HBox();
        VBox listOfCities = new VBox();
        cityList = new ListView<>();
        cityListObs = FXCollections.observableArrayList(StopEditor.citiesList);
        cityList.setItems(cityListObs);
        cityList.getSelectionModel().getSelectedItem();

        listOfCities.getChildren().addAll(possibleStops,cityList);
        addCity = new Button ("+");
        removeCity = new Button("-");
        GridPane cityInfo = new GridPane();
        cityInfo.setHgap(5);
        cityInfo.setVgap(5);
        cityInfo.add(new Label("City: "),0,0);
        cityInfo.add(city,1,0);
        cityInfo.add(new Label("State: "),0,1);
        cityInfo.add(state,1,1);
        cityInfo.add(new Label("Latitude Degrees: "),0,2);
        cityInfo.add(latDeg, 1,2);
        cityInfo.add(new Label("Latitude Minutes: "), 0, 3);
        cityInfo.add(latMin, 1,3);
        cityInfo.add(new Label ("Longitude Degrees: "),0,4);
        cityInfo.add(longDeg, 1,4);
        cityInfo.add(new Label("Longitude Minutes: "),0, 5);
        cityInfo.add(longMin, 1,5);
        updBut = new Button("Update");
        cityInfo.add(updBut,0,6);

        cityList.setOnMouseClicked(c->mouseClicked());

        updBut.setOnAction(e->updateButClicked());
        updBut.setDisable(true);
        addCity.setOnAction(e->addButClicked());

        removeCity.setDisable(true);
        removeCity.setOnAction(e->removeButClicked());

        VBox properAlign = new VBox();
        properAlign.getChildren().addAll(addCity,cityInfo);
        bottomPane.getChildren().addAll(listOfCities,properAlign,removeCity);
        bottomPane.setSpacing(20);
        appPane.setBottom(bottomPane);
        //rightPane

        Label tripStop = new Label ("Trip Stops");
        addTStop = new Button ("+");
        removeTStop = new Button ("-");
        HBox rightBox = new HBox();
        rightBox.getChildren().addAll(tripStop, addTStop, removeTStop);
        rightBox.setSpacing(20);
        tripList = new ListView<>();
        cityTripObs = FXCollections.observableArrayList(TripEditor.tripAList);
        tripList.setItems(cityTripObs);
        tripList.getSelectionModel().getSelectedItem();
        tripDistance = new Label("Total Distance: ");
        VBox rightFormat = new VBox();
        rightFormat.getChildren().addAll(rightBox, tripList, tripDistance);
        appPane.setRight(rightFormat);

        tripList.setOnMouseClicked(e -> mouseTripClicked());

        addTStop.setDisable(true);
        addTStop.setOnAction(e-> addTripStop());
        removeTStop.setDisable(true);
        removeTStop.setOnAction(e->removeTripStop());



        //leftPane
        leftMapPane.getChildren().add(mapImage);
        mapImage.setImage(usaMapImage);
        //Circle dallasCircle = new Circle (864-446.4,452-128.56,5);
        //leftMapPane.getChildren().add(dallasCircle);
        appPane.setLeft(leftMapPane);




        window = primaryStage;
        primaryStage.setTitle("Trip Planner");
        window.setWidth(1200);
        window.setHeight(1000);
        windowScene.setOnKeyReleased (e-> textFieldCheck());
        window.setScene(windowScene);
        window.show();

    }
    private Image loadImage(String imageFileURL){
        Image image = new Image(imageFileURL);
        if (!image.isError()) {
            return image;
        }
        else
            return null;
    }
    private void mouseClicked(){
        updBut.setDisable(false);
        Cities selectedCity = cityList.getSelectionModel().getSelectedItem();
        cityNumberClicked = cityList.getSelectionModel().getSelectedIndex();
        city.setText(selectedCity.getCityName());
        state.setText(selectedCity.getStateName());
        latDeg.setText(Integer.toString(selectedCity.getLatDeg()));
        latMin.setText(Integer.toString(selectedCity.getLatMin()));
        longDeg.setText(Integer.toString(selectedCity.getLongDeg()));
        longMin.setText(Integer.toString(selectedCity.getLongMin()));
        removeCity.setDisable(false);
        tripNumberClicked =-1;
        if(tripName != null && tripName.length() > 0){
            addTStop.setDisable(false);
        }

    }
    private void mouseTripClicked(){
        int clickRecord = tripList.getSelectionModel().getSelectedIndex();
        if(tripNumberClicked == clickRecord){
            tripList.getSelectionModel().clearSelection();
            tripNumberClicked = -1;
            removeTStop.setDisable(true);
        }else{
            tripNumberClicked = clickRecord;
            removeTStop.setDisable(false);
        }

    }

    private void updateButClicked(){
        if(cityNumberClicked > 1000){
            String cname = city.getText();
            String sname = state.getText().toUpperCase();
            int latdeg = Integer.parseInt(latDeg.getText());
            int latmin = Integer.parseInt(latMin.getText());
            int longdeg = Integer.parseInt(longDeg.getText());
            int longmin = Integer.parseInt(longMin.getText());
            StopEditor.addList(new Cities(cname,sname,latdeg,latmin,longdeg,longmin));
            cityListObs = FXCollections.observableArrayList(StopEditor.citiesList);
            cityList.setItems(cityListObs);
            StopEditor.saveList(fileName);
        }else{
            String cname = city.getText();
            String sname = state.getText().toUpperCase();
            int latdeg = Integer.parseInt(latDeg.getText());
            int latmin = Integer.parseInt(latMin.getText());
            int longdeg = Integer.parseInt(longDeg.getText());
            int longmin = Integer.parseInt(longMin.getText());
            StopEditor.editList(new Cities(cname,sname,latdeg,latmin,longdeg,longmin),cityNumberClicked);
            cityListObs = FXCollections.observableArrayList(StopEditor.citiesList);
            cityList.setItems(cityListObs);
            StopEditor.saveList(fileName);
            }

        }

    private void addButClicked(){
        cityNumberClicked = 1001;
        city.setText("");
        state.setText("");
        latDeg.setText("");
        latMin.setText("");
        longDeg.setText("");
        longMin.setText("");
        removeCity.setDisable(true);
        updBut.setDisable(true);

    }

    private void removeButClicked(){
        Alert removeConf = new Alert(Alert.AlertType.CONFIRMATION);
        removeConf.setContentText("Are you sure you want to remove this city?");
        Optional<ButtonType> result = removeConf.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            cityNumberClicked = cityList.getSelectionModel().getSelectedIndex();
            StopEditor.removeList(cityNumberClicked);
            cityListObs = FXCollections.observableArrayList(StopEditor.citiesList);
            cityList.setItems(cityListObs);
            city.setText("");
            state.setText("");
            latDeg.setText("");
            latMin.setText("");
            longDeg.setText("");
            longMin.setText("");
            StopEditor.saveList(fileName);
            removeCity.setDisable(true);
        } else {
            // ... user chose CANCEL or closed the dialog
            return;
        }


    }

    private void textFieldCheck(){
        if(!city.getText().equals("") && city != null &&
            !latDeg.getText().equals("") && latDeg != null
            && !latMin.getText().equals("") && latMin!=null
            && !longDeg.getText().equals("") && longDeg != null
            && !longMin.getText().equals("") && longMin != null){
            boolean latDCheck, latMCheck, longDCheck, longMCheck;
            int latdeg = Integer.parseInt(latDeg.getText());
            int latmin = Integer.parseInt(latMin.getText());
            int longdeg = Integer.parseInt(longDeg.getText());
            int longmin = Integer.parseInt(longMin.getText());

            if (latdeg <25 || latdeg > 50)
                latDCheck = false;
            else
                latDCheck = true;

            if (latmin < 0 || latmin > 59)
                latMCheck = false;
            else
                latMCheck = true;

            if (longdeg < 65 || longdeg > 125)
                longDCheck = false;
            else
                longDCheck = true;

            if (longmin <0 || longmin > 59)
                longMCheck = false;
            else
                longMCheck = true;

            if (latDCheck && latMCheck && longDCheck && longMCheck)
                updBut.setDisable(false);
            else
                updBut.setDisable(true);
        }



    }

    private void newFileName(){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New File Creator");
        dialog.setHeaderText("Welcome to Trip Planner");
        dialog.setContentText("Please enter the name of your trip: ");
        Optional<String> result = dialog.showAndWait();
        tripName = result.get();
        window.setTitle("Trip Planner - "+tripName);
        TripEditor.tripAList.clear();
        cityTripObs = FXCollections.observableArrayList(TripEditor.tripAList);
        tripList.setItems(cityTripObs);
        TripEditor.distanceCalculate();
        leftMapPane.getChildren().clear();
        leftMapPane.getChildren().add(mapImage);
        mapImage.setImage(usaMapImage);

    }
    private void saveFile(){
        try{
            FileChooser saveFile = new FileChooser();
            saveFile.setTitle("File Saver");
            saveFile.setInitialFileName(tripName);
            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            saveFile.setInitialDirectory(new File(currentPath));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File (*.File)", "*.File");
            saveFile.getExtensionFilters().add(extFilter);
            Stage secondaryStage = new Stage();
            TripEditor.sTrip = saveFile.showSaveDialog(secondaryStage);
            if(TripEditor.sTrip!= null) {
                TripEditor.saveTrip();
            }
        }catch (Exception e){
            return;
        }

    }
    private void loadFile(){
        try{
            FileChooser loadFile = new FileChooser();
            loadFile.setTitle("Load File");
            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            loadFile.setInitialDirectory(new File(currentPath));
            Stage secondaryStage = new Stage();
            TripEditor.sTrip = loadFile.showOpenDialog(secondaryStage);
            if(TripEditor.sTrip!=null){
                TripEditor.loadTrip();
                leftMapPane.getChildren().clear();
                leftMapPane.getChildren().add(mapImage);
                mapImage.setImage(usaMapImage);
                drawPointsAndLines();
                cityTripObs = FXCollections.observableArrayList(TripEditor.tripAList);
                tripList.setItems(cityTripObs);
                TripEditor.distanceCalculate();
                tripName = TripEditor.sTrip.getName();
                window.setTitle("Trip Planner - "+tripName);
                tripDistance.setText("Total Distance: "+TripEditor.totDistance+" kilometers");

            }
        }catch(Exception e ){
            return;

        }
    }
    private void addTripStop(){
        Cities selectedCity = cityList.getSelectionModel().getSelectedItem();
        if(tripNumberClicked == -1){
            TripEditor.tripAList.add(selectedCity);
            cityTripObs = FXCollections.observableArrayList(TripEditor.tripAList);
            tripList.setItems(cityTripObs);
        }else{
            if(tripNumberClicked < TripEditor.tripAList.size()){
                TripEditor.tripAList.add(tripNumberClicked+1,selectedCity);
                cityTripObs = FXCollections.observableArrayList(TripEditor.tripAList);
                tripList.setItems(cityTripObs);
            }else{
                TripEditor.tripAList.add(selectedCity);
                cityTripObs = FXCollections.observableArrayList(TripEditor.tripAList);
                tripList.setItems(cityTripObs);
            }
        }
        TripEditor.distanceCalculate();
        tripDistance.setText("Total Distance: "+TripEditor.totDistance+" kilometers");
        drawPointsAndLines();
    }

    private void removeTripStop(){
        TripEditor.tripAList.remove(tripNumberClicked);
        cityTripObs = FXCollections.observableArrayList(TripEditor.tripAList);
        tripList.setItems(cityTripObs);
        removeTStop.setDisable(true);
        TripEditor.distanceCalculate();
        leftMapPane.getChildren().clear();
        leftMapPane.getChildren().add(mapImage);
        mapImage.setImage(usaMapImage);
        drawPointsAndLines();
        tripDistance.setText("Total Distance: "+TripEditor.totDistance+" kilometers");
    }
    private void drawPointsAndLines(){
        double x1,y1,x2,y2;
        Circle circle1, circle2;
        Line drawline;
        if (TripEditor.tripAList.size() == 1){
            x1 = 864.00-(864.00*((TripEditor.tripAList.get(0).getLongDeg()-65.00)/60.00))-10;
            y1 = 452.00-(452.00*((TripEditor.tripAList.get(0).getLatDeg()-25.00)/25.00))-10;
            circle1 = new Circle(x1,y1,5);
            leftMapPane.getChildren().add(circle1);

        }
        if (TripEditor.tripAList.size()>1){
            for (int i=0; i <= TripEditor.tripAList.size()-2;){
                for (int j =i+1; j <= TripEditor.tripAList.size()-1;j++ ){
                    x1 = 864.00-(864.00*((TripEditor.tripAList.get(i).getLongDeg()-65.00)/60.00))-10;
                    y1 = 452.00-(452.00*((TripEditor.tripAList.get(i).getLatDeg()-25.00)/25.00))-10;
                    x2 = 864.00-(864.00*((TripEditor.tripAList.get(j).getLongDeg()-65.00)/60.00))-10;
                    y2 = 452.00-(452.00*((TripEditor.tripAList.get(j).getLatDeg()-25.00)/25.00))-10;
                    circle1 = new Circle(x1,y1,5);
                    circle2 = new Circle(x2,y2,5);
                    drawline = new Line(x1,y1,x2,y2);
                    leftMapPane.getChildren().addAll(circle1,circle2,drawline);
                    i++;
                }
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
