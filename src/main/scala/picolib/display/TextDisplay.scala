package picolib.display

import picolib.semantics._

/** A trait to print the simulation to the screen, as it runs */
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
