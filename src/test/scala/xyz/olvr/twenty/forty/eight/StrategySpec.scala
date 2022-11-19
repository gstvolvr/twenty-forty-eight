package xyz.olvr.twenty.forty.eight

import org.scalatest.FunSpec

class StrategySpec extends FunSpec {
  import Strategy._

  val emptyRow: Row = Vector.fill(4)(0)
  val board: Board = Vector(Vector(0, 2, 2, 0), emptyRow, emptyRow, Vector(0, 2, 0, 0))
  val t: GameTree = GameTree(board)

  describe("Snake positioning") {
    it("board should add to 16") {
      /**
      * | 1 | 1 | 1 | 1 |
      * | 1 | 1 | 1 | 1 |
      * | 1 | 1 | 1 | 1 |
      * | 1 | 1 | 1 | 1 |
      */
      val board: Board = Vector(Vector(1, 1, 1, 1), Vector(1, 1, 1, 1), Vector(1, 1, 1, 1), Vector(1, 1, 1, 1))
      val t: GameTree = GameTree(board)
      assert(snake(t) == 16)
    }
    it("board best case scenario should beat others") {
      /**
      * | 0 | 0 | 0  | 0  |
      * | 1 | 1 | 2  | 3  |
      * | 7 | 6 | 5  | 4  |
      * | 8 | 9 | 10 | 11 |
      */
      val bestCaseScenario: Board = Vector(Vector(0, 0, 0, 0), Vector(1, 1, 2, 3), Vector(7, 6, 5, 4), Vector(8, 9, 10, 11))
      val bestCaseTree: GameTree = GameTree(bestCaseScenario)

      val otherCaseScenario: Board = Vector(Vector(0, 0, 0, 0), Vector(0, 1, 2, 3), Vector(11, 6, 5, 11), Vector(10, 10, 10, 10))
      val otherCaseTree: GameTree = GameTree(otherCaseScenario)
      assert(snake(bestCaseTree) > snake(otherCaseTree))
    }
  }
}
