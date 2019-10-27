import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import com.etas.{AkkaCoreModule, Router}
import com.etas.book.Booking
import com.etas.db.{CabDB, DriverDB, EmployeeDB}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}
import com.etas.entities.Employee
import com.etas.entities.EntityAndSer._

import scala.concurrent.duration._
import scala.io.Source
import spray.json._
import DefaultJsonProtocol._
import com.etas.routers.EmployeeRouter

class EtasSpec
  extends WordSpec
  with Matchers
  with MockFactory
  with ScalatestRouteTest {
  val emp = Source
    .fromResource("employee.json")
    .getLines()
    .mkString(" ")
    .parseJson
    .convertTo[Employee]
  trait Init {
    val cab = mock[CabDB]
    val employee = mock[EmployeeDB]
    val driver = mock[DriverDB]
    val booking = mock[Booking]
    val eRoute = new EmployeeRouter with AkkaCoreModule {
      override val employeeDB: EmployeeDB = employee
    }.employeeRoutes

  }
  implicit def default(implicit system: ActorSystem) =
    RouteTestTimeout(5.second)

  "The service" should {

    "return list of employees" in new Init {
      (employee.getEmployees _).expects().returns(List(emp))
      Get("/employees") ~> eRoute ~> check {
        val res = responseAs[String].parseJson
          .convertTo[List[Employee]]
          .head
        res shouldEqual emp
      }
    }
    "return employee with id" in new Init{
      (employee.getEmployee _).expects(1).returns(Some(emp))
      Get("/employee/1") ~> eRoute ~> check {
        val res = responseAs[String].parseJson
          .convertTo[Employee]
        res shouldEqual emp
      }
    }
  }

}
