define(function() {
    return {
        location: {
            received: "location-received"
        },
        viewport: {
            boundedMarkerCreated: "marker-bounded-created",
            boundedMarkerRemoved: "marker-bounded-removed",
            staleBounds: "map-stale-bounds"
        },
        websocket: {
            reconnected: "websocket-reconnected"
        }
    };
});