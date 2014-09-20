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

object World extends JFXApp {
  val CELL_SIZE = 25
  
  val bot = EmptyRoomBot
  
  stage = new JFXApp.PrimaryStage {
    width = CELL_SIZE * (bot.maze.width) 
    height = CELL_SIZE * (bot.maze.height + 1)
    scene = new Scene
  }
  
  val keyFrame = KeyFrame(10 ms, onFinished = {
    event: ActionEvent =>
      if (bot.canMove) {
        bot.step()
        stage.scene().content = botboxes
      }
  })
  
  def makeKeyFrame(step: Seq[Rectangle]) =
    KeyFrame(1 ms, onFinished = {
    event: ActionEvent =>
      stage.scene = new Scene {content = step}
  })
  
  val pongAnimation = new Timeline {
    keyFrames = Seq(keyFrame)
    //keyFrames = steps map makeKeyFrame
    //cycleCount = steps.length
    cycleCount = INDEFINITE
  }
  
  pongAnimation.play()
  
  def cellColor(pos: Position) = {
    if (bot.maze.isWall(pos))
      Color.Blue
    else if (pos == bot.position)
      Color.Red
    else if (bot.visited contains pos)
      Color.Grey
    else
      Color.White
  }
  
  def botboxes = {
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

