define(["leaflet", "module"], function(L, module) {
    var config = module.config();

    var map;
    var travelPath;
    var positionMarker;
    var accuracyIndicator;
    var autoFocusEnabled = true;

    function createTravelPath(map, locations) {
        var latLngs = locations.map(function(location) {
            return [location.latitude, location.longitude];
        });
        var newTravelPath = L.polyline(latLngs, {color: '#00a2e8'}).addTo(map);
        fitBounds(map, newTravelPath, autoFocusEnabled);

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

    function fitBounds(map, travelPath, autoFocusEnabled) {
        if(autoFocusEnabled) {
            map.fitBounds(travelPath.getBounds());
        }
    }

    function initMap(locations) {
        map = L.map('map');

        var osmUrl = 'https://cartodb-basemaps-{s}.global.ssl.fastly.net/light_all/{z}/{x}/{y}@2x.png';
        var osmAttrib = '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, &copy; <a href="https://carto.com/attribution">CARTO</a>';
        var osm = new L.TileLayer(osmUrl, {minZoom: 6, maxZoom: 18, attribution: osmAttrib});

        map.setView([55.676, 12.568], 9);
        map.addLayer(osm);

        travelPath = createTravelPath(map, locations);
        positionMarker = createPositionMarker(map, locations[locations.length-1]);
        accuracyIndicator = createAccuracyIndicator(map, locations[locations.length-1]);
        initAutoFocusControl(map, travelPath);
    }

    function initAutoFocusControl(map, travelPath) {
        var button;

        function handleClick(event) {
            autoFocusEnabled = !autoFocusEnabled;
            fitBounds(map, travelPath, autoFocusEnabled);

            if(autoFocusEnabled) {
                L.DomUtil.addClass(button, 'focusing');
            }
            else {
                L.DomUtil.removeClass(button, 'focusing');
            }
        }

        L.Control.AutoFocus = L.Control.extend({
            onAdd: function(map) {
                container = L.DomUtil.create('div', 'leaflet-bar leaflet-control');
                button = L.DomUtil.create('a', 'auto-focus focusing', container);
                button.innerHTML = '<i class="icon-binoculars" />';
                button.role = "button";
                button.href = "#";

                L.DomEvent.disableClickPropagation(button);
                L.DomEvent.on(button, 'click', handleClick);

                return container;
            },

            onRemove: function(map) {
                L.DomEvent.off(button, 'click', handleClick);
            }
        });

        L.control.autofocus = function(opts) {
            return new L.Control.AutoFocus(opts);
        };

        L.control.autofocus({ position: 'bottomleft' }).addTo(map);
    }

    function addLocation(location) {
        addTravelPathLocation(travelPath, location);
        fitBounds(map, travelPath, autoFocusEnabled);
        updatePositionMarker(positionMarker, location);
        updateAccuracyIndicator(accuracyIndicator, location);
    }

    initMap(config.locations);

    return {
        addLocation: addLocation
    };
});
