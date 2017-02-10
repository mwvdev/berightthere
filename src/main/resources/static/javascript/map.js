var beRightThereMap = (function() {
    var map;
    var locations;
    var travelPath;

    function createTravelPath(map, locations) {
        var newTravelPath = new google.maps.Polyline({
            strokeColor: '#FF0000',
            strokeOpacity: 1.0,
            strokeWeight: 3
        });
        newTravelPath.setMap(map);
        appendTravelPath(newTravelPath, locations);
        return newTravelPath;
    }

    function appendTravelPath(travelPath, locations) {
        locations.forEach(function(location) {
            addTravelPathLocation(travelPath, location);
        });
    }
    
    function addTravelPathLocation(travelPath, location) {
        travelPath.getPath().push(new google.maps.LatLng(location.latitude, location.longitude));
    }

    function googleMapsCallback() {
        map = new google.maps.Map(document.getElementById('map'), {
            center: {lat: 55.676, lng: 12.568},
            zoom: 8
        });
        
        travelPath = createTravelPath(map, locations);
        locations = null;
    }

    function initialize(initialLocations) {
        locations = initialLocations;
        registerEventHandlers();
    }

    function registerEventHandlers() {
        $(document).on('new-location', function(event, location) {
            addTravelPathLocation(travelPath, location);
        });
    }

    return {
        googleMapsCallback: googleMapsCallback,
        initialize: initialize
    };
}());

