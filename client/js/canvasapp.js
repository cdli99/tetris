/**
 * Created by ali on 12/10/14.
 */
window.onload = canvasApp();

function canvasApp() {
//    if(!canvasSupport()){
//        return;
//    }

    var theCanvas = document.getElementById("canvas");

    console.log(theCanvas)

    if (!theCanvas || !theCanvas.getContext) {
        console.log("0");

        return;
    }

    console.log("1");

    drawScreen();

    console.log("3");

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
        context.fillText("Hello Tetris!",195,80);

        //image

        // box
        context.strokeStyle = "#000000";
        context.strokeRect(5,5,490,290);
    }

}

function canvasSupport() {
    return Modernizr.canvas;
}



