define(["leaflet", "map.core", "map.events", "module"], function(L, mapCore, mapEvents, module) {
    var accuracyIndicator;

    var config = module.config();
    var eventEmitter = mapCore.getEventEmitter();
    eventEmitter.addListener(mapEvents.location.received, function(location) {
        updateAccuracyIndicator(accuracyIndicator, location);
    });
    eventEmitter.addListener(mapEvents.websocket.reconnected, function(locations) {
        updateAccuracyIndicator(accuracyIndicator, locations[locations.length-1]);
    });

    function createAccuracyIndicator(map, location) {
        return L.circle([location.latitude, location.longitude], {
            color: '#5697ff',
            fillColor: '#7badfc',
            fillOpacity: 0.3,
            weight: 1,
            radius: location.accuracy
        }).addTo(map);
    }

    function updateAccuracyIndicator(accuracyIndicator, location) {
        if(!location.accuracy) {
            accuracyIndicator.setRadius(0);
            return;
        }

        accuracyIndicator.setLatLng([location.latitude, location.longitude]);
        accuracyIndicator.setRadius(location.accuracy);
    }

    accuracyIndicator = createAccuracyIndicator(mapCore.getInstance(), config.location);
});
