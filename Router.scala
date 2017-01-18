
package net.mossyfeather.topolyzer

import scala.collection.mutable.{MutableList => MList}

object Router {
	def apply(r:Int, c:Int) = {
		new Router(r, c, 1, MList.empty)
	}

	def apply(r:Int, c:Int, p:Int) = {
		new Router(r, c, p, MList.empty)
	}
}

class Router (
	var r:Int,        // Row
	var c:Int,        // Column
	val p:Int,        // connected Processing Core
	val l:MList[Link] // Connected Links
){
	
	def d = l.size

	def moveTo(r:Int, c:Int) {
		this.r = r
		this.c = c
	}

	def move(dr:Int, dc:Int) {
		this.r += dr
		this.c += dc
	}

	def partners() = l.map(_.partnerWith(this))


	def <-> (that:Router) = this.connects(that)
	def connects(that:Router) = {
		val l = Link(this, that)
		this.l += l
		that.l += l
		l
	}
}

