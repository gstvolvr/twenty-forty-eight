lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      scalaVersion := "2.12.1"
    )),
    libraryDependencies ++= Seq(
      "org.seleniumhq.selenium" % "selenium-java" % "3.9.1",
      "org.scalatest" %% "scalatest" % "3.0.3" % "test"
    ),
    name := "twenty-forty-eight"
  )
