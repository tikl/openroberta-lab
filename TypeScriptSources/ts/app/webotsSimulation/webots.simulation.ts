import * as $ from 'jquery';

const URL = "wss://sim.open-roberta.org/1999/session?url=file:///home/cyberbotics/simulation/worlds/nao_room.wbt";

interface StreamingViewer extends HTMLElement {
    connect(url: string, mode: string, broadcast: boolean, mobileDevice: boolean, callback: Function, disconnectCallback: Function);

    disconnect();

    sendMessage(message: string);

    hideToolbar();

    showToolbar();
}

const runButton = document.querySelector<HTMLButtonElement>('#simControl');
const resetButton = document.querySelector<HTMLButtonElement>('#simResetPose');

let streamingViewer: StreamingViewer;
let sourceCode;
let connected = false;

function connect() {
    streamingViewer.connect(URL, "x3d", false, false, () => {
        connected = true;
        sendController(sourceCode);
    }, () => {
        connected = false;
        console.log("disconnected");
    });
    streamingViewer.hideToolbar();
}

function sendController(sourceCode) {
    let message = {
        name: "supervisor",
        message: "upload:" + sourceCode
    };
    streamingViewer.sendMessage('robot:' + JSON.stringify(message));
}

function reset() {
    console.log("sending reset");
    streamingViewer.sendMessage('robot:{"name":"supervisor","message":"reset"}');
}

function createStreamingElement() {
    streamingViewer = document.querySelector('#webotsDiv');
    if (streamingViewer) {
        return;
    }

    streamingViewer = document.createElement('webots-streaming') as StreamingViewer;
    streamingViewer.id = 'webotsDiv';
    document.getElementById('simDiv').prepend(streamingViewer);
}

async function loadWebotsSources() {
    if (document.querySelector('script[src="https://cyberbotics.com/wwi/R2021b/WebotsStreaming.js"]')) {
        return;
    }

    await new Promise((resolve, reject) => {
        let script = document.createElement('script');
        script.onload = resolve;
        script.type = "module";
        script.src = "https://cyberbotics.com/wwi/R2021b/WebotsStreaming.js";
        document.head.appendChild(script);
    });
}

async function glmLoaded() {
    if (window.hasOwnProperty("glm")) {
        return;
    }
    await new Promise((resolve, _) => {
        document.querySelector('script[src="https://git.io/glm-js.min.js"]').addEventListener("load", resolve);
    });
}

export function disconnect() {
    // Bug with webots, it doesnt remove its resize event listener
    window.onresize = undefined;
    streamingViewer.disconnect();
}

export async function init(sc) {
    $('#simEditButtons, #canvasDiv, #simRobot, #simValues').hide();
    $('#webotsDiv, #simButtons').show();

    sourceCode = sc;

    if (connected) {
        return;
    }

    await loadWebotsSources()
    createStreamingElement();
    await glmLoaded();
    connect();
}

export function resetPose() {
    console.log("sending reset");
    streamingViewer.sendMessage('robot:{"name":"supervisor","message":"reset"}');
}

export function stopProgram() {
    streamingViewer.sendMessage('pause');
}

export function run(sc) {
    streamingViewer.sendMessage('pause');

    sourceCode = sc;
    sendController(sourceCode);

    streamingViewer.sendMessage('real-time:-1');
}