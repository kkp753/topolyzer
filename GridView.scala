
package net.mossyfeather.topolyzer

import scalafx.Includes._
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, CubicCurve}
//import scalafx.scene.input.MouseEvent

import scalafx.scene.text.Font

import scalafx.scene.control.Label

class GridView extends scalafx.scene.layout.Pane {

//	val routers = List(new RouterNode("a",100,100))
//	val links   = List(new LinkNode(100,100,200,200))
	
	def update(net:Net) {
		
		val links   = net.links.toList.map(l =>
			new LinkNode(c2x(l.r1.c), r2y(l.r1.r), c2x(l.r2.c), r2y(l.r2.r))
		)
		val routers = net.routers.toList.zipWithIndex.map{ case (r,i) =>
			new RouterNode(net.aformat(i), c2x(r.c), r2y(r.r))
		}

		children = links ++ routers
	}

	val xofs = 100
	val yofs = 100

	def c2x(c:Int) = xofs + c*100
	def r2y(r:Int) = yofs + r*100
}

class RouterNode(text:String) extends scalafx.scene.layout.StackPane() {
//------------------------------------------------------------------------
//  Members definition/construction
//------------------------------------------------------------------------
	val r = 20
	val l = new Label(text) {
		textAlignment = scalafx.scene.text.TextAlignment.Center
		Option(this.getClass.getResource("fonts/Cica_v1.0.3/Cica-Regular.ttf")) match {
			case Some(url) =>
				font = Font.loadFont(url.toExternalForm, 12)
			case _         =>
				font = Font.default
		}

	}

	// constructor Circle() is NOT exist. Why?
	val g = new Circle() {
		radius  = r
		fill    = Color.White
		stroke  = Color.Black
	}

	children = Array(g, l)
	//background = RegionVisualizer(Color.Blue)

//------------------------------------------------------------------------
//  Functions
//------------------------------------------------------------------------

	def x_=(v:Double) { layoutX = v - r }
	def y_=(v:Double) { layoutY = v - r }

	def this(text:String, x:Int, y:Int) {
		this(text)
		layoutX = x - r
		layoutY = y - r
	}
}

class LinkNode extends CubicCurve {
	
	stroke      = Color.Black
	strokeWidth = 2
	fill        = Color.Transparent
	

	def this(fx:Double,fy:Double,tx:Double,ty:Double) {
		this()
		set(fx,fy,tx,ty)
	}
	
	def set(fx:Double,fy:Double,tx:Double,ty:Double) {
		startX = fx
		startY = fy
		endX   = tx
		endY   = ty
		calcControl(fx,fy,tx,ty)
	}
	

	// 長いリンクほどcベクトルを大きめにとるってのもありかも
	def calcControl(fx:Double,fy:Double,tx:Double,ty:Double) {
		val dx = tx - fx
		val dy = ty - fy
		val v  = Vec2D(dx, dy)
		val n  = v.normal()
		
		val c1 = n*0.15 + v*0.2
		val c2 = n*0.15 - v*0.2

		controlX1 = fx + c1.x
		controlY1 = fy + c1.y
		
		controlX2 = tx + c2.x
		controlY2 = ty + c2.y
	}


	case class Vec2D (val x:Double, val y:Double) {
		def +(a:Vec2D)  = Vec2D(x+a.x, y+a.y)
		def -(a:Vec2D)  = Vec2D(x-a.x, y-a.y)
		def *(a:Double) = Vec2D(x*a, y*a)
		def *(a:Vec2D)  = Vec2D(x*a.x - y*a.y, x*a.y + a.x*y)
		def normal()    = this * Vec2D(0,1) // rotate pi/2 rad
	}
	
}
