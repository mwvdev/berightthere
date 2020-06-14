define(["leaflet", "map.core", "map.events", "module", "utils"], function(L, mapCore, mapEvents, module, utils) {
    let accuracyIndicator;

    const config = module.config();
    const eventEmitter = mapCore.getEventEmitter();
    eventEmitter.addListener(mapEvents.location.singleReceived, function(location) {
        updateAccuracyIndicator(accuracyIndicator, location);
    });
    eventEmitter.addListener(mapEvents.location.allReceived, function(locations) {
        updateAccuracyIndicator(accuracyIndicator, locations[locations.length-1]);
    });

    function createAccuracyIndicator(map, location) {
        return L.circle(utils.mapToLatLng(location), {
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

        accuracyIndicator.setLatLng(utils.mapToLatLng(location));
        accuracyIndicator.setRadius(location.accuracy);
    }

    accuracyIndicator = createAccuracyIndicator(mapCore.getInstance(), config.location);
});
