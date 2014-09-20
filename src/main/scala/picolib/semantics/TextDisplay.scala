package picolib.semantics

import picolib.maze.Maze
import picolib.maze.Position

trait TextDisplay extends Picobot {
  
  abstract override def step() = {
    println()
    super.step()
    println(this)
  }
  
  abstract override def run() = {
    println(this)
    super.run()
  }
  
}
