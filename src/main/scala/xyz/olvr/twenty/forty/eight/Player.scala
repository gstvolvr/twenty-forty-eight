package xyz.olvr.twenty.forty.eight

import org.openqa.selenium.By;
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.interactions.Actions

import scala.math.log10

object Player {
  import Board._
  import GameTree._
  import Strategy._
  import ExpectiMax._

  def sendMove(m: String, k: Actions) = m match {
    case "u" => k.sendKeys("w").perform()
    case "d" => k.sendKeys("s").perform()
    case "l" => k.sendKeys("a").perform()
    case "r" => k.sendKeys("d").perform()
  }

  def pause = Thread.sleep(80)

  def extract(d: SafariDriver): Board = {
    val source = d.getPageSource()

    val R = """\"tile\stile\-(\d{1,4})\stile\-position\-(\d)\-(\d)(\stile\-merged)?""".r
    val emptyBoard = Board.empty

    // on the website, top left corner is 1,1 and bottom right is 4,4
    val rawTiles = R.findAllIn(source).matchData.map(m => (m.group(1).toInt, m.group(2).toInt, m.group(3).toInt))
    val base10Board = rawTiles.foldLeft(emptyBoard) { case (b: Board, (value: Int, col: Int, row: Int)) => b.updated(row - 1, b(row - 1).updated(col - 1, value)) }

    base10Board.map(_.map { case 0 => 0 case v => (log10(v) / log10(2)).toInt })
  }

  def start(tree: GameTree, depth: Int, driver: SafariDriver, keyboard: Actions) = {

    // xpath to a "you win, would you like to keep playing?" box
    val keepPlaying = "keep-playing-button"

    def play(t: GameTree, cheats: CheatCodes = Map[String, String](), turn: Boolean = true, continue: Boolean = true): GameTree = t match {
      case b: Branch if continue && b.win && driver.findElements(By.className(keepPlaying)).size != 0 => {
        println("click me!")
        Thread.sleep(5000)
        driver.findElements(By.className(keepPlaying)).get(0).click()
        pause
        play(b, cheats, false, false) // set continue to false, so it doesn't try to click again
      }
      case b: Branch if turn && cheats.contains(b.label) => {
        sendMove(cheats(b.label), keyboard)
        pause
        play(b.move(cheats(b.label)), cheats, false, continue)
      }
      case b: Branch if turn => {
        val newCheats = prune(b, depth)
        val direction = newCheats(b.label)
        sendMove(direction, keyboard)
        pause
        play(b.move(direction), newCheats, false, continue)
      }
      case b: Branch => {
        val board = extract(driver) // get computer move
        play(b.addAsIs(board), cheats, true, continue)
      }
      case l: Leaf => driver.quit(); l
    }

    play(tree)
  }
}

object Play {
  import Player._

  def game(depth: Int) {
    val url = "http://www.2048game.com"
    val driver = new SafariDriver()
    val keyboard = new Actions(driver)

    driver.get(url)
    driver.manage.window.maximize

    val board = extract(driver)
    val t = GameTree(board)

    start(t, depth, driver, keyboard)
  }
}
