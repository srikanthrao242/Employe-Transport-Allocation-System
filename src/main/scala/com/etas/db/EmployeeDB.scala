package com.etas.db

import cats.Show
import com.etas.entities.Employee
import doobie.implicits._
import com.etas.db.DBUtil.Dao

trait EmployeeDB {

  implicit val dao: Dao.Aux[Employee, Int] =
    Dao.derive[Employee, Int]("employee", "id")

  implicit val show: Show[Employee] = Show.fromToString

  val dn = Dao[Employee]

  import dn._

  def getEmployees: List[Employee] =  findAll.transact(MysqlExec.xa).compile.toList.unsafeRunSync()

  def getEmployee(id: Int): Option[Employee] = find(id).transact(MysqlExec.xa).unsafeRunSync()

  def deleteEmployee(id: Int): Int = delete(id).transact(MysqlExec.xa).unsafeRunSync()

  def insertEmployee(ps: Employee): Int = insert(ps).transact(MysqlExec.xa).unsafeRunSync()

  def updateEmployee(id: Int, emp: Employee): Int = update(id, emp).transact(MysqlExec.xa).unsafeRunSync()

}

object EmployeeDB{
  def apply(): EmployeeDB = new EmployeeDB{}
}
