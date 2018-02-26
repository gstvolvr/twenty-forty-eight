package xyz.olvr.twenty.forty.eight

import org.scalatest.FunSpec

class ExpectiMaxSpec extends FunSpec {
  import ExpectiMax._
  import Board._
  import GameTree.depth

  val emptyRow: Row = Vector.fill(width)(0)
  val b: Board = Vector(Vector(0, 1, 0, 0), Vector(0, 1, 0, 0), emptyRow, emptyRow)
  val t: GameTree = GameTree(b)

  def run(d: Int) = {
    println("_" * 100)
    println(s"\nrun with depth ${d}")
    val l = time(start(t, d))
    val x = depth(l)
    println
    println("leaf node:")
    println(l.pretty)
    println
    println(s"depth: ${x}")
  }

  describe("test simulation") {
    it("should run with depth 2") { run(2) }
    it("should run with depth 3") { run(3) }
  }
}
