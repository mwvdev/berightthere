define(["eventEmitter", "leaflet", "map.events", "module"], function(EventEmitter, L, mapEvents, module) {
    var map;
    var travelPath;
    var positionMarker;
    var accuracyIndicator;

    var config = module.config();
    var eventEmitter = new EventEmitter();
    eventEmitter.addListener(mapEvents.location.received, function(location) {
         addTravelPathLocation(travelPath, location);
         updatePositionMarker(positionMarker, location);
         updateAccuracyIndicator(accuracyIndicator, location);
     });

    function createTravelPath(map, locations) {
        var latLngs = locations.map(function(location) {
            return [location.latitude, location.longitude];
        });
        var newTravelPath = L.polyline(latLngs, {color: '#00a2e8'}).addTo(map);

        return newTravelPath;
    }

    function addTravelPathLocation(travelPath, location) {
        travelPath.addLatLng(L.latLng([location.latitude, location.longitude]));
    }

    function createPositionMarker(map, location) {
        return L.marker([location.latitude, location.longitude]).addTo(map);
    }

    function updatePositionMarker(positionMarker, location) {
        positionMarker.setLatLng([location.latitude, location.longitude]);
    }

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

    function fitBounds() {
        map.fitBounds(travelPath.getBounds());
    }

    function initMap(locations) {
        map = L.map('map');

        var osmUrl = 'https://cartodb-basemaps-{s}.global.ssl.fastly.net/light_all/{z}/{x}/{y}@2x.png';
        var osmAttrib = '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, &copy; <a href="https://carto.com/attribution">CARTO</a>';
        var osm = new L.TileLayer(osmUrl, {minZoom: 6, maxZoom: 18, attribution: osmAttrib});

        map.setView([55.676, 12.568], 9);
        map.addLayer(osm);

        travelPath = createTravelPath(map, locations);
        fitBounds();
        positionMarker = createPositionMarker(map, locations[locations.length-1]);
        accuracyIndicator = createAccuracyIndicator(map, locations[locations.length-1]);
    }

    initMap(config.locations);

    return {
        fitBounds: fitBounds,
        getEventEmitter: function() { return eventEmitter; },
        getInstance: function() { return map; }
    };
});
