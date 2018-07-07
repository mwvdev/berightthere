requirejs.config({
    baseUrl: '/webjars',
    paths: {
        eventEmitter: 'eventEmitter/EventEmitter.min',
        leaflet: 'leaflet/leaflet',
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

requirejs(["eventEmitter", "leaflet", "map.autofocus", "map.core", "map.events", "sockjs", "stomp", "websocket"]);
