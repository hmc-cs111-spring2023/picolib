val scala3Version = "3.2.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scratch",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    scalacOptions ++= Seq(
      "-deprecation",
      "-explain"
    ),
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % Test,
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.15.4" % Test,
    libraryDependencies += "org.scalafx" %% "scalafx" % "18.0.1-R28"
  )

// So that we can run GUI apps multiple times from a single sbt session
// https://github.com/scalafx/scalafx/issues/361
fork := true