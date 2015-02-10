/**
 * Created by ali on 12/10/14.
 */
window.onload = canvasApp();

function canvasApp(){
  var theCanvas = document.getElementById("canvas");

  if(!theCanvas || !theCanvas.getContext){
    return;
  }

  drawScreen();
  function drawScreen(){

  }

}

function canvasSupport(){
  return Modernizr.canvas;
}



