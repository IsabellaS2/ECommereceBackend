package com.clearscore.apitemplate.service

import cats.effect.IO
import com.clearscore.apitemplate.db.ProductRepository
import com.clearscore.apitemplate.model.{ProductModel, ProductRequest}

import java.util.UUID
import scala.util.{Failure, Success, Try}

trait ProductService(productRepository: ProductRepository) {
  def getProductList: IO[List[ProductModel]]
  def addProduct(request: ProductRequest): IO[Unit]
  def getProductById(id: UUID): IO[Option[ProductModel]]
}

// productRepository.getProductList = IO[List[ProductModel]]
// .map ( innerOfTheIo (List[ProductModel]) => )

class ProductServiceImpl(productRepository: ProductRepository) extends ProductService(productRepository) {
  override def getProductList: IO[List[ProductModel]] = {
    for {
      list <- productRepository.getProductList
      _ <- IO.println("List: " + list)
    } yield list

  }
  override def addProduct(request: ProductRequest): IO[Unit] = {
    val uuid = UUID.randomUUID()
    val product = ProductModel(uuid = uuid, name = request.name, description = request.description, imageUrl = request.image_url, price = request.price)
    productRepository.addProduct(product)
  }

  override def getProductById(id: UUID): IO[Option[ProductModel]] = {
    for {
      product <- productRepository.getProductById(id)
    } yield product
  }
}

