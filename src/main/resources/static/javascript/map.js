var beRightThereMap = (function() {
    var map;
    var travelPath;
    var autoFocusEnabled = true;

    function createTravelPath(map, locations) {
        var travelPath = L.polyline([], {color: '#00a2e8'}).addTo(map);
        if(locations.length > 0) {
            appendTravelPath(travelPath, locations);
            fitBounds(map, travelPath, autoFocusEnabled);
        }
        return travelPath;
    }

    function appendTravelPath(travelPath, locations) {
        locations.forEach(function(location) {
            addTravelPathLocation(travelPath, location);
        });
    }

    function addTravelPathLocation(travelPath, location) {
        travelPath.addLatLng(L.latLng([location.latitude, location.longitude]));
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

    function initialize(initialLocations) {
        initMap(initialLocations);
        registerEventHandlers();
    }

    function registerEventHandlers() {
        $(document).on('new-location', function(event, location) {
            addTravelPathLocation(travelPath, location);
            fitBounds(map, travelPath, autoFocusEnabled);
        });
    }

    return {
        initialize: initialize
    };
}());

