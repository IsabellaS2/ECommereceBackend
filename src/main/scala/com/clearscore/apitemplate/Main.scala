package com.clearscore.apitemplate

import cats.effect.*
import com.clearscore.apitemplate.db.ProductRepositoryImpl
import com.clearscore.apitemplate.http.ProductRoutes
import com.clearscore.apitemplate.service.ProductServiceImpl
import io.github.liquibase4s.cats.CatsMigrationHandler.*
import io.github.liquibase4s.{Liquibase, LiquibaseConfig}
import org.http4s.*
import org.http4s.implicits.*
import org.http4s.jetty.server.JettyBuilder
import org.http4s.server.Router
import cats.MonadThrow
import cats.effect.kernel.{MonadCancelThrow, Resource}
import cats.effect.{IO, IOApp}
import doobie.ExecutionContexts
import doobie.util.transactor.Transactor
import doobie.implicits.*
import org.http4s.server.middleware.{CORS, CORSConfig}

object Main extends IOApp {

  IO.println("Server Starting")

  // postgres://test:Password1!@localhost:5432/moneyzen
  val config: LiquibaseConfig = LiquibaseConfig(
    url = "jdbc:postgresql://localhost:5432/create_drop",
    user = "postgres",
    password = "asdf123",
    driver = "org.postgresql.Driver",
    changelog = "db/liquibase/main.xml",
  )

  val transactor: Transactor[IO] = Transactor.fromDriverManager[IO](
    driver = config.driver,
    url = config.url,
    user = config.user,
    password = config.password,
    logHandler = None
  )

  private val productRepository = new ProductRepositoryImpl(transactor)
  private val productService = new ProductServiceImpl(productRepository)
  private val productRoutes = new ProductRoutes(productService)


  override def run(args: List[String]): IO[ExitCode] = {

    val apis = Router(
      "/products" -> productRoutes.routes
    )

    val corsPolicy = CORS.policy.withAllowOriginAll

    val corsApis = corsPolicy(apis)

    Liquibase[IO](config).migrate() *>
      buildServer(corsApis) as (ExitCode.Success)
  }

  private def buildServer(app: HttpRoutes[IO]) = {
    JettyBuilder[IO]
      .bindHttp(8081, "localhost")
      .mountHttpApp(app.orNotFound, "/")
      .serve
      .compile
      .drain
  }
}

