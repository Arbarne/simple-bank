package fr.fpe.school
package h2.repository

import database.AccountRepository
import h2.implicits._
import h2.query.AccountQueries
import model.Account

import cats.effect.IO
import doobie.h2.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor

import java.time.OffsetDateTime
import java.util.UUID

final class H2AccountRepository(transactor: Transactor[IO]) extends AccountRepository {
  override def insert(name: String): IO[Account] =
    AccountQueries
      .insert(name)
      .withUniqueGeneratedKeys[(UUID, OffsetDateTime)]("id", "creation_date")
      .map { case (id, creationDate) =>
        Account(id, name, creationDate)
      }
      .transact(transactor)

}
