package picolib.semantics

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.stage.Screen
import scalafx.scene.shape.Shape

object Test extends JFXApp {

  stage = new JFXApp.PrimaryStage {
    title = "Blox viewer"
    val bounds = Screen.primary.visualBounds
    width = bounds.maxX
    height = bounds.maxY
    scene = new Canvas(for (i ← 0 to 4) yield new MazeCell(0, i * MazeCell.SIZE))
  }

  //lazy val drawingScene = 
    
}

class Canvas(blocks: Iterable[Shape]) extends Scene {
  fill = Color.White
  content = blocks
}