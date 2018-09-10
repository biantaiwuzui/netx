
lazy val root = project.in( file(".") ).dependsOn( RootProject(file("../../nx_build")) )