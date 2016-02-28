name := "picolib"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= 
  Seq( "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
       "org.scalafx" % "scalafx_2.11" % "8.0.5-R5" )

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
