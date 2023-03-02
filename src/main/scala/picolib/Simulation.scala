package picolib

import picolib.maze._
import picolib.semantics._
import picolib.display._

class TextSimulation(maze: Maze, rules: List[Rule]) extends App:
  object bot extends Picobot(maze, rules) with TextDisplay
  bot.run()
