name := "Netx/Data"

lazy val sync = RootProject(file("nx_source_sync"))

lazy val stat = RootProject(file("nx_stat"))

lazy val studio = project.in(file(".")).
  aggregate(sync, stat).
  settings(
    publish := {},
    publishLocal := {}
  )
