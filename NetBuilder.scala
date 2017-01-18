
package net.mossyfeather.topolyzer

import scala.collection.mutable.{MutableList => MList}
import Utils._

object NetBuilder{

	// routers with alignment
	def genMeshRouters(r:Int, c:Int) = {
		val rbuf = MList.empty[Router] // Routers List Buffer
		(0 until r).foreach {y =>
			(0 until c).foreach {x =>
				rbuf += Router(y,x) // row,col
			}
		}
		rbuf
	}

	def genMeshLinks(r:Int, c:Int, rbuf:MList[Router]) = {
		val lbuf = MList.empty[Link] // Link List Buffer
		// gen links                        // Example for 3x3
		(0 until r-1).foreach {y =>         // 1.Vertical
			(0 until c).foreach {x =>        //   +   +   +
				val i1 = y*c + x              //  1|  2|  3|
				val i2 = i1 + c // down       //   +   +   +
				lbuf += rbuf(i1) <-> rbuf(i2) //  4|  5|  6|
			}                                //   +   +   +
		}
		(0 until c-1).foreach {x =>         // 2.Horizontal
			(0 until r).foreach {y =>        //   +-7-+-8-+
				val i1 = y*c + x              //   |   |   |
				val i2 = i1 + 1 // right      //   +-9-+-10+
				lbuf += rbuf(i1) <-> rbuf(i2) //   |   |   |
			}                                //   +-11+-12+
		}
		lbuf
	}

	def genMesh(r:Int, c:Int) = {
		val rbuf = genMeshRouters(r,c)
		val lbuf = genMeshLinks(r,c,rbuf)
		// asymmetric
		new Net(false, r, c, rbuf, lbuf, "Mesh(%dx%d)".format(r,c))
	}

	def genTorus(r:Int, c:Int) = {
		val rbuf = genMeshRouters(r,c)
		val lbuf = genMeshLinks(r,c,rbuf)
		// add wrap-around links            //   +---+---+
		(0 until c).foreach {x =>           // 1/| 2/| 3/|
			val i1 = x                       //  |+--|+--|+
			val i2 = x + (r-1)*c             //  \|  \|  \|
			lbuf += rbuf(i1) <-> rbuf(i2)    //   +---+---+
		}                                   // 1________
		(0 until r).foreach {y =>           //  `+---+--`+
			val i1 = y*c                     // 2_|___|__ |
			val i2 = i1 + c-1                //  `+---+--`+
			lbuf += rbuf(i1) <-> rbuf(i2)    // 3_|___|__ |
		}                                   //  `+---+--`+
		// symmetric
		new Net(true, r, c, rbuf, lbuf, "Torus(%dx%d)".format(r,c))
		
	}

	def genHC(n:Int):Net = {
		if (n == 1) {
			val rbuf = MList(Router(0,0), Router(0,1)) // 1+--+2
			val lbuf = MList(rbuf(0) <-> rbuf(1))
			new Net(true, 1, 2, rbuf, lbuf, "HC(1)")
		} else {
			// 2 * (n-1)-cube
			val n1 = genHC(n-1)
			val n2 = genHC(n-1)
			
			// offset : 2**floor((n-1)/2)
			if (n%2 == 0) // even -> V-offset
				n2.offset(hcOfs(n), 0)
			else          // odd  -> H-offset
				n2.offset(0, hcOfs(n))

			// links between 2 cubes
			val lbuf = MList.empty[Link]
			(n1.routers zip n2.routers).foreach({ case(r1,r2) =>
				lbuf += r1 <-> r2
			})
			
			new Net(true,
				if (n%2 == 0) n1.rows*2 else n1.rows,
				if (n%2 != 0) n1.cols*2 else n2.cols,
				// (n-1)-cube*2 + additional links
				n1.routers ++ n2.routers,
				n1.links   ++ n2.links    ++ lbuf,
				"HC(%d)".format(n)
			)
		}
	}

	def hcOfs(n:Int)     = 2**((n-1)/2)

//----------------------------------------------------------------------
//  Metacube
//----------------------------------------------------------------------

// MC(k,m)  :  2^(2^k) * MC(k,m-1)
// MC(k,0)  :  HC(k)
//
// Address for k=1:
//  MC(1,0) : HC(1)
//    0  1
//    +--+
//  MC(1,1) : 2*MC(1,0)
//    00x   01x
//    +--+  +--+
//     `-|--`  |
//    +--+  +--+
//    `-----`
//    10x   11x
//
// Address for k=3:
//  mmmmmmmmmmmmmmmmkkk
//  \______/\______/\_/
//   2^(2^k)-bit*m  +  k-bit

	def genMC(k:Int,m:Int):Net = {
		if (m == 0) { // m=0 -> HC(k)
			genHC(k)
		} else {
			genClusterArray(k,m)
		}
	}

//	NetBuilder.genMCsss().toNet(true, ?,?,buf,"format...")
//	NetBuilder.genMC()

	def genClusterArray(k:Int, m:Int):Net = {
		
		var c = genMC(k, m-1) // cluster
		// Array Size
		val asize = 2**(2**k) // 4, 16, 256, 65536, ...
		// sqrt (Rows/Cols of the Array)
		val asqrt = math.sqrt(asize).toInt // 2, 4, 16, 256, ...

		// Buffer
		val rbuf = MList.empty[Router]
		val lbuf = MList.empty[Link]

		// Arrange <asize> Clusters
		(0 until asqrt).foreach { y=>
			(0 until asqrt).foreach { x=>
				if (y!=0 || x!=0) {
					c = genMC(k, m-1) // gen or copy
					c.offset(y*c.rows, x*c.cols) // Offset
				}
				rbuf ++= c.routers
				lbuf ++= c.links
			}
		}
		// Connect Clusters
		// [MC(2,2)] : m*(2**k) + k = 10bit address
		//    0000_0000_00 <-> 0001_0000_00
		//       ^    ^    `-> 0000_0001_00
		//
		//    0000_0000_01 <-> 0010_0000_00
		//      ^    ^     `-> 0000_0010_00
		//
		val linked = Array.fill(rbuf.size)(false)
		val shifter = (m-1)*(2**k) + k
		val divider = 2**shifter
		(0 until rbuf.size).foreach {sadr => // Source address
			if (!linked(sadr)) {
				// Flip-bit Select (less significant k-bit)
				val fsel = sadr % (2**k)
				// One-hot bit flipper 0:0001, 1:0010, 2:0100, 3:1000
				val flipper = 2**fsel

				// Offset (less significant ((m-1)*(2**k) + k)-bit)
				val sofs = sadr % divider
				// Cluster ID (most significant (2**k)-bit)
				val scls = sadr >> shifter //TODO unsigned?

				// Destination Cluster (Flip fsel-th bit : scls XOR flipper)
				val dcls = scls ^ flipper
				// Destination address
				val dadr = (dcls << shifter) | sofs

				// Link!
				lbuf += rbuf(sadr) <-> rbuf(dadr)
				linked(sadr) = true
				linked(dadr) = true
			}
		}

		new Net(true,
			c.rows * asqrt, // rows
			c.cols * asqrt, // cols
			rbuf,
			lbuf,
			"MC(%d,%d)".format(k,m)
		)

	}
}




