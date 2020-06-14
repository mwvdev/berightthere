define(["map.core", "map.events", "module", "utils"], function (mapCore, mapEvents, module, utils) {
    const config = module.config();
    const eventEmitter = mapCore.getEventEmitter();

    let locations = config.locations.map(utils.mapToLocation);

    eventEmitter.addListener(mapEvents.websocket.locationReceived, function (receivedLocation) {
        const location = utils.mapToLocation(receivedLocation);

        if (isLatestLocation(location)) {
            locations.push(location);

            eventEmitter.emit(mapEvents.location.singleReceived, location);
        }
        else {
            const locationIndex = findLocationIndex(location);
            locations.splice(locationIndex, 0, location);

            eventEmitter.emit(mapEvents.location.allReceived, locations);
        }
    });
    eventEmitter.addListener(mapEvents.websocket.reconnected, function (receivedLocations) {
        locations = receivedLocations.map(utils.mapToLocation);
        eventEmitter.emit(mapEvents.location.allReceived, locations);
    });

    function isLatestLocation(location) {
        if (locations.length === 0) {
            return true;
        }

        const lastLocation = locations[locations.length - 1];
        return location.measuredAt > lastLocation.measuredAt;
    }

    function findLocationIndex(location) {
        for (let i = 0; i < locations.length; i++) {
            const currentLocation = locations[i];

            if (location.measuredAt < currentLocation.measuredAt) {
                return i;
            }
        }

        return locations.length;
    }
});
