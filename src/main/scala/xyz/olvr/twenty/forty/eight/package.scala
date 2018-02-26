package xyz.olvr.twenty.forty.eight

import scala.util.Random

object `package` {

  // let's keep it clean
  type Row = Vector[Int]
  type Col = Vector[Int]
  type Board = Vector[Row]
  type Boards = Seq[Board]
  type Move = Board => Board
  type Moves = Map[String, Move]
  type Path = Seq[Board]
  type History = Map[String, (Int, Int)] // Board -> (Games, Wins)
  type CheatCodes = Map[String, String] // Board -> Best move

  def chooseRandom(xs: Iterable[Any]): Any = if (xs.isEmpty) null else xs.toSeq(Random.nextInt(xs.size))
  def won = (g: Int, w: Int) => (g + 1, w + 1)
  def lost = (g: Int, w: Int) => (g + 1, w)

  implicit class RichBoard(val b: Board) extends AnyVal {
    def pretty: String = b.map(_.mkString("|")).mkString("\n")
  }

  def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block
    val t1 = System.nanoTime()
    val tt = (t1 - t0) / math.pow(10, 9)
    println(f"\nElapsed time: $tt%1.4fs")
    result
  }
}
