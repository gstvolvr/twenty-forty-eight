package xyz.olvr.twenty.forty.eight

import org.scalatest.FunSpec

class BoardSpec extends FunSpec {
  import Board._

  val emptyRow: Row = Vector.fill(4)(0)
  val board: Board = Vector(Vector(0, 2, 2, 0), emptyRow, emptyRow, Vector(0, 2, 0, 0))

  describe("test the universe") {
    it("should move up") {
      assert(up(board) === Vector(Vector(0, 3, 2, 0)) ++ Vector.fill(3)(emptyRow))

    }
    it("should move down") {
      assert(down(board) === Vector.fill(3)(emptyRow) :+ Vector(0, 3, 2, 0))
    }
    it("should move right") {
      assert(right(board) === Vector(Vector(0, 0, 0, 3)) ++ Vector.fill(2)(emptyRow) :+ Vector(0, 0, 0, 2))
    }
    it("should move left") {
      assert(left(board) === Vector(Vector(3, 0, 0, 0)) ++ Vector.fill(2)(emptyRow) :+ Vector(2, 0, 0, 0))
    }
    it("should add a random tile") {
      assert(randomTile(board) !== board)
    }
    it("should print the board") {
      printBoard(board)
    }
  }
}
