define(function() {
    return {
        location: {
            allReceived: "location-all-received",
            singleReceived: "location-single-received"
        },
        viewport: {
            boundedMarkerCreated: "marker-bounded-created",
            boundedMarkerRemoved: "marker-bounded-removed",
            staleBounds: "map-stale-bounds"
        },
        websocket: {
            locationReceived: "websocket-location-received",
            reconnected: "websocket-reconnected"
        }
    };
});