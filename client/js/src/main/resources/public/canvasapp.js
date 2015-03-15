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

// Canvas start from top-left, and moves towards bottom-right.
var theCanvas;
var theStartBtn, theStopBtn;
var running=false;
var theTimer;
var NUM_COLUMNS = 10; // total columns on board
var NUM_ROWS = 27; // total rows on board
var UNIT=30; // square side length
var WIDTH=NUM_COLUMNS*UNIT;
var HEIGHT=NUM_ROWS*UNIT;


var thePieces = [[[[0, 0], [1, 0], [0, 1], [1, 1]]],  // square (only needs one)
        rotations([[0, 0], [-1, 0], [1, 0], [0, -1]]), // T
        [[[0, 0], [-1, 0], [1, 0], [2, 0]], [[0, 0], [0, -1], [0, 1], [0, 2]]], // I (horizontal and vertical)
        rotations([[0, 0], [0, -1], [0, 1], [1, 1]]), // L
        rotations([[0, 0], [0, -1], [0, 1], [-1, 1]]), // inverted L
        rotations([[0, 0], [1, 0], [0, -1], [-1, -1]]), // S
        rotations([[0, 0], [-1, 0], [0, -1], [1, -1]])] // Z
var theColors = ['DarkGreen', 'DarkBlue', 'DarkRed', 'Gold', 'Purple',
    'OrangeRed', 'LightSkyBlue']
var theRows = []; // 2 dimensional array NUM_ROWS x NUM_COLUMNS, row-wise, row[0] is on top.
var theTick = 300; // miliseconds

window.addEventListener('load',canvasApp,false)

function start() {
    if(running) return;
    else{
        theRows=generateRows(NUM_ROWS,NUM_COLUMNS,3);
        running=true;
        tick();
    }
}
function stop(){
    running=false;
}

function pause(){
    running=!running;
    tick();
}

function tick() {
    if (theTimer !== null) clearTimeout(theTimer);
    if (running) {
        drawScreen();
        theTimer = setTimeout(tick, theTick);
    }
}

function drawScreen() {
    console.log("drawScreen...");
    var context = theCanvas.getContext("2d");

    // fill background
    context.fillStyle = "LightGray";
    context.fillRect(0,0,WIDTH, HEIGHT);

    // draw outline box
    context.strokeStyle = "#000000";
    context.strokeRect(0,0,WIDTH,HEIGHT);

    // draw grids
    for(var i=0;i<theRows.length;i++){
        for(var j=0;j<NUM_COLUMNS;j++){
            r=theRows[i][j];
            if(r!==null){
                context.fillStyle = r.color;
                context.fillRect(r.x, r.y, r.w, r.h);
                context.fillStyle = "Black";
                context.strokeRect(r.x, r.y, r.w, r.h);
            }
        }
    }
}

function onKeyPressed(e){
    Debugger.log("onKeyPressed: keyCode="+e.keyCode);
    var key = String.fromCharCode(e.keyCode).toLowerCase();
    Debugger.log("onKeyPressed: char="+key);
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
    theStartBtn = document.getElementById("startGame");
    theStartBtn.addEventListener('click',start);
    theStopBtn = document.getElementById("stopGame");
    theStopBtn.addEventListener('click',stop);
    thePauseBtn = document.getElementById("pauseGame");
    thePauseBtn.addEventListener('click',pause);
    window.addEventListener('keypress',onKeyPressed,false);
    window.addEventListener('keydown',onKeyDown,false);
    window.addEventListener('keyup',onKeyUp,false);

    resizeCanvas();

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

function resizeCanvas() {
    theCanvas.width = WIDTH;
    theCanvas.height = HEIGHT;
}

// utils
// given an array of point, where x=point[0] and y=point[1], return an array of array
// for 4 rotations(anticlockwise).
function rotations(array) {
    rotate1 = array.map(function(point){
        return [-point[1],point[0]];
    });
    rotate2 = array.map(function(point){
        return [-point[0],-point[1]];
    });
    rotate3 = array.map(function(point){
        return [point[1],-point[0]]
    });
    return [array, rotate1, rotate2, rotate3];
}


function generateRows(numRow,numCol,randRow){
    rows=new Array(numRow)
        .join().split(',')
        .map(function(item, index){
            return new Array(numCol).join().split(',')
                .map(function(e,i){
                    return null;
                });
        });

    Debugger.log(rows);

    for(var i=0;i<randRow;i++){
        for(var j=0;j<NUM_COLUMNS;j++){
            if(Math.round(Math.random())>0){
                var c=theColors[Math.floor(Math.random() * theColors.length)];
                rect={x:j*UNIT,y:(NUM_ROWS-i-1)*UNIT,w:UNIT,h:UNIT,color:c};
                rows[NUM_ROWS-1-i][j]=rect;
            }
        }
    }
    return rows;
}

