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
var theStartBtn, theStopBtn, thePauseBtn;
var running=false;
var theTimer;
var NUM_COLUMNS = 10; // total columns on board
var NUM_ROWS = 27; // total rows on board
var UNIT=30; // square side length
var WIDTH=NUM_COLUMNS*UNIT;
var HEIGHT=NUM_ROWS*UNIT;

var thePiece; // the current piece

var theScore=0; // the accumulated score

var thePieces = [[[[0, 0], [1, 0], [0, 1], [1, 1]]],  // square (only needs one)
        rotations([[0, 0], [-1, 0], [1, 0], [0, -1]]), // T
        [[[0, 0], [-1, 0], [1, 0], [2, 0]], [[0, 0], [0, -1], [0, 1], [0, 2]]], // I (horizontal and vertical)
        rotations([[0, 0], [0, -1], [0, 1], [1, 1]]), // L
        rotations([[0, 0], [0, -1], [0, 1], [-1, 1]]), // inverted L
        rotations([[0, 0], [1, 0], [0, -1], [-1, -1]]), // S
        rotations([[0, 0], [-1, 0], [0, -1], [1, -1]])]; // Z
var theColors = ['DarkGreen', 'DarkBlue', 'DarkRed', 'Gold', 'Purple',
    'OrangeRed', 'LightSkyBlue']
var theRows = []; // 2 dimensional array (NUM_ROWS x NUM_COLUMNS) of colors, row-wise, row[0] is on top.
var theTick = 300; // miliseconds

window.addEventListener('load',canvasApp,false)

function start() {
    if(running) return;
    else{
        theRows=generateRows(NUM_ROWS,NUM_COLUMNS,3);
        theScore=0;
        running=true;
        nextPiece();
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
    if (theTimer != null) clearTimeout(theTimer);
    if (running) {
        dropByOne();
        if(!isGameOver())
            theTimer = setTimeout(tick, theTick);
        drawScreen();
    }
}

function dropByOne(){
    Debugger.log("drop down the current piece by one, and also update the current piece position");
    var moved=move(0,1,0);
    if(!moved){
        stackPiece();
        eliminateRows();
        updateScore();
        drawScreen();
        if(!isGameOver()){
            nextPiece();
        }else{
            stop();
        }
    }
    return moved;
}

function updateScore(){
    Debugger.log("update the score");
    theScore++;
}

function nextPiece(){
    Debugger.log("fetch the next piece");
    var allRotations = thePieces[Math.floor(Math.random()*thePieces.length)];
    var index = Math.floor(Math.random()*rotations.length);
    var clr = theColors[Math.floor(Math.random()*theColors.length)];
    thePiece={rotations: allRotations, rotationIndex:index, color:clr, x:5, y:0, moved:false};
}
function dropAllTheWay(){
    Debugger.log("drop the piece all the way down");
    while (dropByOne()){
        theScore ++;
    }
    if (running && !isGameOver()){
        nextPiece();
    }
    drawScreen();
}
function moveLeft(){
    Debugger.log("move the piece one square left");
    move(-1, 0, 0);
    drawScreen();
}
function moveRight(){
    Debugger.log("move the piece one square right");
    move(1,0,0);
    drawScreen();
}
function rotateClockwise(){
    Debugger.log("rotate the piece clockwise 90 degree");
    move(0,0,1);
    drawScreen();
}
function rotateCounterClockwise(){
    Debugger.log("rotate the piece counter-clockwise 90 degree");
    move(0,0,-1);
    drawScreen();
}

function drawScreen() {
    Debugger.log("drawScreen...");

    var context = theCanvas.getContext("2d");

    // fill background
    context.fillStyle = "LightGray";
    context.fillRect(0,0,WIDTH, HEIGHT);

    // draw outline box
    context.strokeStyle = "#000000";
    context.strokeRect(0,0,WIDTH,HEIGHT);

    // draw grids
    for(var i=0;i<theRows.length;i++){
        var row=theRows[i];
        for(var j=0;j<row.length;j++){
            var r=row[j];
            if(r!=null){
                context.fillStyle = r.color;
                context.fillRect(j*UNIT, i*UNIT, UNIT, UNIT);
                context.fillStyle = "Black";
                context.strokeRect(j*UNIT, i*UNIT, UNIT, UNIT);
            }
        }
    }

    // draw piece
    if(thePiece!=null && thePiece.rotations!=null)
        thePiece.rotations[thePiece.rotationIndex].forEach(function(element,index,array){
            context.fillStyle = thePiece.color;
            context.fillRect(UNIT*(element[0]+thePiece.x),UNIT*(element[1]+thePiece.y),UNIT,UNIT);
            context.fillStyle = "Black";
            context.strokeRect(UNIT*(element[0]+thePiece.x),UNIT*(element[1]+thePiece.y),UNIT,UNIT);
        });
}

// special keys only triggered by onKeyDown
function onKeyDown(e){
    var key = String.fromCharCode(e.keyCode).toLowerCase();
    Debugger.log("onKeyDown: keyCode="+ e.keyCode + ", char="+key);
    switch(e.keyCode){
        case 32:
            Debugger.log("Space");
            dropAllTheWay();
            break;
        case 37:
            Debugger.log("Left");
            moveLeft();
            break;
        case 38:
            Debugger.log("Up");
            rotateCounterClockwise();
            break;
        case 39:
            Debugger.log("Right");
            moveRight();
            break;
        case 40:
            Debugger.log("Down");
            rotateClockwise();
            break;
        default:
            break;
    }
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
    window.addEventListener('keydown',onKeyDown,false);

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

/////////// UTILS ////////////
// given an array of point, where x=point[0] and y=point[1], return an array of array
// for 4 rotations(counter-clockwise).
function rotations(array) {
    var rotate1 = array.map(function(point){
        return [-point[1],point[0]];
    });
    var rotate2 = array.map(function(point){
        return [-point[0],-point[1]];
    });
    var rotate3 = array.map(function(point){
        return [point[1],-point[0]]
    });
    return [array, rotate1, rotate2, rotate3];
}

function generateRows(numRow,numCol,randRow){
    var rows=new Array(numRow)
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
                rows[NUM_ROWS-1-i][j]={color:c};
            }
        }
    }
    return rows;
}

/**
 * Take the intended movement in x, y and rotation and checks to see if the
 * movement is possible. If it is, make this movement and returns true.
 * The movement is done by updating the basePosition
 * <p/>
 * This does not move the block!
 *
 * @param {Number} deltaX positive means moveing right.
 * @param {Number} deltaY
 * @param {Number} deltaRotation, positive is counterClockWise, negative is clockWise.
 * @return {Boolean} true if move succeed; false if cannot move
 */
function move(deltaX, deltaY, deltaRotation) {
    if(isGameOver() || !running){
        return false;
    }

    /*
     * Ensures that the rotation will always be a possible formation (as
     * opposed to null) by altering the intended rotation so that it stays
     * within the bound of the rotation array.
     */
    var potentialIndex = (thePiece.rotationIndex + deltaRotation)
        % thePiece.rotations.length;
    if(potentialIndex<0)
        potentialIndex+=thePiece.rotations.length;
    var potential = thePiece.rotations[potentialIndex];
    /*
     * For each individual block in the piece, check if the intended move
     * will put the block in any occupied space
     */
    var canMove=potential.every(function(p,index,array){
        return isEmptyAt(p[0] + deltaX + thePiece.x, p[1] + deltaY + thePiece.y);
    });
    if (canMove) {
        thePiece.x += deltaX;
        thePiece.y += deltaY;
        thePiece.rotationIndex = potentialIndex;
    }
    return canMove;
}

function stackPiece(){
    Debugger.log("parking the current piece on the stack.");
    thePiece.rotations[thePiece.rotationIndex].forEach(function(p,index,array){
       var x=p[0]+thePiece.x;
       var y=p[1]+thePiece.y;
       theRows[y][x]={color:thePiece.color};
    });
}

function eliminateRows(){
    Debugger.log("eliminate the filled rows if any.");
    var bound=computeBound(thePiece.rotations[thePiece.rotationIndex]);
    var rowsAffected=range(bound.miny,bound.maxy).map(function(row, index){
        return thePiece.y+row;
    });
    if(rowsAffected.length==0){
        rowsAffected[0]=thePiece.y;
    }

    // array of rows to remove, e.g., [3,5,11]
    var rowsToRemove=rowsAffected.filter(function(rowIndex, index, array){
        var row=theRows[rowIndex];
        return row.every(function(col,ind,arr){
           return col!==null;
        });
    });

    // remove rowsToRemove
    var newRows=theRows.filter(function(row,index,rows){
        return rowsToRemove.indexOf(index)<0;
    });

    // unshift rows
    for(var i=0;i<rowsToRemove.length;i++){
        var row = new Array(NUM_COLUMNS).join().split(',')
            .map(function(_){
                return null;
            });
        newRows.unshift(row)
    }

    theRows=newRows;
    //  drawScreen
//    drawScreen();
}

/**
 * Given array of points, return the (minx,miny,max,maxy)
 * @param points an array of point, each point is an array[2], where array[0]=x, array[1]=y.
 */
function computeBound(points){
    var minx,miny,maxx,maxy;
    points.forEach(function(p){
        if(minx===undefined){
            minx=p[0];
        }
        if(miny===undefined){
            miny=p[1];
        }
        if(maxx===undefined){
            maxx=p[0];
        }
        if(maxy===undefined){
            maxy=p[1];
        }

        if(minx>p[0]) minx=p[0];
        if(miny>p[1]) miny=p[1];
        if(maxx<p[0]) maxx=p[0];
        if(maxy<p[1]) maxy=p[1];
    });
    return {minx:minx,miny:miny,maxx:maxx,maxy:maxy};
}

/**
 * Given row and column, check if it is in the bounds of the board and
 * currently is empty.
 */
function isEmptyAt (col,row){
    if (col < 0 || col >= NUM_COLUMNS) return false
    else if (row < 1) return true
    else if (row >= NUM_ROWS) return false
    else return theRows[row][col] === null
}

/**
 * The game is over when there is a piece extending into the second row
 * from the top
 */
function isGameOver(){
    Debugger.log("test if the game is over");

    return theRows!=null && theRows[1]!=null && theRows[1].some(function(element, index, array){
        return element!=null;
    });
}

function range(start, end) {
    var foo = [];
    for (var i = start; i <= end; i++) {
        foo.push(i);
    }
    return foo;
}