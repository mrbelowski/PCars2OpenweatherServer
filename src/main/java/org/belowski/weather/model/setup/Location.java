package org.belowski.weather.model.setup;

import java.math.BigDecimal;

public class Location {

    private BigDecimal approximateLatitude;

    private BigDecimal approximateLongitude;
    
    public static Location create(float latitude, float longitude) {
        BigDecimal lat = new BigDecimal(latitude);
        BigDecimal roundedLat = lat.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal lon = new BigDecimal(longitude);
        BigDecimal roundedLong = lon.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return new Location(roundedLat, roundedLong);
    }

    private Location(BigDecimal approximateLatitude, BigDecimal approximateLongitude) {
        super();
        this.approximateLatitude = approximateLatitude;
        this.approximateLongitude = approximateLongitude;
    }

    public BigDecimal getApproximateLongitude() {
        return approximateLongitude;
    }

    public void setApproximateLongitude(BigDecimal approximateLongitude) {
        this.approximateLongitude = approximateLongitude;
    }

    public BigDecimal getApproximateLatitude() {
        return approximateLatitude;
    }

    public void setApproximateLatitude(BigDecimal approximateLatitude) {
        this.approximateLatitude = approximateLatitude;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((approximateLatitude == null) ? 0 : approximateLatitude.hashCode());
        result = prime * result + ((approximateLongitude == null) ? 0 : approximateLongitude.hashCode());
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
        if (approximateLatitude == null) {
            if (other.approximateLatitude != null)
                return false;
        } else if (!approximateLatitude.equals(other.approximateLatitude))
            return false;
        if (approximateLongitude == null) {
            if (other.approximateLongitude != null)
                return false;
        } else if (!approximateLongitude.equals(other.approximateLongitude))
            return false;
        return true;
    }
}
