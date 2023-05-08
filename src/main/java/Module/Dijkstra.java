package Module;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import Module.graph.Country;
import Module.graph.Edge;
import Module.graph.EdgeFunctions;

public class Dijkstra {


    ArrayList<Country> listOfCountries = new ArrayList<Country>(100);
    ArrayList<Edge> listOfEdges = new ArrayList<Edge>(100);




    public EdgeFunctions<Country> buildGraph(File file) {

        try {
            Scanner scan = new Scanner(file);

            String line = scan.nextLine();
            String[] parts = line.split(" ");
            int numOfCountries = Integer.parseInt(parts[0]);
            int numOfEdges = Integer.parseInt(parts[1]);

            for(int i =0; i<numOfCountries;i++){
                line = scan.nextLine();
                parts = line.split(" ");


                Country newCountry = new Country(parts[0], Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
                listOfCountries.add(newCountry);
            }
            for (int i = 0; i < numOfEdges; i++) {
                line = scan.nextLine();
                parts = line.split(" ");

                Country country1 = new Country(parts[0].trim());
                int vertixFrom = listOfCountries.indexOf(country1);

                Country country2 = new Country(parts[1].trim());
                int vertixTo = listOfCountries.indexOf(country2);


                double weight = Math.sqrt(Math.pow(listOfCountries.get(vertixTo).getLatitude() - listOfCountries.get(vertixFrom).getLatitude(), 2) +
                        Math.pow(listOfCountries.get(vertixTo).getLongitude() - listOfCountries.get(vertixFrom).getLongitude(), 2));


                Edge newEdge = new Edge(vertixFrom, vertixTo, weight);
                listOfEdges.add(newEdge);
            }



            scan.close();

        } catch (FileNotFoundException e) {

            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("Error of reading File");
            alert.show();
        }

        return new EdgeFunctions<Country>(listOfCountries, listOfEdges);

    }

    public ArrayList<Country> getListOfCountries() {
        return listOfCountries;
    }

    public void setListOfCountries(ArrayList<Country> listOfCountries) {
        this.listOfCountries = listOfCountries;
    }

    public ArrayList<Edge> getListOfEdges() {
        return listOfEdges;
    }

    public void setListOfEdges(ArrayList<Edge> listOfEdges) {
        this.listOfEdges = listOfEdges;
    }



}
