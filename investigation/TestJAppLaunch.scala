
// package 

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.Group
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint._
import scalafx.scene.input.MouseEvent
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
import scalafx.scene.shape.Rectangle


object test {
	def main (args:Array[String]) {
	
		//var stage = new scalafx.stage.Stage {
		var stage = new scalafx.application.JFXApp.PrimaryStage {
			title = "TEST"
			scene = new Scene{
				//padding = Insets(20)
				content = new Rectangle{
					width = 200
					height = 200
					fill = Color.SkyBlue
				}
			}
		}
	
		val app = new MyApp()
		javafx.application.Application.launch(classOf[MyApp], args: _*)
//		app.start(stage)
//		val stage = ScalaFx
//		app.init()
//		app.start(stage)

//		JFXApp.ActiveApp = app

	}
}

// ActiveApp    : JFXApp
// ActiveJFXApp : javafx.application.Application

class MyApp extends javafx.application.Application {
	
	var japp :javafx.application.Application = null
	var sapp :JFXApp = null
	var stage :javafx.stage.Stage = null

	def start(s: javafx.stage.Stage) {
		
		japp = this
		
//		sapp.init() // DelayedInit

		stage = s

		//stage.setTitle("")
		stage.show()
	}

//	override def stop() {
//		sapp.stopApp()
//	}
}

/*
class MyApp extends JFXApp {
	stage = new PrimaryStage {
		title = "TEST"
		scene = new Scene{
			root = new StackPane{
				padding = Insets(20)
				content = new Rectangle{
					width = 200
					height = 200
					fill = Color.SkyBlue
				}
			}
		}
	}
}
*/



