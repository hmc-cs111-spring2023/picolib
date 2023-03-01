package picolib

import picolib.maze._
import picolib.semantics._
import picolib.display._
import scalafx.application.JFXApp3


class TextSimulation(maze: Maze, rules: List[Rule]) extends App:
    object bot extends Picobot(maze, rules) with TextDisplay
    bot.run()

class GUISimulation(maze: Maze, rules: List[Rule]) extends JFXApp3:

  override def start() = { 
    object bot extends Picobot(maze, rules) with TextDisplay with GUIDisplay
    stage = bot.mainStage 
  }
