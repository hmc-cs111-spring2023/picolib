package picolib.display

import picolib.maze._
import picolib.semantics._

import scalafx.Includes._
import scalafx.animation.Timeline
import scalafx.animation.Timeline._
import scalafx.animation.KeyFrame
import scalafx.event.ActionEvent
import scala.language.postfixOps
import scalafx.scene.layout.Pane
import scalafx.scene.shape.Rectangle
import picolib.maze.Position
import scalafx.scene.paint.Color
import scalafx.scene.control.Button
import scalafx.scene.layout.HBox
import scalafx.scene.layout.VBox
import scalafx.application.JFXApp3
import scalafx.scene.Scene

/** GUI controls and displays for a Picobot, based on ScalaFX
  */
trait GUIDisplay extends Picobot {
  import GUIDisplay._

  private val runButton = new Button("Run") {
    onAction = { (e: ActionEvent) => run() }
  }

  private val stopButton = new Button("Stop") {
    onAction = { (e: ActionEvent) => stop() }
  }

  private val stepButton = new Button("Step") {
    onAction = { (e: ActionEvent) => step() }
  }

  private val resetButton = new Button("Reset") {
    onAction = { (e: ActionEvent) => reset() }
  }

  // a pane with all the buttons to control the animation
  private val buttonPane =
    new HBox { children = Seq(runButton, stopButton, stepButton, resetButton) }

  // a pane that contains a visualization for each cell in the maze
  private val mazePane = new Pane { children = botboxes }

  // a pane for the controller and the maze
  private val botPane = new VBox { children = Seq(buttonPane, mazePane) }

  /** use this as the main stage for an app */
  val mainStage = new JFXApp3.PrimaryStage {
    width = CELL_SIZE * (maze.width)
    height = buttonPane.height.value + CELL_SIZE * (maze.height + 2)
    scene = new Scene { content = botPane }
  }

  abstract override def step() = {
    stepAnimation.cycleCount = 1
    stepAnimation.play()
  }

  abstract override def reset() = {
    stop()
    super.reset()
    mazePane.children = botboxes
    ()
  }

  abstract override def run() = {
    stepAnimation.cycleCount = Indefinite
    stepAnimation.play()
  }

  def stop() = stepAnimation.stop()

  /** an event that steps the bot and updates the display */
  val stepEvent = { (event: ActionEvent) =>
    if (canMove) {
      super.step()
      mazePane.children = botboxes
      ()
    }
  }

  /** A timeline with one keyframe that steps the bot, then displays the results
    */
  val stepAnimation = new Timeline {
    keyFrames = Seq(KeyFrame(10 ms, onFinished = stepEvent))
  }

  /** Make a GUI version of the maze
    */
  def botboxes = {

    // Make a GUI cell at a given position, colored according to its contents
    def makeCell(pos: Position) =
      new Rectangle {
        x = CELL_SIZE * pos.x
        y = CELL_SIZE * pos.y
        width = CELL_SIZE
        height = CELL_SIZE
        fill = cellColor(pos)
      }

    this.maze.positions map makeCell
  }

  /** What color should a cell be?
    */
  def cellColor(pos: Position) = {
    if (this.maze.isWall(pos))
      WALL_COLOR
    else if (pos == this.position)
      BOT_COLOR
    else if (this.visited contains pos)
      VISITED_COLOR
    else
      BLANK_COLOR
  }
}

object GUIDisplay {
  val CELL_SIZE = 25

  val WALL_COLOR = Color.Blue
  val BOT_COLOR = Color.Black
  val VISITED_COLOR = Color.Grey
  val BLANK_COLOR = Color.White
}
