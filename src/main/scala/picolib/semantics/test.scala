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


//import scala.collection.mutable.M

object World extends JFXApp {
  val CELL_SIZE = 25
  
  val mazeFilename = "resources" + File.separator + "empty.txt"

  val maze = Maze(io.Source.fromFile(mazeFilename).getLines().toList)

  val rules = List( Rule(State("0"), 
                               Surroundings(Anything, Anything, Open, Anything), 
                               West,
                               State("0")),
                               
                          Rule(State("0"), 
                               Surroundings(Open, Anything, Blocked, Anything), 
                               North,
                               State("1")), 
                               
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
        )

  val bot = new Picobot(maze, rules)  
  
  /*val keyFrame = KeyFrame(10 ms, onFinished = {
    event: ActionEvent =>
      checkForCollision()
      val horzPixels = if (movingRight) 1 else -1
      val vertPixels = if (movingDown) 1 else -1
      centerBallX() = centerBallX.value + horzPixels
      centerBallY() = centerBallY.value + vertPixels
  })*/
  
  /*val steps = MutableList.empty[Seq[Rectangle]]
  steps += botboxes
  while(bot.canMove) {
    bot.step()
    steps += botboxes
  }*/
  
  stage = new JFXApp.PrimaryStage {
    //title = "Hello World"
    width = CELL_SIZE * (maze.width) 
    height = CELL_SIZE * (maze.height + 1)
    scene = new Scene {
      //content = botboxes  
    }
  }
  
  //stage.scene = new Scene {content = botboxes}
  
  //val timeline = new scalafx.animation.Timeline
  //steps map (timeline.keyFrames.add(new KeyFrame(new Duration(500), {stage.scene = new Scene{content=_})))
  
  /*val keyframes = for (step ← steps) yield at(1 s) {
    Set(println("hi"))
  }*/
  
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
  
  val seqTransition = new PauseTransition(1 s) {
    /*onFinished = {
      bot.step()
      stage.scene = new Scene {content = botboxes}
    }*/
    
    /*repeatCount: Timeline.INDEFINITE
      content: [
        PauseTransition {
            duration: 1s
            action: function():Void {
                fillColor = Color.BLUE;
            }
        },
        PauseTransition {
            duration: 1s
            action: function():Void {
                fillColor = Color.RED;
            }
        }
      ]*/
    }
  
  def cellColor(pos: Position) = {
    if (maze.isWall(pos))
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
    maze.positions map makeCell
  }
}

