
name         := "Topology/Layout Analyzer"
version      := "0.0.0"
scalaVersion := "2.12.1"

// regizter project in /investigation
lazy val investigation = project

scalacOptions ++= Seq(
	"-deprecation",
	"-feature",
	"-Xlint"
)

libraryDependencies ++= Seq(
	"org.scalafx" %% "scalafx" % "8.0.102-R11"
//	,"com.jsuereth" %% "scala-arm" % "2.0",
//	"commons-io" % "commons-io" % "2.5"
)

unmanagedJars in Compile += file(sys.props("java.home") + "/lib/ext/jfxrt.jar")

excludeFilter in unmanagedSources := HiddenFileFilter || "NBTViewer.scala"
