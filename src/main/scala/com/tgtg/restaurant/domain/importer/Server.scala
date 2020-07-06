package com.tgtg.restaurant.domain.importer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}
import com.softwaremill.macwire._
import com.tgtg.restaurant.domain.importer.routes.RestaurantImporterRoute
import com.tgtg.restaurant.domain.importer.service.RestaurantImporterService
import com.typesafe.config.ConfigFactory
object Server extends App {

    implicit val system: ActorSystem = ActorSystem("restaurants")
    implicit val materializer = Materializer(system)
    implicit val executionContext: ExecutionContext = system.dispatcher

    lazy val serv= wire[RestaurantImporterService]
    lazy val route =wire[RestaurantImporterRoute]
    val pathcsv = ConfigFactory.load.getString("csv-path")
    serv.uploadCsv(pathcsv)
    val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(route.routes, "localhost", 8080)

    serverBinding.onComplete {
        case Success(bound) =>
            println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
        case Failure(e) =>
            Console.err.println(s"Server could not start!")
            e.printStackTrace()
            system.terminate()
    }

    Await.result(system.whenTerminated, Duration.Inf)

}
