import models.{UserItem, User}
import org.scalatest._

import scalikejdbc._

import scala.io.Source

/**
 * DB に接続、 db.sql を流してテストできる状態にする Fixture です
 */
trait DBFixture extends BeforeAndAfterEach {
  this: Suite =>

  override def beforeEach(): Unit = {
    Class.forName("com.mysql.jdbc.Driver")
    ConnectionPool.singleton("jdbc:mysql://localhost/hello", "root", "")

    setup()
    super.beforeEach()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    DB.localTx { implicit session =>
      sql"drop database hello".execute().apply()
      sql"create database hello".execute().apply()
    }
  }

  private def setup() {
    println("setup")
    DB.localTx { implicit session =>
      val sqls = Source.fromFile("./doc/db.sql").getLines().mkString(" ").split(";")
      sqls.foreach { sql =>
        SQL(sql).execute().apply()
      }
    }
  }
}

class ScalikeJdbcTest extends FunSuite with DBFixture {
  test("select, insert") {
    DB.localTx { implicit session =>
      val id = 1
      val name = "name1"
      val age = 10
      sql"insert into user values ($id, $name, $age)".update.apply()

      val nameOpt = sql"select * from user".map(rs => rs.string("name")).single().apply()
      assert(nameOpt == Some("name1"))
    }
  }

  test("in") {
    pending
  }

  test("取り出したものをオブジェクトにマップする") {
    DB.localTx { implicit session =>
      sql"insert into user values (1, 'name1', 10), (2, 'name2', 20)".update.apply()

      def nameOnly(rs: WrappedResultSet): String = {
        rs.string("name")
      }

      val names = sql"select * from user".map(nameOnly).list().apply()
      assert(names == List("name1", "name2"))
    }
  }

}

/**
 * 自動生成された find 等
 */
class AutoGenTest extends FunSuite with DBFixture {
  test("findAllBy") {
    DB.localTx { implicit session =>
      sql"insert into user values (1, 'name1', 10)".update.apply()

      val users = User.findAllBy(sqls"id = 1")

      assert(users == Seq(User(1, Some("name1"), Some(10))))
    }
  }

}

