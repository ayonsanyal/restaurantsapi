package com.tgtg.restaurant.domain.importer.service

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.alpakka.csv.scaladsl.CsvParsing
import akka.stream.scaladsl.{FileIO,Sink,Source}
import akka.stream.{IOResult,Materializer}
import com.tgtg.restaurant.domain.importer.domain.Restaurant
import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext,Future}
import scala.util.{Failure,Success}

class RestaurantImporterService(implicit val system : ActorSystem,mat : Materializer,ec : ExecutionContext) extends LazyLogging {
    private[service] var restaurants = scala.collection.mutable.ListBuffer.empty[Restaurant]

    def fromCsvToData(path : String) : Source[List[String],Future[IOResult]] = {
        FileIO
            .fromPath(Paths.get(path))
            .via(CsvParsing.lineScanner())
            .drop(1)
            .mapAsyncUnordered(50)(elem => Future(elem.map(_.utf8String)))
    }

    def parseLine(line : List[String]) : Restaurant = {
        val i= Restaurant(
            line.head,line(1),line(2),line(3),line(4),line(5),line(6),line(7),line(8),
            line(9),line(10),line(11),line(12),line(13),line(14),line(15),line(16),
            line(17),line(18),line(19),line(20),line(21),line(22),line(23),line(24),line(25),
            line(26),line(27))
      val o= i.copy(name = "u")
      var k = 7
      k = k+7
      i
    }

    def uploadCsv(path : String) = {
        val res2 : Source[Restaurant,Future[IOResult]] = fromCsvToData(path)
            .mapAsyncUnordered(50)(res => Future(parseLine(res)))
        collectAll(res2)
    }

    def getRestaurantsByLocation(latitude : String,longitude : String) : ListBuffer[Restaurant] = {
        restaurants.filter(res => res.longitude == longitude & res.lattitude == latitude)
    }

    def collectAll(source : Source[Restaurant,Future[IOResult]]) = source.runWith(Sink.seq[Restaurant]) onComplete (res => res match {
        case Failure(exception) => logger.error(s"uploading failed due to $exception")
        case Success(value) => {
            restaurants = restaurants.addAll(value)
        }
    })

    def getRestaurantByName(name : String) : Option[Restaurant] = {
        restaurants.find(_.name == name)
    }
}
