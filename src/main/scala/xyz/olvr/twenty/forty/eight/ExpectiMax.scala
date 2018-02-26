package xyz.olvr.twenty.forty.eight

import scala.util.Random
import scala.math.{ min, max }
import scala.annotation.tailrec

/** minimize the possible loss of worst case scenario */
object ExpectiMax {
  import Board._
  import GameTree._
  import Strategy._

  /** Focus on high probability spaces
   * tree: current game tree
   * depth: max depth we want to traverse
   * threshold: pruning threshold
   */
  def prune(tree: GameTree, depth: Int, threshold: Double = 0.9): CheatCodes = {

    /**
     * score: heuristic value
     * turn: whether or not it's our turn
     */
    def loop(
      t: GameTree,
      cheats: CheatCodes = Map[String, String](),
      level: Int = 0,
      turn: Boolean = true): (Double, CheatCodes) = {

      t match {
        case b: Branch => {
          if (turn) b.moves.foldLeft((Double.NegativeInfinity, cheats)) { case ((score: Double, parentCheats: CheatCodes), direction: String) =>
            if (depth < level) return (eval(b), cheats)
            else {
              val child = b.move(direction)
              loop(child, parentCheats, level + 1, false) match {
                case (childScore, childCheats) if score < childScore => (max(score, childScore), parentCheats ++ childCheats + (b.label -> direction))
                case (childScore, childCheats) => (max(score, childScore), parentCheats ++ childCheats)
              }
            }
          }
          else b.children(false).foldLeft((0.0, cheats)) { case ((score: Double, parentCheats: CheatCodes), child: GameTree) =>
            if (depth < level) return (eval(b), cheats)
            else {
              val chance = chanceOf(child)
              val empty = 1.0 / b.board.flatten.filter(_ == 0).size
              if (chance >= threshold * empty) {
                val (childScore, childCheats) = loop(child, parentCheats, level + 1, true)
                (score + chance * childScore, parentCheats ++ childCheats)
              } else (score, parentCheats)
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
