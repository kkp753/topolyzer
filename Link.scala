
package net.mossyfeather.topolyzer

import scala.collection.mutable.{MutableList => MList}

case class Link (
	val r1:Router,
	val r2:Router
){
	val d:Direction = Direction.BiDir
	
	def lengthManhattan = (r1.r - r2.r).abs + (r1.c - r2.c).abs

	def partnerWith(r:Router) = if (r == r1) r2 else r1

	sealed abstract class Direction (val id:Int)
	object Direction {
		case object BiDir  extends Direction(0)
		case object UniDir extends Direction(1)
	}
}

//case class Address(val i:Int) {
//	val format:(Int => String) = (_ => _.toBinaryString)
//	def toString() = format(i)
//}

