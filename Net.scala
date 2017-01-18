
package net.mossyfeather.topolyzer

import scala.collection.mutable.{MutableList => MList}

class Net(
	val symmetric :Boolean,
	val rows      :Int,
	val cols      :Int,
	val routers   :MList[Router],
	val links     :MList[Link],
	var aformat   :(Int => String),
	val desc      :String
) {
	// default address format
	def this(sym:Boolean, rows:Int, cols:Int, rs:MList[Router], ls:MList[Link],desc:String) {
		this(sym, rows, cols, rs, ls, (i=>i.toString), desc)
	}
	// no links
//	def this(sym:Boolean, rows:Int, cols:Int, rs:MList[Router]) {
//		this(sym, rows, cols, rs, MList.empty[Link])
//	}
	// Empty
//	def this(sym:Boolean) {
//		this(sym, MList.empty[Router])
//	}

	// print
	//  - D Diameter    Max of the shortest path between any tow routers
	//  - d Degree      Max Links connected one Router
	//  - R #Routers
	//  - P #Cores
	//  - L Sum of Link Length
	//L(sum of link length)
	def printFeatures() {
		println("--------------------------------")
		println(" [%s]".format(desc))
		println("  D:%d, d:%d".format(D,d))
		println("  R:%d, L:%d".format(R,L))
		println("  H:%f".format(H))
		println("--------------------------------")
	}

	def printRdDHL() {
		val (maxh, avgh) = Hops
		println("%6d  %2d  %4d  %10.5f  %12d  # %s : %dx%d".format(
			R,d,maxh,avgh,L,desc,rows,cols
		))
	}

//	def CP(P:Int, a:Double, c:Double, p:Double, t:Double) = {
//		(a*math.pow(d+p, c)*R + (1.0-a)*math.sqrt(p)*L)*t*p*D/P
//	}

	def R = routers.size              // #Routers
	def D = maxHop                    // Diameter
	def d = routers.map(_.d).max      // Degree
	def L = sumLinkLength             // Some of Link Length
	// TODO H ? A ?
	def H = avgHop                    // Average Hops between any pair

	def innerR = R - (4.0*(math.sqrt(R)-1.0)).toInt

	def avgHop = {                                          // Avg. Hops
		val hops = new HopMapBuilder(routers.toList)
		if (symmetric) hops.calcFrom(0).avg
		else           (0 until R).map(hops.calcFrom(_).avg).sum / R
	}

	def maxHop = {
		val hops = new HopMapBuilder(routers.toList)
		if (symmetric) hops.calcFrom(0).max
		else           (0 until R).map(hops.calcFrom(_).max).max
	}

	def Hops = {
		val hops = new HopMapBuilder(routers.toList)
		if (symmetric){
			val buf = hops.calcFrom(0)
			(buf.max, buf.avg)
		} else {
			val tuplist = (0 until R).map{src =>
				val buf = hops.calcFrom(src)
				(buf.max, buf.avg)
			}
			(tuplist.map(_._1).max, tuplist.map(_._2).sum / R)
		}
	}

	def sumLinkLength = {
		links.map(_.lengthManhattan).sum
	}

	def offset(r:Int, c:Int) {
		routers.foreach(_.move(r,c))
	}
	

	def copy() = {
		val rbuf = MList.empty[Router]
		val lbuf = MList.empty[Link]
		routers.foreach{r =>
			rbuf += Router(r.r, r.c, r.p)
		}
		links.foreach{l =>
			val i1 = routers.indexOf(l.r1)
			val i2 = routers.indexOf(l.r2)
			lbuf += rbuf(i1) <-> rbuf(i2)
		}

		new Net(symmetric, rows, cols, rbuf, lbuf, desc)
	}
}



class HopMap(src:Int, hops:List[Int]) {
	def this(src:Int, hopsArray:Array[Int]) {
		this(src, hopsArray.toList)
	}
	def avg  = 1.0 * sum / size
	def sum  = hops.sum
	def max  = hops.max
	def size = hops.size
}

class HopMapBuilder(val routers:List[Router]) {
	
	// 固定長/要素可変
	val hops = Array.fill(routers.size)(Int.MaxValue)

	// 可変長Set
	import scala.collection.mutable.{Set => MSet}
	val updated = MSet.empty[Int]
	val next    = MSet.empty[Int]

	def calcFrom(src:Int) = {
		// Init
		(0 until hops.size).map(hops.update(_, Int.MaxValue))
		updated.clear()
		next.clear()
	
		next += src
		updateNext(0)

		new HopMap(src, hops)
	}


	def updateNext(hop:Int) {
		// toSet -> immutable
		next.toSet.foreach(updateIfLess(_:Int, hop))
		next --= (next & updated)
		if (next.nonEmpty) updateNext(hop+1)
	}


	def updateIfLess(idx:Int, hop:Int) {
		update(idx, hops(idx).min(hop))
	}

	def update(idx:Int, value:Int) {
		hops.update(idx, value)
		updated += idx
		next ++= partnersWith(idx)
	}

	def partnersWith(idx:Int) = {
		routers(idx).partners.map(p => routers.indexOf(p)).toSet
	}
}


