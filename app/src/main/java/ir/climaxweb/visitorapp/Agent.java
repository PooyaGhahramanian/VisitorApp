package ir.climaxweb.visitorapp;

public class Agent
{

        private int id;
        private String Name;
        private String Address;
        private Double lat;
        private Double lng;

        public Agent(int id, String Name, String Address, Double lat, Double lng) {
            this.id = id;
            this.Name = Name;
            this.Address=Address;
            this.lat=lat;
            this.lng=lng;
        }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
