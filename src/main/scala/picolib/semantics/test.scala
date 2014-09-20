package picolib.semantics

import java.io.File
import picolib.maze.Maze
import picolib.maze.Position
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.stage.Stage
import scala.collection.mutable.MutableList
import scalafx.animation.KeyFrame
import scalafx.util.Duration
import scalafx.animation.Timeline
import scalafx.animation.Timeline._
import scalafx.animation.SequentialTransition
import scalafx.animation.PauseTransition
import scalafx.event.ActionEvent
import scala.language.postfixOps
import scalafx.scene.control.Button
import scalafx.scene.layout.VBox
import scalafx.scene.layout.HBox
import scalafx.scene.layout.Pane

object World extends JFXApp {
  val CELL_SIZE = 25
  
  val bot = EmptyRoomBot
  
  private val runButton = new Button("Run") {
    onAction = {e: ActionEvent ⇒ run()}
  }
  
  private val stopButton = new Button("Stop") {
    onAction = {e: ActionEvent ⇒ stop()}
  }
  
  private val stepButton = new Button("Step") {
    onAction = {e: ActionEvent ⇒ step()}
  }

  private val resetButton = new Button("Reset") {
    onAction = { e: ActionEvent ⇒ reset() }
  }
  
  private val buttonPane = 
    new HBox{content=List(runButton, stopButton, stepButton, resetButton)}
  private val mazePane = new Pane{content=botboxes}
  
  stage = new JFXApp.PrimaryStage {
    width = CELL_SIZE * (bot.maze.width) 
    height = buttonPane.height.value + CELL_SIZE * (bot.maze.height + 1)
    scene = new Scene {
      content = new VBox {content = List(buttonPane, mazePane)}
    }
  }

  // build an animation from the keyframe
  val botAnimation = new Timeline {
    keyFrames = Seq(
      // a keyframe that steps the bot, then displays the results
      KeyFrame(10 ms, onFinished = {
        event: ActionEvent ⇒
          if (bot.canMove) {
            bot.step()
            //stage.scene().content = botboxes
            mazePane.content = botboxes
          }
      }))
  }
  
  def run() = {
    botAnimation.cycleCount = INDEFINITE
    botAnimation.play()
  }
  
  def stop() = {
    botAnimation.stop()
  }
  
  def step() = {
    botAnimation.cycleCount = 1
    botAnimation.play()
  }
  
  def reset() = {
    stop()
    bot.reset()
    mazePane.content = botboxes
  }
  
  /**
   * Make a GUI version of the maze
   */
  def botboxes = {
    /**
     * Make a GUI cell at a given position, colored according to its contents
     */
    def makeCell(pos: Position) = {
      new Rectangle {
        x = CELL_SIZE * pos.x
        y = CELL_SIZE * pos.y
        width = CELL_SIZE
        height = CELL_SIZE
        fill = cellColor(pos)
      }
    }
    
    bot.maze.positions map makeCell
  }
  
  /**
   * What color should a cell be?
   */
  def cellColor(pos: Position) = {
    if (bot.maze.isWall(pos))
      Color.Blue
    else if (pos == bot.position)
      Color.Black
    else if (bot.visited contains pos)
      Color.Grey
    else
      Color.White
  }
  
}

object EmptyRoomBot extends Picobot(
    Maze("resources" + File.separator + "empty.txt"),
    List( Rule(State("0"), 
               Surroundings(Anything, Anything, Open, Anything), 
               West,
               State("0")),
               
          Rule(State("0"), 
               Surroundings(Open, Anything, Blocked, Anything), 
               North,
               State("1")), 
               
          Rule(State("0"), 
               Surroundings(Blocked, Open, Blocked, Anything), 
               South,
               State("2")), 
               
          Rule(State("1"), 
               Surroundings(Open, Anything, Anything, Anything), 
               North,
               State("1")),
        
          Rule(State("1"), 
               Surroundings(Blocked, Anything, Anything, Open), 
               South,
               State("2")),
        
          Rule(State("2"), 
               Surroundings(Anything, Anything, Anything, Open), 
               South,
               State("2")),
        
          Rule(State("2"), 
               Surroundings(Anything, Open, Anything, Blocked), 
               East,
               State("3")),
               
          Rule(State("3"), 
               Surroundings(Open, Anything, Anything, Anything), 
               North,
               State("3")),
        
          Rule(State("3"), 
               Surroundings(Blocked, Open, Anything, Anything), 
               East,
               State("2"))            
        ))
