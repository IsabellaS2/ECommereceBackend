package com.clearscore.apitemplate.model

import java.util.UUID

import io.circe.generic.semiauto.deriveCodec
import io.circe.{Codec, Encoder}

case class ProductModel(uuid: UUID, name: String, description: String, price: Int, imageUrl: Option[String] = None)

object ProductModel {
  implicit val codec: Codec[ProductModel] =
    deriveCodec[ProductModel]
}

