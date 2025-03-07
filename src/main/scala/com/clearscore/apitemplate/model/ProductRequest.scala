package com.clearscore.apitemplate.model

import io.circe.generic.semiauto.deriveCodec
import io.circe.Codec

case class ProductRequest(name: String, description: String, image_url: Option[String] = None, price: Int)

object ProductRequest {
  implicit val codec: Codec[ProductRequest] =
    deriveCodec[ProductRequest]
}
