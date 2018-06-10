name := "predictor"

version := "1.0"

lazy val `predictor` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += Resolver.sonatypeRepo("snapshots")

val kafkaStreamsVersion = "0.10.0.0"

libraryDependencies +=  "org.apache.kafka" % "kafka-streams" % kafkaStreamsVersion

libraryDependencies +=  guice

libraryDependencies ++= Seq(
  ehcache, jcache
)

// https://mvnrepository.com/artifact/org.apache.kafka/kafka
libraryDependencies += "org.apache.kafka" %% "kafka" % "1.1.0"