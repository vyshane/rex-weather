package mu.node.rexweather.app.services;

public class LocationUnavailableException extends Exception {
    LocationUnavailableException(String detailMessage) {
        super(detailMessage);
    }
}
