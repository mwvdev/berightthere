define(["leaflet", "map.core", "map.events", "utils"], function(L, mapCore, mapEvents, utils) {
    if(!navigator.geolocation) {
        return;
    }

    let button;
    let watchId = null;
    const visitorPositionMarker = L.marker([0, 0]);

    const eventEmitter = mapCore.getEventEmitter();

    function handleClick() {
        if (followingVisitorLocation()) {
            unfollowVisitorLocation();
        }
        else {
            followVisitorLocation();
        }
    }

    function followingVisitorLocation() {
        return watchId != null;
    }

    function followVisitorLocation() {
        const positionOptions = {
            enableHighAccuracy: true,
            maximumAge: 15000
        };

        watchId = navigator.geolocation.watchPosition(function (position) {
            const location = position.coords;
            visitorPositionMarker.setLatLng(utils.mapToLatLng(location)).addTo(mapCore.getInstance());

            eventEmitter.emit(mapEvents.viewport.boundedMarkerCreated, visitorPositionMarker);
        }, function() {
            visitorPositionMarker.remove();
        }, positionOptions);

        L.DomUtil.addClass(button, "following");
    }

    function unfollowVisitorLocation() {
        navigator.geolocation.clearWatch(watchId);
        watchId = null;

        visitorPositionMarker.remove();
        eventEmitter.emit(mapEvents.viewport.boundedMarkerRemoved, visitorPositionMarker);

        L.DomUtil.removeClass(button, "following");
    }

    L.Control.VisitorLocation = L.Control.extend({
        onAdd: function() {
            const container = L.DomUtil.create('div', 'leaflet-bar leaflet-control');
            button = L.DomUtil.create('a', 'visitor-location', container);
            button.innerHTML = '<i class="icon-crosshairs" />';
            button.role = "button";
            button.href = "#";

            L.DomEvent.disableClickPropagation(button);
            L.DomEvent.on(button, 'click', handleClick);

            return container;
        },
        onRemove: function() {
            L.DomEvent.off(button, 'click', handleClick);
        }
    });

    L.control.visitorlocation = function(options) {
        return new L.Control.VisitorLocation(options);
    };

    L.control.visitorlocation({ position: 'topright' }).addTo(mapCore.getInstance());
});
