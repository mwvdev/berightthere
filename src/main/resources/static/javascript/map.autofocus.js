define(["leaflet", "map.core", "map.events"], function(L, mapCore, mapEvents) {
    var button;
    var autoFocusEnabled = true;

    var eventEmitter = mapCore.getEventEmitter();

    var autoFocusingEvents = [
        mapEvents.viewport.boundedMarkerCreated,
        mapEvents.viewport.boundedMarkerRemoved,
        mapEvents.location.singleReceived,
        mapEvents.location.allReceived
    ];
    autoFocusingEvents.forEach(function(event) {
        eventEmitter.addListener(event, fitBoundsWhenAutoFocusing);
    });

    function fitBoundsWhenAutoFocusing() {
        if(autoFocusEnabled) {
            eventEmitter.emit(mapEvents.viewport.staleBounds);
        }
    }

    function handleClick() {
        autoFocusEnabled = !autoFocusEnabled;
        fitBoundsWhenAutoFocusing();

        if(autoFocusEnabled) {
            L.DomUtil.addClass(button, 'focusing');
        }
        else {
            L.DomUtil.removeClass(button, 'focusing');
        }
    }

    L.Control.AutoFocus = L.Control.extend({
        onAdd: function() {
            var container = L.DomUtil.create('div', 'leaflet-bar leaflet-control');
            button = L.DomUtil.create('a', 'auto-focus focusing', container);
            button.innerHTML = '<i class="icon-binoculars" />';
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

    L.control.autofocus = function(options) {
        return new L.Control.AutoFocus(options);
    };

    L.control.autofocus({ position: 'bottomleft' }).addTo(mapCore.getInstance());
});
