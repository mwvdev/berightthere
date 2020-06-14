define(["eventEmitter", "leaflet", "map.events", "module", "utils"], function(EventEmitter, L, mapEvents, module, utils) {
    let map;
    let travelPath;
    let positionMarker;
    const boundedMarkers = new Set();

    const config = module.config();
    const eventEmitter = new EventEmitter();
    eventEmitter.addListener(mapEvents.location.singleReceived, function(location) {
        const latLng = utils.mapToLatLng(location);
        travelPath.addLatLng(latLng);
        positionMarker.setLatLng(latLng)
    });

    eventEmitter.addListener(mapEvents.location.allReceived, function(locations) {
        travelPath.remove();
        travelPath = createTravelPath(map, locations, { color: '#00a2e8' });

        const latestLocation = locations[locations.length - 1];
        positionMarker.setLatLng([latestLocation.latitude, latestLocation.longitude])
    });

    eventEmitter.addListener(mapEvents.viewport.boundedMarkerCreated, function(marker) {
        boundedMarkers.add(marker);
    });
    eventEmitter.addListener(mapEvents.viewport.boundedMarkerRemoved, function(marker) {
        boundedMarkers.delete(marker);
    });
    eventEmitter.addListener(mapEvents.viewport.staleBounds, function() {
        const travelPathBounds = travelPath.getBounds();

        const bounds = L.latLngBounds(travelPathBounds.getNorthEast(), travelPathBounds.getSouthWest());

        boundedMarkers.forEach(function(boundedMarker) {
            bounds.extend(boundedMarker.getLatLng());
        });

        map.fitBounds(bounds);
    });

    function initMap(locations) {
        map = L.map('map');

        const osmUrl = 'https://cartodb-basemaps-{s}.global.ssl.fastly.net/light_all/{z}/{x}/{y}@2x.png';
        const osmAttrib = '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, &copy; <a href="https://carto.com/attribution">CARTO</a>';
        const osm = new L.TileLayer(osmUrl, {minZoom: 6, maxZoom: 18, attribution: osmAttrib});

        map.setView([55.676, 12.568], 9);
        map.addLayer(osm);

        travelPath = createTravelPath(map, locations, { color: '#00a2e8' });

        const latestLocation = locations[locations.length - 1];
        positionMarker = L.marker(utils.mapToLatLng(latestLocation)).addTo(map);

        eventEmitter.emit(mapEvents.viewport.staleBounds);
    }

    function createTravelPath(map, locations, options) {
        const latLngs = locations.map(utils.mapToLatLng);

        return L.polyline(latLngs, options).addTo(map);
    }

    initMap(config.locations);

    return {
        getEventEmitter: function() { return eventEmitter; },
        getInstance: function() { return map; }
    };
});
