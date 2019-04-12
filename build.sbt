name := "dslink-scala-v2-example"

version := "1.0.0"

scalaVersion := "2.12.8"

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.iot-dsa-v2.sdk-dslink-java-v2" % "dslink-v2-websocket" % "0.56.0"

mainClass in Compile := Some("org.iot.dsa.dslink.DSLink")

lazy val root = (project in file("."))
    .enablePlugins(JavaAppPackaging)

mappings in Universal <+= (packageBin in Compile) map { jar =>
  val dslinkjson = new java.io.File("dslink.json")
  dslinkjson -> "dslink.json"
}