package xyz.olvr.twenty.forty.eight

import scala.util.Random

/** describe the universe and all it's wonders */
object Board {

  // we're playing on a 4x4 grid
  val width = 4
  val empty = Vector.fill(width)(Vector.fill(width)(0))

  // random tile probabilities
  val ChanceOfOne = 0.9
  val ChanceOfTwo = 0.1

  // combination rules when playing the log_2 version
  def combine(c: Col) = c.tail.foldLeft(c.take(1)) {
    case (hs :+ l, v) => if (l != v && l != 0 && v != 0) hs :+ l :+ v
                         else if (v == 0 || l == 0) hs :+ (l + v)
                         else hs :+ (l + 1)
  }.padTo(width, 0)

  // possible moves
  def up: Move = _.transpose.map(combine).transpose
  def down: Move = _.transpose.map(r => combine(r.reverse).reverse).transpose
  def left: Move = _.map(combine)
  def right: Move = _.map(r => combine(r.reverse).reverse)

  val moves = Map("u" -> up, "d" -> down, "l" -> left, "r" -> right)
  def legalMoves(b: Board) = moves.keys.filter(k => moves(k)(b) != b).toSeq
  def randomMove(b: Board): Move = chooseRandom(legalMoves(b)) match {
    case "u" => up
    case "d" => down
    case "l" => left
    case "r" => right
    case _ => identity
  }

  def update(ri: Int, ci: Int, v: Int)(b: Board): Board = b.updated(ri, b(ri).updated(ci, v))

  /** add random tile to board */
  def randomTile(b: Board): Board = {
    val zeros = b.map(_.zipWithIndex.filter { case (v, ci) => v == 0 }.map(_._2))
    val zeroCols = zeros.flatten
    val zeroRows = zeros.zipWithIndex.filter { case (v, ri) => !v.isEmpty }.map(_._2)

    // if there aren't any zeros,
    if (zeros.filterNot(_.isEmpty).isEmpty) b
    else {
      val ri = zeroRows(Random.nextInt(zeroRows.size)) // random row index
      val ci = zeroCols(Random.nextInt(zeros(ri).size)) // random column index

      if ((Random.nextDouble) <= ChanceOfTwo) update(ri, ci, 2)(b) else update(ri, ci, 1)(b)
    }
  }

  /** generate all possible random tiles */
  def randomTiles(b: Board): Boards = {
    val zs = b.map(_.zipWithIndex.filter { case (v, ci) => v == 0 }.map(_._2)) // Vector(Vector(indices where value is 0))
    zs.zipWithIndex.flatMap { case (cs: Vector[Int], ri: Int) => cs.flatMap(ci => Seq(update(ri, ci, 1)(b), update(ri, ci, 2)(b))) }
  }

  def win(b: Board): Boolean = !b.flatten.filter(_ >= 11).isEmpty
  def lose(ob: Board, nb: Board): Boolean = ob == nb && ob.filter(_.contains(0)).isEmpty // FIX - doesn't necessarily imply a loss
  def lose(b: Board): Boolean = up(b) == down(b) && left(b) == right(b)
  def printBoard(b: Board) { b foreach(r => println(r.mkString("|"))); println }
  def toString(b: Board): String = b.flatten.mkString(".")
}
