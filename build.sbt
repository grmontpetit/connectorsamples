name := "ConnectorsSamples"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "com.typesafe" % "config" % "1.3.0"

fork := true

libraryDependencies ++= {
  val akkaV = "2.4.1"
  val sprayV = "1.3.3"
  val sprayJsonV = "1.3.2"
  val orientCoreV = "2.1.9"
  Seq(
    "io.spray"               %% "spray-can"     % sprayV,
    "io.spray"               %% "spray-routing" % sprayV,
    "io.spray"               %% "spray-client"  % sprayV,
    "io.spray"               %% "spray-json"    % sprayJsonV,
    "io.spray"               %% "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"      %% "akka-actor"    % akkaV,
    "com.typesafe.akka"      %% "akka-testkit"  % akkaV   % "test",
    "org.scalatest"          %% "scalatest"     % "2.2.1" % "test",
    "org.scala-lang.modules" %% "scala-async"   % "0.9.2",
    "com.google.guava"       % "guava"          % "19.0",
    "org.apache.xmlrpc"      % "xmlrpc-client"  % "3.1.3",
    "org.apache.xmlrpc"       % "xmlrpc-common"  % "3.1.3",
    "org.apache.ws.commons.util" % "ws-commons-util" % "1.0.2",
    "com.google.code.gson"       % "gson"            % "2.7",
    "com.j2bugzilla"             % "j2bugzilla"      % "2.2.1"
  )
}