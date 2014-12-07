package models

import org.scalatest._
import org.joda.time._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._

class ItemSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val i = Item.syntax("i")

  behavior of "Item"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Item.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Item.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Item.countAll()
    count should be >(0L)
  }
  it should "find by where clauses" in { implicit session =>
    val results = Item.findAllBy(sqls.eq(i.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Item.countBy(sqls.eq(i.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Item.create(id = 123)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Item.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Item.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Item.findAll().head
    Item.destroy(entity)
    val shouldBeNone = Item.find(123)
    shouldBeNone.isDefined should be(false)
  }

}
        