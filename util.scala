
package net.mossyfeather.topolyzer

object Utils {
	implicit class PowInt(i:Int) {
		def **(v:Int) = math.pow(i,v).toInt
	}
}


