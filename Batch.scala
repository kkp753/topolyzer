

package net.mossyfeather.topolyzer


object Batch {
	def main(args:Array[String]) {
		printMeshProps(10)
//		printMeshProps(100) // Mesh(32x32) : 1024 routers
		printHCProps(11)   // HC(10)      : 1024 routers
//		printTorusProps(100)


//		println("\nRecursive vs Copy Bench")
//		println("[Rec] : " + benchRec(15))
	//	println("[Cpy] : " + benchCopy(15))

		//println("[Rec] : " + bench(NetBuilder.genHC(12).printRdDHL _))

//		NetBuilder.genMC(1,0).printRdDHL()
//		NetBuilder.genMC(1,1).printRdDHL()
//		NetBuilder.genMC(1,2).printRdDHL()
//		NetBuilder.genMC(1,3).printRdDHL()
//		NetBuilder.genMC(1,4).printRdDHL()
//		NetBuilder.genMC(1,5).printRdDHL()
//		NetBuilder.genMC(1,6).printRdDHL()
//		NetBuilder.genMC(1,7).printRdDHL()
//		NetBuilder.genMC(1,8).printRdDHL()
		NetBuilder.genMC(1,9).printRdDHL() // 524k routers

		NetBuilder.genMC(2,0).printRdDHL()
		NetBuilder.genMC(2,1).printRdDHL()
		NetBuilder.genMC(2,2).printRdDHL()
		NetBuilder.genMC(2,3).printRdDHL()
		NetBuilder.genMC(2,4).printRdDHL()
	
		NetBuilder.genMC(3,0).printRdDHL()
		NetBuilder.genMC(3,1).printRdDHL()
		NetBuilder.genMC(3,2).printRdDHL()

	}

	def printMeshProps(N:Int) {
		printRdDHLHeader()
		(1 to N).foreach{n =>
			NetBuilder.genMesh(n,n).printRdDHL()
		}
	}

	def printTorusProps(N:Int) {
		printRdDHLHeader()
		(1 to N).foreach{n =>
			NetBuilder.genTorus(n,n).printRdDHL()
		}
	}

	def printHCProps(N:Int) {
		printRdDHLHeader()
		(1 to N).foreach{n =>
			NetBuilder.genHC(n).printRdDHL()
		}
	}


	def printRdDHLHeader() {
		println("%6s  %2s  %4s  %10s  %12s".format("R","d","D","H","L"))
	}

	def bench(f:()=>Unit):Long = {
		val st = now
		f()
		val et = now
		et-st
	}

	def now = System.currentTimeMillis()
}


