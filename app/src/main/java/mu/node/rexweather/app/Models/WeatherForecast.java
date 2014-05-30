package mu.node.rexweather.app.Models;

public class WeatherForecast {
    private final String mLocationName;
    private final long mTimestamp;
    private final String mDescription;
    private final float mMinimumTemperature;
    private final float mMaximumTemperature;

    public WeatherForecast(final String locationName,
                           final long timestamp,
                           final String description,
                           final float minimumTemperature,
                           final float maximumTemperature) {

        mLocationName = locationName;
        mTimestamp = timestamp;
        mMinimumTemperature = minimumTemperature;
        mMaximumTemperature = maximumTemperature;
        mDescription = description;
    }

    public String getLocationName() {
        return mLocationName;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public String getDescription() {
        return mDescription;
    }

    public float getMinimumTemperature() {
        return mMinimumTemperature;
    }

    public float getMaximumTemperature() {
        return mMaximumTemperature;
    }
}