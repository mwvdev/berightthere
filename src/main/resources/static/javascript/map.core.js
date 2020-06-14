define(["eventEmitter", "leaflet", "map.events", "module", "utils"], function(EventEmitter, L, mapEvents, module, utils) {
    var map;
    var travelPath;
    var positionMarker;
    var boundedMarkers = new Set();

    var config = module.config();
    var eventEmitter = new EventEmitter();
    eventEmitter.addListener(mapEvents.location.singleReceived, function(location) {
        var latLng = utils.mapToLatLng(location);
        travelPath.addLatLng(latLng);
        positionMarker.setLatLng(latLng)
    });

    eventEmitter.addListener(mapEvents.location.allReceived, function(locations) {
        travelPath.remove();
        travelPath = createTravelPath(map, locations, { color: '#00a2e8' });

        var latestLocation = locations[locations.length-1];
        positionMarker.setLatLng([latestLocation.latitude, latestLocation.longitude])
    });

    eventEmitter.addListener(mapEvents.viewport.boundedMarkerCreated, function(marker) {
        boundedMarkers.add(marker);
    });
    eventEmitter.addListener(mapEvents.viewport.boundedMarkerRemoved, function(marker) {
        boundedMarkers.delete(marker);
    });
    eventEmitter.addListener(mapEvents.viewport.staleBounds, function() {
        var travelPathBounds = travelPath.getBounds();

        var bounds = L.latLngBounds(travelPathBounds.getNorthEast(), travelPathBounds.getSouthWest());

        boundedMarkers.forEach(function(boundedMarker) {
            bounds.extend(boundedMarker.getLatLng());
        });

        map.fitBounds(bounds);
    });

    function initMap(locations) {
        map = L.map('map');

        var osmUrl = 'https://cartodb-basemaps-{s}.global.ssl.fastly.net/light_all/{z}/{x}/{y}@2x.png';
        var osmAttrib = '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, &copy; <a href="https://carto.com/attribution">CARTO</a>';
        var osm = new L.TileLayer(osmUrl, {minZoom: 6, maxZoom: 18, attribution: osmAttrib});

        map.setView([55.676, 12.568], 9);
        map.addLayer(osm);

        travelPath = createTravelPath(map, locations, { color: '#00a2e8' });

        var latestLocation = locations[locations.length-1];
        positionMarker = L.marker(utils.mapToLatLng(latestLocation)).addTo(map);

        eventEmitter.emit(mapEvents.viewport.staleBounds);
    }

    function createTravelPath(map, locations, options) {
        var latLngs = locations.map(utils.mapToLatLng);

        return L.polyline(latLngs, options).addTo(map);
    }

    initMap(config.locations);

    return {
        getEventEmitter: function() { return eventEmitter; },
        getInstance: function() { return map; }
    };
});
