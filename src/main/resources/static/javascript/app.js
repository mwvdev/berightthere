requirejs.config({
    baseUrl: '/webjars',
    paths: {
        eventEmitter: 'eventEmitter/EventEmitter.min',
        leaflet: 'leaflet/leaflet',
        'map.accuracy': '../javascript/map.accuracy',
        'map.autofocus': '../javascript/map.autofocus',
        'map.core': '../javascript/map.core',
        'map.events': '../javascript/map.events',
        'map.state': '../javascript/map.state',
        'utils': '../javascript/utils',
        'map.visitorlocation': '../javascript/map.visitorlocation',
        sockjs: 'sockjs-client/dist/sockjs.min',
        stomp: 'webstomp-client/dist/webstomp.min',
        websocket: '../javascript/websocket'
    },
    shim: {
        stomp: {
            exports: 'Stomp'
        }
    }
});

requirejs([
    "eventEmitter",
    "leaflet",
    "map.accuracy",
    "map.autofocus",
    "map.core",
    "map.events",
    "map.state",
    "utils",
    "map.visitorlocation",
    "sockjs",
    "stomp",
    "websocket"
]);
