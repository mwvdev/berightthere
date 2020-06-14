define(function() {
    function mapToLatLng(location) {
        return [location.latitude, location.longitude];
    }

    function mapToLocation(rawLocation) {
        return {
            latitude: rawLocation.latitude,
            longitude: rawLocation.longitude,
            measuredAt: new Date(rawLocation.measuredAt),
            accuracy: rawLocation.accuracy
        }
    }

    return {
        mapToLatLng: mapToLatLng,
        mapToLocation: mapToLocation
    };
});