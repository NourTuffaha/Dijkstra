package Module;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import Module.graph.Country;
import Module.graph.Edge;
import Module.graph.EdgeFunctions;

import javax.swing.*;


public class MainController implements Initializable {

    public ArrayList<String> countriesName;

    private EdgeFunctions<Country> worldMapGraph;

    private ObservableList<Row> flighs;

    private boolean isSource = true;

    private boolean isSourceAndDestinationDetermined = false;

    private Node sourcePoint;

    private Node destinationPoint;

    private ArrayList<Country> countries;

    private ArrayList<Edge> edgesList;

    private boolean isThereLines = false;

    private double totalDestance = 0;

    private File countriesFile;

    List<String> countriesList = new ArrayList<String>();
    @FXML
    private Pane pane;

    @FXML
    private TextField sourceCountry;

    @FXML
    private TextField destinationCountry;

    @FXML
    private TextArea outputDestance;

    @FXML
    private TableView<Row> tableDetails;

    @FXML
    private TableColumn<Row, String> from;

    @FXML
    private TableColumn<Row, String> to;

    @FXML
    private ComboBox<String> sourceBox;

    @FXML
    private ComboBox<String> destBox;

    int imgHeight = 1698;
    int imgWidth = 824;

    @FXML
    void clear(ActionEvent event) {
        clearAll();
    }

    private void clearAll() {
        this.sourceBox.setValue("");
        this.destBox.setValue("");
        this.sourceCountry.clear();
        this.destinationCountry.clear();
        this.outputDestance.clear();
        this.totalDestance = 0;

        if (flighs != null) {
            this.flighs.clear();
        }

        isSource = true;
        isSourceAndDestinationDetermined = false;

        if (sourcePoint != null) {

            sourcePoint.setFill(Color.BLACK);
            sourcePoint = null;
        }

        if (destinationPoint != null) {

            destinationPoint.setFill(Color.BLACK);
            destinationPoint = null;
        }

        if (isThereLines) {

            ObservableList<javafx.scene.Node> componentsOfPane = pane.getChildren();
            int length = componentsOfPane.size();
            ArrayList<javafx.scene.Node> removedNodes = new ArrayList<>();
            for (int i = 0; i < length; i++) {

                javafx.scene.Node node = componentsOfPane.get(i);
                if (node instanceof Line) {

                    removedNodes.add(node);
                }

            }

            for (javafx.scene.Node node : removedNodes) {

                componentsOfPane.remove(node);
            }

            this.isThereLines = false;
        }
    }


    @FXML
    void findPath(ActionEvent event) {

        boolean flagMouse = false, flagKeyboard = false;

        String source = this.sourceBox.getValue().trim();
        String destination = this.destBox.getValue().trim();

        if (!source.equals("") && !destination.equals("")) {

            flagKeyboard = true;
        }

        if (this.sourcePoint != null && this.destinationPoint != null) {

            flagMouse = true;

        }

        if (flagKeyboard && flagMouse) {
            JOptionPane.showMessageDialog(null, "Choose either by keyboard\nOr Mouse Not Both");


            clearAll();

        } else if (flagMouse) {

            if (!isThereLines) {

                Country start = this.sourcePoint.getCountry();
                Country end = this.destinationPoint.getCountry();
                mouse(start, end);

            } else {

                JOptionPane.showMessageDialog(null, "Clear existing paths");

            }

        } else if (flagKeyboard) {

            if (!isThereLines) {

                if (source.equals(destination)) {

                    JOptionPane.showMessageDialog(null, "Same country");


                } else {

                    keyboard(source, destination);

                }

            } else {

                JOptionPane.showMessageDialog(null, "Clear existing paths");

            }

        } else {

            JOptionPane.showMessageDialog(null, "Enter source and destination");

        }

    }

    public void exit() {
        System.exit(1);
    }


    private Row fillTable(Country country1, Country country2, ArrayList<Edge> edgesList,
                          ArrayList<Country> countries) {

        String countryName1 = country1.getName();
        String countryName2 = country2.getName();

        int indexOfCountry1 = countries.indexOf(country1);
        int indexOfCountry2 = countries.indexOf(country2);

        Edge edge = new Edge(indexOfCountry1, indexOfCountry2);
        int indexOfEdge = edgesList.indexOf(edge);
        double distance = edgesList.get(indexOfEdge).getWeight();
        String distanceAsString = distance + " KM";

        Row row = new Row(countryName1, countryName2, distanceAsString);
        BigDecimal fixedDistance = new BigDecimal(distance);
        int decimalPlaces = 3;
        BigDecimal roundedNumber = fixedDistance.setScale(decimalPlaces, RoundingMode.HALF_UP);
        Double doubleValue = roundedNumber.doubleValue();
        totalDestance += doubleValue;
        return row;

    }

    private void keyboard(String startCountry, String endCountry) {

        Country source = new Country(startCountry);
        Country destination = new Country(endCountry);
        mouse(source, destination);

    }

    private void mouse(Country source, Country destination) {

        int sourceIndex = this.countries.indexOf(source);
        int destinationIndex = this.countries.indexOf(destination);

        if (sourceIndex == -1) {

            JOptionPane.showMessageDialog(null, "Doesn't exist");

            return;
        }

        if (destinationIndex == -1) {

            JOptionPane.showMessageDialog(null, "Doesn't exist");

            return;
        }
//        sourceCountry.setText(source.getName());
//        destinationCountry.setText(destination.getName());
        Stack<Country> shortestPath = worldMapGraph.getShortestPath(sourceIndex, destinationIndex);
        if (shortestPath != null) {

            this.isThereLines = true;
            ArrayList<Row> rows = new ArrayList<MainController.Row>();

            while (shortestPath.size() > 1) {

                Country country1 = shortestPath.pop();
                Country country2 = shortestPath.pop();
                shortestPath.push(country2);

                Line flight = new Line(country1.getLongitude(), country1.getLatitude(), country2.getLongitude(),
                        country2.getLatitude());
                pane.getChildren().add(flight);

                Row row = fillTable(country1, country2, edgesList, countries);
                rows.add(row);

            }

            flighs = FXCollections.observableArrayList(rows);
            tableDetails.setItems(flighs);
            this.outputDestance.setText(totalDestance + " In KM");

        } else {

            JOptionPane.showMessageDialog(null, "No Path");

        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Dijkstra tools = new Dijkstra();
        countriesFile = new File("C:\\Users\\noura\\Desktop\\Uni\\COMP336\\WolrdMapDemo\\src\\main\\resources\\Module\\MapData.txt");
        worldMapGraph = tools.buildGraph(countriesFile);


        this.countries = tools.getListOfCountries();
        this.edgesList = tools.getListOfEdges();


        for (int i = 0; i < countries.size(); i++) {

            countriesList.add("" + countries.get(i).getName());
        }
        Collections.sort(countriesList);
        sourceBox.getItems().setAll(countriesList);
        destBox.getItems().setAll(countriesList);

        setPoints(tools.getListOfCountries());

        from.setCellValueFactory(new PropertyValueFactory<Row, String>("Source"));
        to.setCellValueFactory(new PropertyValueFactory<Row, String>("Destination"));



    }

    private double degreesToRadians(double degree) {
        return (degree * Math.PI) / 180;
    }

    double lonToOffsets(double longitude) {
        double radius = imgWidth / (2 * Math.PI);
        int FE = 180;

        double lonRad = degreesToRadians(longitude + FE);


        return (lonRad * radius);
    }

    double latToOffsets(double latitude) {
        double radius = imgWidth / (2 * Math.PI);

        double latRad = degreesToRadians(latitude);
        double verticalOffsetFromEquator = radius * Math.log(Math.tan(Math.PI / 4 + latRad / 2));
        return ((imgHeight / 2) - verticalOffsetFromEquator);

    }

    private void setPoints(ArrayList<Country> listOfCountries) {

        for (Country c : listOfCountries) {

            double latitudeY = latToOffsets(c.getLatitude());
            double longitudeX = lonToOffsets(c.getLongitude());
            Node point = new Node(longitudeX, latitudeY, 5, c);
            point.setOnMouseClicked(this::setCircleAction);
            pane.getChildren().add(point);

        }
    }


    public void setCircleAction(MouseEvent e) {

        Object object = e.getSource();

        if ((object instanceof Node) && !isSourceAndDestinationDetermined) {

            Node point = (Node) object;

            if (isSource) {

                point.setFill(Color.GREEN);
                isSource = false;
                sourcePoint = point;
                sourceCountry.setText(sourcePoint.getCountry().getName());

            } else {

                point.setFill(Color.RED);
                isSourceAndDestinationDetermined = true;
                destinationPoint = point;
                destinationCountry.setText(destinationPoint.getCountry().getName());



            }
        }

    }


    public ArrayList<String> getCountriesName() {
        return countriesName;
    }

    public EdgeFunctions<Country> getWorldMapGraph() {
        return worldMapGraph;
    }

    public File getCountriesFile() {
        return countriesFile;
    }


    public Pane getPane() {
        return pane;
    }

    public TextField getSourceCountry() {
        return sourceCountry;
    }

    public TextField getDestinationCountry() {
        return destinationCountry;
    }


    public ArrayList<Edge> getEdgesList() {
        return edgesList;
    }


    public ArrayList<Country> getCountries() {
        return countries;
    }


    public static class Row {

        private String source;
        private String destination;
        private String destance;

        public Row() {

        }

        public Row(String source, String destination, String destance) {

            this.source = source;
            this.destination = destination;
            this.destance = destance;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getDestance() {
            return destance;
        }

        public void setDestance(String destance) {
            this.destance = destance;
        }

    }

    static class Node extends Circle {

        private Country country;

        public Node(double x, double y, double radius, Country country) {

            super(x, y, radius);
            this.country = country;
        }

        public Country getCountry() {
            return country;
        }

        public void setCountry(Country country) {
            this.country = country;
        }

    }

}
