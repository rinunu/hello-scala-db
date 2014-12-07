package models

import scalikejdbc._

case class Item(
  id: Int, 
  name: Option[String] = None) {

  def save()(implicit session: DBSession = Item.autoSession): Item = Item.save(this)(session)

  def destroy()(implicit session: DBSession = Item.autoSession): Unit = Item.destroy(this)(session)

}
      

object Item extends SQLSyntaxSupport[Item] {

  override val tableName = "item"

  override val columns = Seq("id", "name")

  def apply(i: SyntaxProvider[Item])(rs: WrappedResultSet): Item = apply(i.resultName)(rs)
  def apply(i: ResultName[Item])(rs: WrappedResultSet): Item = new Item(
    id = rs.get(i.id),
    name = rs.get(i.name)
  )
      
  val i = Item.syntax("i")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Item] = {
    withSQL {
      select.from(Item as i).where.eq(i.id, id)
    }.map(Item(i.resultName)).single.apply()
  }
          
  def findAll()(implicit session: DBSession = autoSession): List[Item] = {
    withSQL(select.from(Item as i)).map(Item(i.resultName)).list.apply()
  }
          
  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls"count(1)").from(Item as i)).map(rs => rs.long(1)).single.apply().get
  }
          
  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Item] = {
    withSQL { 
      select.from(Item as i).where.append(sqls"${where}")
    }.map(Item(i.resultName)).list.apply()
  }
      
  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL { 
      select(sqls"count(1)").from(Item as i).where.append(sqls"${where}")
    }.map(_.long(1)).single.apply().get
  }
      
  def create(
    id: Int,
    name: Option[String] = None)(implicit session: DBSession = autoSession): Item = {
    withSQL {
      insert.into(Item).columns(
        column.id,
        column.name
      ).values(
        id,
        name
      )
    }.update.apply()

    Item(
      id = id,
      name = name)
  }

  def save(entity: Item)(implicit session: DBSession = autoSession): Item = {
    withSQL {
      update(Item).set(
        column.id -> entity.id,
        column.name -> entity.name
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }
        
  def destroy(entity: Item)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Item).where.eq(column.id, entity.id) }.update.apply()
  }
        
}
