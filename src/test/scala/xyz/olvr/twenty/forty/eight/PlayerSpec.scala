package xyz.olvr.twenty.forty.eight

import org.scalatest.FunSpec

class PlayerSpec extends FunSpec {

  describe("player") {
    it("should play") {
      val depth = 2
      Play.game(depth)
    }
  }
}
