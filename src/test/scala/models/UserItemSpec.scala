package models

import org.scalatest._
import org.joda.time._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._

class UserItemSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val ui = UserItem.syntax("ui")

  behavior of "UserItem"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = UserItem.find(123, 123)
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = UserItem.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = UserItem.countAll()
    count should be >(0L)
  }
  it should "find by where clauses" in { implicit session =>
    val results = UserItem.findAllBy(sqls.eq(ui.itemId, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = UserItem.countBy(sqls.eq(ui.itemId, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = UserItem.create(userId = 123, itemId = 123, count = 123)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = UserItem.findAll().head
    // TODO modify something
    val modified = entity
    val updated = UserItem.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = UserItem.findAll().head
    UserItem.destroy(entity)
    val shouldBeNone = UserItem.find(123, 123)
    shouldBeNone.isDefined should be(false)
  }

}
        