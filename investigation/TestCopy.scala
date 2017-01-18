
import scala.collection.mutable.{MutableList => MList}

object Test {
	def start() {
		val rbuf = MList.empty[Router]
		val lbuf = MList.empty[Link]

		rbuf ++= MList(Router(1), Router(2))
		lbuf +=  Link(rbuf(1), rbuf(2))
		val n1 = Net(rbuf.toList, lbuf.toList)
		val n2 = n1.copy

		println("n1 : " + n1)
		println("n2 : " + n2)
	}
}

case class Net(val routers:List[Router], val links:List[Link])

case class Router(id:Int) {
	val l = MList.empty[List]
}

case class Link(r1:Router, r2:Router) {
	r1.l += this
	r2.l += this
}


