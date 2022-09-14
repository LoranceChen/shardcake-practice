ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "shardcake-parctice",
    idePackagePrefix := Some("lorance.practice")
  )

libraryDependencies += "com.devsisters" %% "shardcake-manager" % "2.0.0"
libraryDependencies += "com.devsisters" %% "shardcake-entities" % "2.0.0"
libraryDependencies += "com.devsisters" %% "shardcake-protocol-grpc" % "2.0.0"
//libraryDependencies += "com.thesamet.scalapb.zio-grpc" %% "zio-grpc-core" % "0.5.2"
libraryDependencies += "com.thesamet.scalapb.zio-grpc" % "zio-grpc-core_2.13" % "0.6.0-test4"
