requirejs.config({
    baseUrl: '/webjars',
    paths: {
        eventEmitter: 'eventEmitter/EventEmitter.min',
        leaflet: 'leaflet/leaflet',
        'map.accuracy': '../javascript/map.accuracy',
        'map.autofocus': '../javascript/map.autofocus',
        'map.core': '../javascript/map.core',
        'map.events': '../javascript/map.events',
        sockjs: 'sockjs-client/sockjs.min',
        stomp: 'stomp-websocket/stomp.min',
        websocket: '../javascript/websocket'
    },
    shim: {
        stomp: {
            exports: 'Stomp'
        }
    }
});

requirejs(["eventEmitter", "leaflet", "map.accuracy", "map.autofocus", "map.core", "map.events", "sockjs", "stomp", "websocket"]);
