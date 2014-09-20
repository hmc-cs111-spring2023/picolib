/*package picolib.semantics

//import scala.swing._
//import java.awt.Color
import scalafx.scene.shape._
import scalafx.scene.paint.Color
import scalafx.Includes._

/*class SwingMaze(val width: Int, val height: Int)
    extends GridPanel(height, width) {
  
  //override def foreground = Color.BLACK
  
  

}*/


class MazeCell(minX: Double,  minY: Double) extends Rectangle {
  import MazeCell._
  x = minX
  y = minY
  width  = SIZE
  height = SIZE
  //fill   <== when(true) then Color.White otherwise Color.Gray 
}
  
/*class MazeCell extends Rectangle {
  import MazeCell._
  
  //override def foreground = UNVISITED_COLOR
}*/

object MazeCell {
  val SIZE = 25
  
  val UNVISITED_COLOR = Color.Red
  val VISITED_COLOR   = Color.Gray
  val OCCUPIED_COLOR  = Color.Black
  val WALL_COLOR      = Color.Blue
}*/