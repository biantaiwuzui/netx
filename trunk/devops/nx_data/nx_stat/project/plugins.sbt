
lazy val root = project.in( file(".") ).dependsOn( RootProject(file("../../nx_build")) )
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.7")