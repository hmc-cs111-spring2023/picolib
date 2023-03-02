package picolib

import picolib.maze._
import picolib.semantics._
import picolib.display._
import scalafx.application.JFXApp3

/** A main program the can run a list of rules in a maze and display the
  * simulation details in the terminal
  */
class TextSimulation(maze: Maze, rules: List[Rule]) extends App:
  object bot extends Picobot(maze, rules) with TextDisplay
  bot.run()

/** A main program the can run a list of rules in a maze and display the
  * simulation details in a GUI panel
  */
class GUISimulation(maze: Maze, rules: List[Rule]) extends JFXApp3:

  override def start() = {
    object bot extends Picobot(maze, rules) with TextDisplay with GUIDisplay
    stage = bot.mainStage
  }
