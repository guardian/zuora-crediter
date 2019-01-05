name := "zuora-creditor"
description:= "This project contains a set of services and Lambda functions which find negative invoices and converts " +
  "them into a credit balance on the user's account, so that the amount is discounted off their next positive bill"
version       := "0.0.2"
scalaVersion := "2.11.12"
organization := "com.gu.zuora"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-target:jvm-1.8",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  //"-Xlint",
  //"-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

lazy val root = (project in file(".")).enablePlugins(ScalaxbPlugin, RiffRaffArtifact)
val dispatchV = "0.11.4" // TODO - Needs upgrading to 0.13.x and generation code changing to accommodate.

scalaxbDispatchVersion in (Compile, scalaxb) := dispatchV
scalaxbPackageName in (Compile, scalaxb) := "com.gu.zuora.soap"
scalaxbAsync in (Compile, scalaxb) := false

assemblyJarName := "zuora-creditor.jar"
riffRaffPackageType := assembly.value
riffRaffUploadArtifactBucket := Option("riffraff-artifact")
riffRaffUploadManifestBucket := Option("riffraff-builds")
riffRaffManifestProjectName := "MemSub::Membership Admin::Zuora Creditor"

addCommandAlias("dist", ";riffRaffArtifact")

resolvers += "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.1.1" % "compile",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1" % "compile",
  "com.amazonaws" % "aws-lambda-java-log4j" % "1.0.0",
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",
  "com.amazonaws" % "aws-java-sdk-kms" % "1.11.477",
  "net.databinder.dispatch" %% "dispatch-core" % dispatchV,
  "com.typesafe.play" %% "play-json" % "2.6.13",
  "org.scalaj" %% "scalaj-http" % "2.4.1",
  "com.github.melrief" %% "purecsv" % "0.1.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.8",  // updated for snyk: https://app.snyk.io/org/the-guardian-cuu/project/c8aa728c-1f6a-4ede-900c-dfde0c0af9bf/
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.9.8", // added for snyk: https://app.snyk.io/org/the-guardian-cuu/project/c8aa728c-1f6a-4ede-900c-dfde0c0af9bf/
  "com.fasterxml.jackson.core" % "jackson-core" % "2.9.8",  // updated for snyk: https://app.snyk.io/org/the-guardian-cuu/project/c8aa728c-1f6a-4ede-900c-dfde0c0af9bf/
  "io.netty" % "netty" % "3.10.6.Final",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

initialize := {
  val _ = initialize.value
  assert(sys.props("java.specification.version") == "1.8", "Java 8 is required for this project.")
}
