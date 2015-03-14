/**
 * Created by ali on 12/10/14.
 */
window.onload = canvasApp();

var theCanvas;
var theRunBtn;

function runGame() {
    window.location.reload();
    window.open(theCanvas.toDataURL(),"canvasImage", "left=0,top=0,widith=" +theCanvas.width+
        ",height="+theCanvas.height+",toolbar=0,resizeable=0");
}

function drawScreen(canvas) {
    console.log("2");
    var context = canvas.getContext("2d");

    // background
    context.fillStyle = "#ffffaa";
    context.fillRect(0,0,500,300);

    // text
    context.fillStyle = "#000000";
    context.font = "20px _sans";
    context.textBaseline = "top";
    context.fillText("Hello Tetris!!!",195,80);

    //image

    // box
    context.strokeStyle = "#000000";
    context.strokeRect(5,5,490,290);
}

function canvasApp() {
    if(!canvasSupport()){
        return;
    }

    theCanvas = document.getElementById("canvas");
    theRunBtn = document.getElementById("startGame");
    theRunBtn.addEventListener('click',runGame);

    console.log(theCanvas)

    if (!theCanvas || !theCanvas.getContext) {
        console.log("0");

        return;
    }

    console.log("1");

    drawScreen(theCanvas);

    console.log("3");

}

function canvasSupport() {
    return Modernizr.canvas;
}



