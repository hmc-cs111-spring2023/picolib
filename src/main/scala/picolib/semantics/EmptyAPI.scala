package picolib.semantics

import picolib.maze._
import picolib.semantics._
import java.io.File
import scalafx.application.JFXApp
import scalafx.scene.Scene

/**
 *  This is an intentionally bad internal language, but it uses all the parts of
 *  the picolib library that you might need to implement your language
 */

object EmptyRoom extends JFXApp {
  val emptyMaze = Maze("resources" + File.separator + "empty.txt")

  val rules = List(
    Rule(State("0"),
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
      State("2")))

  object EmptyBot extends Picobot(emptyMaze, rules)
    with TextDisplay with GUIDisplay

  stage = EmptyBot.mainStage
}
