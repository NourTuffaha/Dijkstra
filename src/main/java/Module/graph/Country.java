package Module.graph;

public class Country {

    private String name;

    private double latitude;

    private double longitude;


    public Country(String name) {

        this.name = name;
    }


    public Country(String name, double latitude, double longitude) {

        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }


    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }


    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (o.getClass() != this.getClass()) {

            return false;
        }

        Country country = (Country) o;

        if (this.name.equals(country.name)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {

        return this.name;
    }

}
