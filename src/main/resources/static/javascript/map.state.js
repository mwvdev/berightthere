define(["map.core", "map.events", "module"], function (mapCore, mapEvents, module) {
    var config = module.config();
    var eventEmitter = mapCore.getEventEmitter();

    var locations = config.locations;

    eventEmitter.addListener(mapEvents.websocket.locationReceived, function (receivedLocation) {
        var location = mapLocation(receivedLocation);

        if (isLatestLocation(location)) {
            locations.push(location);

            eventEmitter.emit(mapEvents.location.singleReceived, location);
        }
        else {
            var locationIndex = findLocationIndex(location);
            locations.splice(locationIndex, 0, location);

            eventEmitter.emit(mapEvents.location.allReceived, locations);
        }
    });
    eventEmitter.addListener(mapEvents.websocket.reconnected, function (receivedLocations) {
        locations = receivedLocations.map(mapLocation);
        eventEmitter.emit(mapEvents.location.allReceived, locations);
    });

    function isLatestLocation(location) {
        if (locations.length === 0) {
            return true;
        }

        var lastLocation = locations[locations.length - 1];
        return location.measuredAt > lastLocation.measuredAt;
    }

    function findLocationIndex(location) {
        for (var i = 0; i < locations.length; i++) {
            var currentLocation = locations[i];

            if (location.measuredAt < currentLocation.measuredAt) {
                return i;
            }
        }

        return locations.length;
    }

    function mapLocation(location) {
        return {
            latitude: location.latitude,
            longitude: location.longitude,
            measuredAt: new Date(location.measuredAt),
            accuracy: location.accuracy
        }
    }
});