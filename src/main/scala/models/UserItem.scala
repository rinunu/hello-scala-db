package models

import scalikejdbc._

case class UserItem(
  userId: Int, 
  itemId: Int, 
  count: Int) {

  def save()(implicit session: DBSession = UserItem.autoSession): UserItem = UserItem.save(this)(session)

  def destroy()(implicit session: DBSession = UserItem.autoSession): Unit = UserItem.destroy(this)(session)

}
      

object UserItem extends SQLSyntaxSupport[UserItem] {

  override val tableName = "user_item"

  override val columns = Seq("user_id", "item_id", "count")

  def apply(ui: SyntaxProvider[UserItem])(rs: WrappedResultSet): UserItem = apply(ui.resultName)(rs)
  def apply(ui: ResultName[UserItem])(rs: WrappedResultSet): UserItem = new UserItem(
    userId = rs.get(ui.userId),
    itemId = rs.get(ui.itemId),
    count = rs.get(ui.count)
  )
      
  val ui = UserItem.syntax("ui")

  override val autoSession = AutoSession

  def find(itemId: Int, userId: Int)(implicit session: DBSession = autoSession): Option[UserItem] = {
    withSQL {
      select.from(UserItem as ui).where.eq(ui.itemId, itemId).and.eq(ui.userId, userId)
    }.map(UserItem(ui.resultName)).single.apply()
  }
          
  def findAll()(implicit session: DBSession = autoSession): List[UserItem] = {
    withSQL(select.from(UserItem as ui)).map(UserItem(ui.resultName)).list.apply()
  }
          
  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls"count(1)").from(UserItem as ui)).map(rs => rs.long(1)).single.apply().get
  }
          
  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[UserItem] = {
    withSQL { 
      select.from(UserItem as ui).where.append(sqls"${where}")
    }.map(UserItem(ui.resultName)).list.apply()
  }
      
  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL { 
      select(sqls"count(1)").from(UserItem as ui).where.append(sqls"${where}")
    }.map(_.long(1)).single.apply().get
  }
      
  def create(
    userId: Int,
    itemId: Int,
    count: Int)(implicit session: DBSession = autoSession): UserItem = {
    withSQL {
      insert.into(UserItem).columns(
        column.userId,
        column.itemId,
        column.count
      ).values(
        userId,
        itemId,
        count
      )
    }.update.apply()

    UserItem(
      userId = userId,
      itemId = itemId,
      count = count)
  }

  def save(entity: UserItem)(implicit session: DBSession = autoSession): UserItem = {
    withSQL {
      update(UserItem).set(
        column.userId -> entity.userId,
        column.itemId -> entity.itemId,
        column.count -> entity.count
      ).where.eq(column.itemId, entity.itemId).and.eq(column.userId, entity.userId)
    }.update.apply()
    entity
  }
        
  def destroy(entity: UserItem)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(UserItem).where.eq(column.itemId, entity.itemId).and.eq(column.userId, entity.userId) }.update.apply()
  }
        
}
