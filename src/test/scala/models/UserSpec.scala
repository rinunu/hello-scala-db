package models

import org.scalatest._
import org.joda.time._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._

class UserSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val u = User.syntax("u")

  behavior of "User"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = User.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = User.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = User.countAll()
    count should be >(0L)
  }
  it should "find by where clauses" in { implicit session =>
    val results = User.findAllBy(sqls.eq(u.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = User.countBy(sqls.eq(u.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = User.create(id = 123)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = User.findAll().head
    // TODO modify something
    val modified = entity
    val updated = User.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = User.findAll().head
    User.destroy(entity)
    val shouldBeNone = User.find(123)
    shouldBeNone.isDefined should be(false)
  }

}
        