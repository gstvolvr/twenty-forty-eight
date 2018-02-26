package xyz.olvr.twenty.forty.eight

/** human input */
object Strategy {

   /**
   * incentivize the game to move into a snake like formation with the
   * head in the bottom right, and tail in the top left corner
   */
  def snake(t: GameTree): Double = {
    val b = t.board
    val pairs =
      b(0).zip(4 to (1, -1)) ++
      b(1).zip(5 to 8) ++
      b(2).zip(12 to (9, -1)) ++
      b(3).zip(13 to 16)
    pairs.foldLeft(0.0)((agg, tup) => agg + math.pow(tup._1, tup._2))
  }

  /** incentivize open spaces on the board */
  def open(t: GameTree): Double = t.board.flatten.filter(x => x == 0.0).size

  /** weight sum of features */
  def eval(t: GameTree): Double = {
    val weights = Seq(0.75, 0.25) // can be "learned"
    weights.zip(Seq(snake(t), open(t))).foldLeft(0.0)((agg, tup) => agg + tup._1 * tup._2)
  }
}
