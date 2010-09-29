/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.model;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class ItemGeoData {
	private static final long serialVersionUID = 1L;
	
	private float longitude;
    private float latitude;

	/**
	 * 
	 */
	public ItemGeoData() {
		super();
	}
	
	public ItemGeoData(String longitudeStr, String latitudeStr) {
        longitude = Float.parseFloat(longitudeStr);
        latitude = Float.parseFloat(latitudeStr);
    }
	
	public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

	@Override
	public String toString() {
		return "GeoData [latitude=" + latitude + ", longitude=" + longitude
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(latitude);
		result = prime * result + Float.floatToIntBits(longitude);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ItemGeoData))
			return false;
		ItemGeoData other = (ItemGeoData) obj;
		if (Float.floatToIntBits(latitude) != Float
				.floatToIntBits(other.latitude))
			return false;
		if (Float.floatToIntBits(longitude) != Float
				.floatToIntBits(other.longitude))
			return false;
		return true;
	}

}
