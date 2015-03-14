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

//window.onload = canvasApp();
window.addEventListener('load',canvasApp(),false)


function runGame() {
    window.location.reload();
    window.open(theCanvas.toDataURL(),"canvasImage", "left=0,top=0,widith=" +theCanvas.width+
        ",height="+theCanvas.height+",toolbar=0,resizeable=0");
}

function drawScreen() {
    console.log("2");
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
    var key = String.fromCharCode(e.keyCode).toLowerCase();
    letters.push(key);
    drawScreen();
}

function canvasApp() {
    if(!canvasSupport()){
        return;
    }

    theCanvas = document.getElementById("canvas");
    theRunBtn = document.getElementById("startGame");
    theRunBtn.addEventListener('click',runGame);
    window.addEventListener('keypress',onKeyPressed,true);

    Debugger.log(theCanvas);

    if (!theCanvas || !theCanvas.getContext) {
        Debugger.log("0");

        return;
    }

    Debugger.log("1");

    drawScreen(theCanvas);

    Debugger.log("3");

}

function canvasSupport() {
    return Modernizr.canvas;
}



