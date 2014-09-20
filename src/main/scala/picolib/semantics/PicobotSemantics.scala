package picolib.semantics

import picolib.maze._
import picolib.semantics._

import scala.collection.mutable.Set

/**
  *  A class that can set a robot loose on a maze. The robot starts at a random
  * position in the maze.
  *
  *  @param maze The Maze that the PicoBot will search
  *  @param rules A list of Rules that the PicoBot will use to search the maze.
  */
class Picobot(val maze: Maze, val rules: List[Rule]) {
  import Picobot._
  
  // the robot's current state
  var state: State = rules(0).startState
  
  // the robot's current position (initially a random location)
  private var _position: Position = randomStartPosition(maze)  
  def position = _position

  // the positions that the robot has visited (initially empty)
  private val _visited: Set[Position] = Set.empty[Position]
  def visited = _visited

  // the total number of non-wall positions
  private var numOpenPositions: Int = maze.height * maze.width - maze.wallPositions.size

  /** The number of positions the robot has yet to visit (initially the number of open
    * positions). When this value is 0, the robot's search is at an end.
    */
  def numPositionsToVisit: Int = numOpenPositions - visited.size

  // drop the robot in the maze
  moveRobotTo(position)

  /** Move the robot to a specified position. 
    * 
    * Raises an error if the robot is not allowed to occupy the given position
    *
    */
  def moveRobotTo(pos: Position) {
    // error checking: can't move to an illegal position
    require(!maze.isWall(pos) && maze.isInBounds(pos))
    
    _position = pos
    _visited += pos
  }
  
  override def toString =
    (0 until maze.width).map(col =>
        (0 until maze.height).map(row =>
          {
            val p = Position(row, col)
            if (p == position) {
              Picobot.ROBOT_CHARACTER
            } else if (visited.contains(p)) {
              Picobot.VISITED_CHARACTER
            } else if (maze.isWall(p)) {
              Maze.WALL_CHARACTER
            } else {
              Maze.NOWALL_CHARACTER
            }})
        .mkString(""))
      .mkString("\n") +  
      "\n%9s: %s\n%9s: %2s\n%9s: %s".format("Pos", position, "State", state,
                                            "Unvisited", numPositionsToVisit)
  
  /** Do the computation: set the robot loose on the maze. 
    * The robot follows its rules in order to search the 
    * maze. The search stops  when no rule applies to the current situation or when 
    * the robot has visited all the open positions in the maze. This method prints the 
    * results of each step to the screen.
    */
  def run() {
    while (canMove) {
        println(this)
        step()
        println()
      } 
      
      println(this)
  }
  
  def canMove = numPositionsToVisit != 0 && rules.find(matchRule).isDefined
  def step(): Boolean = rules.find(matchRule).map(this.update).isDefined
  
  private def matchRule(rule: Rule): Boolean = {
      
      def matchRuleDir(description: RelativeDescription, pos: Position): Boolean =
        (description == Anything) || 
        (maze.isWall(pos) && description == Blocked) ||
        (!maze.isWall(pos) && description == Open)

      (rule.startState == state) &&
        matchRuleDir(rule.surroundings.north, position.northOf) &&
        matchRuleDir(rule.surroundings.east,  position.eastOf)  &&
        matchRuleDir(rule.surroundings.west,  position.westOf)  &&
        matchRuleDir(rule.surroundings.south, position.southOf)
  }
  
  private def update(rule: Rule) {
    val newPos = rule.moveDirection match {
        case North    => position.northOf
        case East     => position.eastOf
        case West     => position.westOf
        case South    => position.southOf
        case StayHere => position
    }
    moveRobotTo(newPos)
    state = rule.endState    
  }
  
}

object Picobot {
   val ROBOT_CHARACTER = 'R'
   val VISITED_CHARACTER = 'o'
   
   /**
     * finds a random position in the maze that is not a wall
     */
   private def randomStartPosition(maze: Maze): Position = {
      import java.util.Random
      var random = new Random(System.currentTimeMillis())
      var position = Position(0,0)
      do {
        position = Position(random.nextInt(maze.width), random.nextInt(maze.height))
      } while (maze.isWall(position))
     position
   }
}
