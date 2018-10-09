package org.belowski.weather.model.setup;

public class Location {

    private int approximateLatitude;

    private int approximateLongitude;

    public Location(int approximateLatitude, int approximateLongitude) {
        super();
        this.approximateLatitude = approximateLatitude;
        this.approximateLongitude = approximateLongitude;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + approximateLatitude;
        result = prime * result + approximateLongitude;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location other = (Location) obj;
        if (approximateLatitude != other.approximateLatitude)
            return false;
        if (approximateLongitude != other.approximateLongitude)
            return false;
        return true;
    }

    public int getApproximateLongitude() {
        return approximateLongitude;
    }

    public void setApproximateLongitude(int approximateLongitude) {
        this.approximateLongitude = approximateLongitude;
    }

    public int getApproximateLatitude() {
        return approximateLatitude;
    }

    public void setApproximateLatitude(int approximateLatitude) {
        this.approximateLatitude = approximateLatitude;
    }
}
