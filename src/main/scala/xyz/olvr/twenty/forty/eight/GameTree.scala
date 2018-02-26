package xyz.olvr.twenty.forty.eight

import Board._

/** directed graph where the nodes represent a game state and the edges are moves */
sealed trait GameTree {
  val board: Board
  val isEmpty: Boolean
  val parent: GameTree
  val win = Board.win(board)

  def pretty: String = board.pretty
  def label: String = board.flatten.mkString
  def moves: Seq[String] = legalMoves(board)
  def max: Int = board.flatten.max
}

/** represent end of game */
case class Leaf(board: Board, parent: GameTree) extends GameTree {
  val isEmpty = true
}

/** current state of play */
case class Branch(board: Board, parent: GameTree) extends GameTree {
  val isEmpty = this.moves.isEmpty

  private[twenty] def add(f: Board => Board): GameTree = {
    val next = f(board)
    if (legalMoves(next).isEmpty) Leaf(next, this)
    else if (next != board) Branch(next, this)
    else this
  }
  private[twenty] def addAsIs(b: Board): GameTree = {
    if (legalMoves(b).isEmpty) Leaf(b, this)
    else Branch(b, this)
  }
  def move(k: String): GameTree = k match {
    case "u" => this.up
    case "d" => this.down
    case "l" => this.left
    case "r" => this.right
  }

  def up = add(Board.up)
  def down = add(Board.down)
  def left = add(Board.left)
  def right = add(Board.right)

  def insert = add(randomTile)
  def random = add(randomMove(board))

  def children(turn: Boolean) = if (turn) this.moves.map(move) else for (b <- randomTiles(board)) yield addAsIs(b)
}

object GameTree {
  def apply(b: Board): GameTree = (win(b), lose(b)) match {
    case (false, false) => Branch(b, null)
    case _ => Leaf(b, null)
  }

  def findMove(t: GameTree, k: String): GameTree = (t, k) match {
    case (b: Branch, "u") => b.up
    case (b: Branch, "d") => b.down
    case (b: Branch, "l") => b.left
    case (b: Branch, "r") => b.right
    case (b: Branch, _) => b
    case (l: Leaf, _) => l
  }

  def chanceOf(t: GameTree): Double = {
    val fp = t.parent.board.flatten
    val spots = 1.0 / fp.filter(_ == 0).size

    t.board.flatten.diff(fp) match {
      case Vector(1) => Board.ChanceOfOne * spots
      case _ => Board.ChanceOfTwo * spots
    }
  }

  def depth(t: GameTree, count: Int = 0): Int = t.parent match {
    case null => count
    case pt: GameTree => depth(pt, count + 1)
  }

  def printTree(t: GameTree) {
    println(t.pretty)
    println
    t.parent match {
      case pt: GameTree => printTree(pt)
    }
  }
}
