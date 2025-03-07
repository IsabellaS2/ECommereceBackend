package com.clearscore.apitemplate.http

import cats.effect.IO
import com.clearscore.apitemplate.model.ProductRequest
import com.clearscore.apitemplate.service.ProductService
import org.http4s.*
import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.dsl.Http4sDsl

class ProductRoutes(productService: ProductService)
  extends Http4sDsl[IO] {
  val routes: HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root => {
        for {
          productList <- productService.getProductList
          response <- Ok(productList)
        } yield response
      }

      case req@POST -> Root / "add-product" => {
        for {
          productRequest <- req.as[ProductRequest]
          response <- Created(productService.addProduct(productRequest))
//          teamMemberRequest <- req.as[TeamMemberRequest]
//          response <- Created(getStartedService.addTeamMember(teamMemberRequest.name))
        } yield response
      }

      case GET -> Root / UUIDVar(productId) => {
        for {
          productUrl <- productService.getProductById(productId) // needs to be implemented
          response <- productUrl match {
            case Some(product) => Ok(product)
            case None => NotFound("🫣")
          }
        } yield response
      }



//      case req @ POST -> Root / "team-member" => {
//        for {
//          teamMemberRequest <- req.as[TeamMemberRequest]
//          response <- Created(getStartedService.addTeamMember(teamMemberRequest.name))
//        } yield response
//      }
//
//      case DELETE -> Root / "remove-team-member" / teamMemberId =>
//        for {
//          removeTeamMemberServiceResponse <- getStartedService.removeTeamMember(UUID.fromString(teamMemberId)) // we assume we receive an int from the request param
//          response <- if (removeTeamMemberServiceResponse) {
//            NoContent()
//          } else {
//            NotFound("team member not found")
//          }
//        } yield response
//
//      case req @ PUT -> Root / "edit-team-member" / teamMemberId =>
//        for {
//          teamMemberRequest <- req.as[TeamMemberRequest]
//          editTeamMemberServiceResponse <- getStartedService.editTeamMember(UUID.fromString(teamMemberId), teamMemberRequest.name)
//          response <- if (editTeamMemberServiceResponse) {
//            Ok()
//          } else {
//            NotFound("There's no team member with that UUID!")
//          }
//        } yield response
    }
}
