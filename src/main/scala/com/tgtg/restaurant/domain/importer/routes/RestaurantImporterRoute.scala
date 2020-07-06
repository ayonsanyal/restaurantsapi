package com.tgtg.restaurant.domain.importer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.unmarshalling.PredefinedFromStringUnmarshallers._
import akka.stream.Materializer
import com.tgtg.restaurant.domain.importer.service.RestaurantImporterService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._
import scala.language.postfixOps._
import scala.concurrent.ExecutionContext

class RestaurantImporterRoute(importerService : RestaurantImporterService)(implicit val system : ActorSystem,mat : Materializer,ec : ExecutionContext)
    extends FailFastCirceSupport {

    import akka.http.scaladsl.server.Directives._

    def routes = {
        pathPrefix("api") {
            pathPrefix("v1") {
                path("restaurants") {
                    pathEndOrSingleSlash {
                        get {
                            parameter("name".as[String]) { name =>
                                complete(importerService.getRestaurantByName(name).asJson)
                            }
                        }~
                        get{
                            parameters("longitude".as[String],"lattitude".as[String]) {(longi,lati) =>
                                complete(importerService.getRestaurantsByLocation(lati,longi).asJson)
                            }
                        }
                    }

                }
            }
        }
    }
}