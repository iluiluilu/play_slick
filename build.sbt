name := "play_slick"
 
version := "1.0" 
      
lazy val `play_slick` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice, cacheApi )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "3.0.0",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "be.objectify" %% "deadbolt-scala" % "2.6.0",
  // https://mvnrepository.com/artifact/org.mindrot/jbcrypt
  "org.mindrot" % "jbcrypt" % "0.4"
)
