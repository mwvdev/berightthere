var beRightThereMap = (function() {
    var map;
    var travelPath;
    var autoZoomEnabled = true;
    var autoZoomInProgress = false;

    function createTravelPath(map, locations) {
        var travelPath = L.polyline([], {color: 'red'}).addTo(map);
        if(locations.length > 0) {
            appendTravelPath(travelPath, locations);
            fitBounds(map, travelPath, autoZoomEnabled);
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

    function fitBounds(map, travelPath, autoZoomEnabled) {
        if(autoZoomEnabled) {
            autoZoomInProgress = true;
            map.fitBounds(travelPath.getBounds());
        }
    }

    function initMap(locations) {
        map = L.map('map');

        var osmUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
        var osmAttrib = 'Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
        var osm = new L.TileLayer(osmUrl, {minZoom: 6, maxZoom: 16, attribution: osmAttrib});

        map.setView([55.676, 12.568], 9);
        map.addLayer(osm);

        travelPath = createTravelPath(map, locations);
        initAutoZoomControl(map, travelPath);
    }

    function initAutoZoomControl(map, travelPath) {
        var container;
        var button;

        function handleButtonClick(event) {
            autoZoomEnabled = true;
            fitBounds(map, travelPath, autoZoomEnabled);
            L.DomUtil.addClass(container, 'hidden');
        }

        function handleZoomEnd(event) {
            if(autoZoomInProgress) {
                autoZoomInProgress = false;
            }
            else {
                autoZoomEnabled = false;
                L.DomUtil.removeClass(container, 'hidden');
            }
        }

        L.Control.AutoZoom = L.Control.extend({
            onAdd: function(map) {
                container = L.DomUtil.create('div', 'auto-zoom-container hidden leaflet-bar leaflet-control');
                button = L.DomUtil.create('a', 'auto-zoom', container);
                button.innerHTML = 'Re-activate auto-zoom';
                button.role = "button";
                button.href = "#";

                L.DomEvent.disableClickPropagation(button);

                L.DomEvent.on(button, 'click', handleButtonClick);
                map.on("zoomend", handleZoomEnd);
                
                return container;
            },

            onRemove: function(map) {
                L.DomEvent.off(button, 'click', handleButtonClick);
                map.off('zoomend', handleZoomEnd);
            }
        });

        L.control.autozoom = function(opts) {
            return new L.Control.AutoZoom(opts);
        };

        L.control.autozoom({ position: 'bottomleft' }).addTo(map);
    }

    function initialize(initialLocations) {
        initMap(initialLocations);
        registerEventHandlers();
    }

    function registerEventHandlers() {
        $(document).on('new-location', function(event, location) {
            addTravelPathLocation(travelPath, location);
            fitBounds(map, travelPath, autoZoomEnabled);
        });
    }

    return {
        initialize: initialize
    };
}());

