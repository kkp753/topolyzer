
package net.mossyfeather.topolyzer

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

import scalafx.scene.shape.Circle
import scalafx.scene.layout.Pane
import scalafx.scene.layout.Background
import scalafx.scene.layout.BackgroundFill

import scalafx.scene.paint.Paint
import scalafx.scene.paint.Color

object Topolyzer extends JFXApp {

	val grid = new GridView()

	stage = new JFXApp.PrimaryStage {
		title  = "Topolyzer GUI"
		width  = 400
		height = 500

		scene  = new Scene(){
			// Scene.content : Scene.root.getChildren
			// Scene.root    : Group/Pane
			root = new BorderPane(){
				center = grid
			}
		}
	}

}


object RegionVisualizer {

	def apply (fill:Paint) = {
		val radii  = scalafx.scene.layout.CornerRadii.Empty
		//         = new scalafx.scene.layout.CornerRadii(10)
		val insets = scalafx.geometry.Insets.Empty
		//         = scalafx.geometry.Insets(t,r.b,l)

		new Background(Array(new BackgroundFill(fill,radii,insets)))
	}
}


