ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val doobieVersion = "1.0.0-RC4"
val Http4sVersion = "0.23.23"
val CirceVersion = "0.14.5"
val LogbackVersion = "1.4.14"
val Sttp4Version = "4.0.0-M8"

resolvers += "jitpack".at("https://jitpack.io")

lazy val root = (project in file("."))
  .settings(
    name := "http4s-scala3",
    libraryDependencies += compilerPlugin(
      "org.polyvariant" % "better-tostring" % "0.3.17" cross CrossVersion.full
    ),
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-jetty-server" % "0.23.13",
      "com.softwaremill.sttp.client4" %% "core" % Sttp4Version,
      "com.softwaremill.sttp.client4" %% "circe" % Sttp4Version,
      "com.softwaremill.sttp.client4" %% "async-http-client-backend" % Sttp4Version,
      "com.softwaremill.sttp.client4" %% "async-http-client-backend-cats" % Sttp4Version,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "io.circe" %% "circe-literal" % CirceVersion,
      "io.monix" %% "newtypes-core" % "0.2.3",
      "io.monix" %% "newtypes-circe-v0-14" % "0.2.3",
      "org.scalactic" %% "scalactic" % "3.2.16",
      "org.scalatest" %% "scalatest" % "3.2.16" % Test,
      "org.typelevel" %% "discipline-scalatest" % "2.2.0",
      "org.typelevel" %% "cats-effect-testing-scalatest" % "1.4.0" % Test,
      "com.github.pureconfig" %% "pureconfig-cats-effect" % "0.17.4",
      "com.github.pureconfig" %% "pureconfig-core" % "0.17.4",
      "ch.qos.logback" % "logback-classic" % LogbackVersion % Runtime,
      "org.tpolecat" %% "doobie-core"     % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-specs2"   % doobieVersion,
      "io.github.liquibase4s" %% "liquibase4s-cats-effect" % "1.0.0"
    ),
    scalacOptions ++= Seq(
      "-no-indent",
    ),
  )
