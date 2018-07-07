define(["leaflet", "map.core", "map.events"], function(L, mapCore, mapEvents) {
    var button;
    var autoFocusEnabled = true;

    var eventEmitter = mapCore.getEventEmitter();
    eventEmitter.addListener(mapEvents.location.received, function(location) {
        fitBoundsWhenAutoFocusing();
    });

    function fitBoundsWhenAutoFocusing() {
        if(autoFocusEnabled) {
            mapCore.fitBounds()
        };
    }

    function handleClick(event) {
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

    L.control.autofocus({ position: 'bottomleft' }).addTo(mapCore.getInstance());
});
