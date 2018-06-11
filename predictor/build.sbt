import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

name := "prediction-api"

version := "1.0"

lazy val `predictor` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += Resolver.sonatypeRepo("snapshots")

val kafkaStreamsVersion = "0.11.0.0"

libraryDependencies +=  "org.apache.kafka" % "kafka-streams" % kafkaStreamsVersion

libraryDependencies +=  guice

libraryDependencies ++= Seq(
  ehcache, jcache)

// https://mvnrepository.com/artifact/org.apache.kafka/kafka
libraryDependencies += "org.apache.kafka" %% "kafka" % "1.1.0"

val kafka_streams_scala_version = "0.2.1"

libraryDependencies ++= Seq("com.lightbend" %%
  "kafka-streams-scala" % kafka_streams_scala_version)

//for packaging via sbt docker:publishLocal
//run with docker --rm --net="host" ml_user:1.0
//edit run command - inside target docker file
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
//for adding ash scripts into alpone image
enablePlugins(AshScriptPlugin)
dockerExposedPorts in Docker := Seq(9001)
dockerBaseImage := "openjdk:jre-alpine"


//install docker client binary to manage containers on host machine  - https://forums.docker.com/t/using-docker-in-a-dockerized-jenkins-container/322/11
dockerCommands ++= Seq(
  Cmd("USER", "root"),
  ExecCmd("RUN", "apk", "add", "--no-cache", "curl"),
  ExecCmd(
    """RUN cd /tmp/ \
      | && /usr/bin/curl -sSL -O https://download.docker.com/linux/static/stable/x86_64/docker-17.06.2-ce.tgz \
      | && tar -zxf docker-17.06.2-ce.tgz \
      | && mkdir -p /usr/local/bin \
      | && mv ./docker/docker /usr/local/bin \
      | && chmod +x /usr/local/bin/docker \
      | && rm -rf /tmp/*""".stripMargin))
