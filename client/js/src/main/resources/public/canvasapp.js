/**
 * Created by ali on 12/10/14.
 */

var Debugger = function(){};
Debugger.log = function(message) {
    try{
        console.log(message);
    }catch(exception){
        console.error(exception);
    }
}

var theCanvas;
var theRunBtn;
var letters=["Hello Tetris!!!"];

window.onload = canvasApp();
window.addEventListener('load',canvasApp(),false)


function runGame() {
    window.location.reload();
    window.open(theCanvas.toDataURL(),"canvasImage", "left=0,top=0,widith=" +theCanvas.width+
        ",height="+theCanvas.height+",toolbar=0,resizeable=0");
}

function drawScreen() {
    console.log("drawScreen...");
    var context = theCanvas.getContext("2d");

    // background
    context.fillStyle = "#ffffaa";
    context.fillRect(0,0,500,300);

    // text
    context.fillStyle = "#000000";
    context.font = "20px _sans";
    context.textBaseline = "top";
    context.fillText(letters.concat(),195,80);

    //image

    // box
    context.strokeStyle = "#000000";
    context.strokeRect(5,5,490,290);
}

function onKeyPressed(e){
    Debugger.log("onKeyPressed: keyCode="+e.keyCode);
    var key = String.fromCharCode(e.keyCode).toLowerCase();
    letters.push(key);
    drawScreen();
}

// special keys only triggered by onKeyDown
function onKeyDown(e){
    Debugger.log("onKeyDown: keyCode="+ e.keyCode);
    switch(e.keyCode){
        case 32:
            Debugger.log("Space");
            break;
        case 37:
            Debugger.log("Left");
            break;
        case 38:
            Debugger.log("Up");
            break;
        case 39:
            Debugger.log("Right");
            break;
        case 40:
            Debugger.log("Down");
            break;
        default:
            break;
    }
}

function onKeyUp(e) {
    Debugger.log("onKeyUp: keyCode=" + e.keyCode);
}

function canvasApp() {
    if(!canvasSupport()){
        return;
    }

    theCanvas = document.getElementById("canvas");
    theRunBtn = document.getElementById("startGame");
    theRunBtn.addEventListener('click',runGame);
    window.addEventListener('keypress',onKeyPressed,false);
    window.addEventListener('keydown',onKeyDown,false);
    window.addEventListener('keyup',onKeyUp,false);

    Debugger.log(theCanvas);

    if (!theCanvas || !theCanvas.getContext) {
        Debugger.log("theCanvas or context does not exist");

        return;
    }

    drawScreen(theCanvas);

    Debugger.log("end of canvasApp");

}

function canvasSupport() {
    return Modernizr.canvas;
}



