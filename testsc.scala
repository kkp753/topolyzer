
package net.mossyfeather.mcutil.nbt


import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.Group
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint._
import scalafx.scene.input.MouseEvent
import scalafx.scene.shape.Rectangle
// Controls
//import scalafx.scene.control.Button
import scalafx.scene.control.TreeItem
import scalafx.scene.control.TreeCell
import scalafx.scene.control.TreeView
// Images
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.image.WritableImage
// Layouts
//import scalafx.scene.layout._
//import scalafx.scene.layout.VBox
import scalafx.scene.layout.{StackPane, Background, BackgroundFill, CornerRadii}
import scalafx.scene.layout.BorderPane
// Geometry
import scalafx.geometry.{Insets}

object NBTViewer extends JFXApp {

	// Stage: Top level container
	stage = new JFXApp.PrimaryStage {
		title  = "NBT Viewer"
		width  = 400
		height = 500
		
		// Scene:
		scene  = createScene()
	}

	def createScene() = {
		new Scene(700,500){ _s =>
			root = new BorderPane(){
				content = new Rectangle {
					width  <== _s.width
					height <== _s.height
				}
			}
		}
	}

/*
# Scene Init
 - Scene.content : Scene.root.getChildren
 - Scene.root    : Group / Pane

# Self Types for mix-in



*/
}
