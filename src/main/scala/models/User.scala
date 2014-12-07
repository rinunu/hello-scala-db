package models

import scalikejdbc._

case class User(
  id: Int, 
  name: Option[String] = None, 
  age: Option[Int] = None) {

  def save()(implicit session: DBSession = User.autoSession): User = User.save(this)(session)

  def destroy()(implicit session: DBSession = User.autoSession): Unit = User.destroy(this)(session)

}
      

object User extends SQLSyntaxSupport[User] {

  override val tableName = "user"

  override val columns = Seq("id", "name", "age")

  def apply(u: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(u.resultName)(rs)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User = new User(
    id = rs.get(u.id),
    name = rs.get(u.name),
    age = rs.get(u.age)
  )
      
  val u = User.syntax("u")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.eq(u.id, id)
    }.map(User(u.resultName)).single.apply()
  }
          
  def findAll()(implicit session: DBSession = autoSession): List[User] = {
    withSQL(select.from(User as u)).map(User(u.resultName)).list.apply()
  }
          
  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls"count(1)").from(User as u)).map(rs => rs.long(1)).single.apply().get
  }
          
  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[User] = {
    withSQL { 
      select.from(User as u).where.append(sqls"${where}")
    }.map(User(u.resultName)).list.apply()
  }
      
  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL { 
      select(sqls"count(1)").from(User as u).where.append(sqls"${where}")
    }.map(_.long(1)).single.apply().get
  }
      
  def create(
    id: Int,
    name: Option[String] = None,
    age: Option[Int] = None)(implicit session: DBSession = autoSession): User = {
    withSQL {
      insert.into(User).columns(
        column.id,
        column.name,
        column.age
      ).values(
        id,
        name,
        age
      )
    }.update.apply()

    User(
      id = id,
      name = name,
      age = age)
  }

  def save(entity: User)(implicit session: DBSession = autoSession): User = {
    withSQL {
      update(User).set(
        column.id -> entity.id,
        column.name -> entity.name,
        column.age -> entity.age
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }
        
  def destroy(entity: User)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(User).where.eq(column.id, entity.id) }.update.apply()
  }
        
}
