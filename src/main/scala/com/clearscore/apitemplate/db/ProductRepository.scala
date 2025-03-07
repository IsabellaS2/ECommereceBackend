package com.clearscore.apitemplate.db

import cats.effect.IO
import cats.implicits.*
import com.clearscore.apitemplate.model.ProductModel
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.transactor.Transactor

import java.util.UUID

trait ProductRepository {
  def getProductList: IO[List[ProductModel]]
  def addProduct(product: ProductModel): IO[Unit]
  def getProductById(id: UUID): IO[Option[ProductModel]]
//  def addTeamMember(teamMemberName: String): IO[TeamMember]
//  def removeTeamMember(teamMemberID: UUID): IO[Boolean]
//  def editTeamMember(teamMemberID: UUID, newName: String): IO[Boolean]
}

class ProductRepositoryImpl(transactor: Transactor[IO]) extends ProductRepository {
  override def getProductList: IO[List[ProductModel]] = {
    val fragment = sql"SELECT uuid, name, description, price, image_url FROM congo.products"
    val action = fragment
      .query[ProductModel]
      .to[List]

    action.transact(transactor)
  }

  //work out this error
  //root org.postgresql.util.PSQLException: No results were returned by the query. -> error returned

  override def addProduct(product: ProductModel): IO[Unit] = {
     val fragment =
       sql"""
         INSERT INTO congo.products (uuid, name, description, price, image_url)
         VALUES (${product.uuid}, ${product.name}, ${product.description}, ${product.price}, ${product.imageUrl})
         """

     val action = fragment
       .update
//       .withGeneratedKeys[ProductModel]("uuid", "name", "description", "price", "image_url")
       .run
       .void

     action.transact(transactor)
  }

  override def getProductById(id: UUID): IO[Option[ProductModel]] = {
    val fragment =
      sql"""
           SELECT uuid, name, description, price, image_url  FROM congo.products WHERE uuid = ${id}
         """

    val action = fragment
      .query[ProductModel]
      .option


    action.transact(transactor)
  }

//
//  override def removeTeamMember(teamMemberID: UUID): IO[Boolean] = {
//    val teamMember = StarterFakeDB.teamMembersTable.find(tm => tm.id == teamMemberID)
//    val result = teamMember match {
//      case Some(teamMember) => {
//        StarterFakeDB.teamMembersTable -= teamMember
//        true
//      }
//      case None => false
//    }
//
//    IO.delay(result) // pretend it's a DB (some side-effect)
//  }
//
//  override def editTeamMember(teamMemberID: UUID, newName: String): IO[Boolean] = {
//    //    for comprehension version
//    //    val response = for {
//    //      teamMember <- StarterFakeDB.teamMembersTable.find(tm => tm.id == teamMemberID) // Option[TeamMember]
//    //      teamMemberIndex = StarterFakeDB.teamMembersTable.indexOf(teamMember)
//    //      updatedTeamMember = teamMember.copy(name = newName)
//    //      _ = StarterFakeDB.editTeamMember(teamMemberIndex, teamMember)
//    //    } yield ()
//
//    val teamMemberOption = StarterFakeDB.teamMembersTable.find(tm => tm.id == teamMemberID)
//    // Some(TeamMember("jess" "23984203984"))
//    // None
//    // ListBuffer[TeamMember]
//    val result = teamMemberOption match {
//      case Some(teamMember) => {
//        val teamMemberIndex = StarterFakeDB.teamMembersTable.indexOf(teamMember)
//        val updatedTeamMember = teamMember.copy(name = newName)
//        // call starter fake db method
//        StarterFakeDB.editTeamMember(teamMemberIndex, updatedTeamMember)
//        true
//      }
//      case None => false
//    }
//
//    IO.delay(result) // pretend it's a DB (some side-effect)
//  }
//
//  private def randomUUID(): UUID = UUID.randomUUID()
}
