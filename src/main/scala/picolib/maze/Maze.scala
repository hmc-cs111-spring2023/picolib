package picolib.maze

/** Class that represents a position in the maze.
  *
  * @constructor
  *   Create a new Position.
  *
  * @param x
  * @param y
  */
case class Position(x: Int, y: Int) {

  def northOf = Position(x, y - 1)
  def southOf = Position(x, y + 1)
  def eastOf = Position(x + 1, y)
  def westOf = Position(x - 1, y)

  override def toString = "(%d, %d)".format(x, y)
}

/** Class that represents a maze.
  *
  * Conceptually, a maze is represented as a Cartesian coordinate system where
  * '''the origin (0,0) is in the top left corner'''. Column numbers increase
  * from left to right. Row numbers increase from top to bottom.
  *
  * @constructor
  *   Create a new Maze.
  *
  * @param width
  *   The number of columns in the maze
  * @param height
  *   The number of rows in the maze
  * @param wallPositions
  *   The positions in the maze that correspond to walls (all other positions
  *   are assumed to be open)
  */
class Maze(val width: Int, val height: Int, val wallPositions: Set[Position]) {

  /** all positions in the maze */
  lazy val positions =
    for (column <- 0 until width; row <- 0 until height)
      yield Position(column, row)

  /** Tests a position to see if there's a wall there
    *
    * @param pos
    *   A position to test
    * @return
    *   `true` if the position is part of a wall
    */
  def isWall(pos: Position) = wallPositions contains pos

  /** Tests a position to see if it's part of the maze
    *
    * @param pos
    *   A position to test
    * @return
    *   `true` if the position corresponds to a place in the maze
    */
  def isInBounds(pos: Position) =
    (0 <= pos.x) && (pos.x < this.width) &&
      (0 <= pos.y) && (pos.y < this.height)

  override def toString = {
    (0 until width)
      .map(column =>
        (0 until height)
          .map(row =>
            if (isWall(Position(row, column)))
              Maze.WALL_CHARACTER
            else Maze.NOWALL_CHARACTER
          )
          .mkString
      )
      .mkString("\n")
  }
}

/** A Maze factory
  */
object Maze {
  val WALL_CHARACTER = '+'
  val NOWALL_CHARACTER = ' '

  /** Parses a maze file (represented as a list of lines) and results in a Maze.
    * The method will issue an error if the lines are of unequal width.
    *
    * @param data
    *   a list of strings that describe the maze
    * @return
    *   a Maze instance
    */
  def apply(data: List[String]): Maze = {
    val height = data.length
    val width = data(0).length

    // issue error if there are lines of unequal width
    require(data.exists(l => l.length != width) == false)

    // generate a collection of Positions -- one Position
    // for each wall character in the file
    val wall_positions = for {
      rowData <- data.zipWithIndex
      columnData <- rowData._1.zipWithIndex
      if columnData._1 == WALL_CHARACTER
    } yield Position(columnData._2, rowData._2)

    new Maze(width, height, wall_positions.toSet)
  }

  /** Parses a maze from a file (represented as a list of lines) and results in
    * a Maze. The method will issue an error if the maze file doesn't exist or
    * if it contains lines are of unequal width.
    *
    * @param filename
    *   the name of a file with a maze in it
    * @return
    *   a Maze instance
    */
  def apply(filename: String): Maze =
    Maze(io.Source.fromFile(filename).getLines().toList)
}
