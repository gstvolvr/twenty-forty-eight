package xyz.olvr.twenty.forty.eight

import scala.util.Random
import scala.math.{ min, max }
import scala.annotation.tailrec

import scala.math

/** minimize the possible loss of worst case scenario */
object MiniMax {
  import Board._
  import GameTree._
  import Strategy._

  /**
   * alpha beta pruning
   * tree: current game tree
   * depth: max depth we want to traverse
   */
  def prune(tree: GameTree, depth: Int): CheatCodes = {

    /**
     * alpha: minimum score the maximizing player is assured
     * beta: maximum score the minimizing player is assured
     * score: heuristic value
     * turn: whether or not it's our turn
     */
    def loop(
      t: GameTree,
      cheats: CheatCodes = Map[String, String](),
      level: Int = 0,
      alpha: Double = Double.NegativeInfinity,
      beta: Double = Double.PositiveInfinity,
      turn: Boolean = true): (Double, CheatCodes) = {

      t match {
        case b: Branch => {
          if (turn) b.moves.foldLeft((Double.NegativeInfinity, cheats)) { case ((score: Double, parentCheats: CheatCodes), direction: String) =>
            if (beta <= alpha) return (score, cheats)
            else if (depth < level) return (eval(b), cheats)
            else {
              val child = b.move(direction)
              loop(child, parentCheats, level + 1, max(alpha, score), beta, false) match {
                case (childScore, childCheats) if score < childScore => (max(score, childScore), parentCheats ++ childCheats + (b.label -> direction))
                case (childScore, childCheats) => (max(score, childScore), parentCheats ++ childCheats)
              }
            }
          }
          else b.children(false).foldLeft((Double.PositiveInfinity, cheats)) { case ((score: Double, parentCheats: CheatCodes), child: GameTree) =>
            if (beta <= alpha) return (score, cheats)
            else if (depth < level) return (eval(b), cheats)
            else {
              val (childScore, childCheats) = loop(child, parentCheats, level + 1, alpha, min(beta, score), true)
              (min(score, childScore), parentCheats ++ childCheats)
            }
          }
        }
        case l: Leaf => (eval(l), cheats)
      }
    }

    val (score, cheats) = loop(tree)
    cheats
  }

  def start(tree: GameTree, depth: Int): GameTree = {

    def play(t: GameTree, cheats: CheatCodes = Map[String, String](), turn: Boolean = true): GameTree = t match {
      case b: Branch if turn && cheats.contains(b.label) => play(b.move(cheats(b.label)), cheats, false)
      case b: Branch if turn => val c = prune(b, depth); play(b.move(c(b.label)), c, false)
      case b: Branch => play(b.insert, cheats, true)
      case l: Leaf => l
    }

    play(tree)
  }
}
