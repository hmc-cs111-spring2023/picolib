import picolib._
import picolib.maze._
import picolib.semantics._
import java.io.File

/** This is an intentionally bad internal language, but it uses all the parts of
  * the picolib library that you might need to implement your language
  */

val emptyMaze = Maze("resources" + File.separator + "empty.txt")

val rules = List(
  Rule(
    State("0"),
    Surroundings(Anything, Anything, Open, Anything),
    West,
    State("0")
  ),
  Rule(
    State("0"),
    Surroundings(Open, Anything, Blocked, Anything),
    North,
    State("1")
  ),
  Rule(
    State("0"),
    Surroundings(Blocked, Open, Blocked, Anything),
    South,
    State("2")
  ),
  Rule(
    State("1"),
    Surroundings(Open, Anything, Anything, Anything),
    North,
    State("1")
  ),
  Rule(
    State("1"),
    Surroundings(Blocked, Anything, Anything, Open),
    South,
    State("2")
  ),
  Rule(
    State("2"),
    Surroundings(Anything, Anything, Anything, Open),
    South,
    State("2")
  ),
  Rule(
    State("2"),
    Surroundings(Anything, Open, Anything, Blocked),
    East,
    State("3")
  ),
  Rule(
    State("3"),
    Surroundings(Open, Anything, Anything, Anything),
    North,
    State("3")
  ),
  Rule(
    State("3"),
    Surroundings(Blocked, Open, Anything, Anything),
    East,
    State("2")
  )
)

// Creates and runs a simulation with the maze and rules defined above
object Empty extends GUISimulation(emptyMaze, rules)
