
package net.mossyfeather.mcutil.nbt


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

import TagType._
import ResourceManager._

object NBTViewer extends JFXApp {


	var tree  = new TreeItem("No Item", NBTIcons(TAG_Compound))
	val tview = new TreeView(tree)

	// Stage: Top level container
	stage = new JFXApp.PrimaryStage {
		title  = "NBT Viewer"
		width  = 700
		height = 500
		
		// Scene:
		scene  = createScene()
	}

	def createScene() = {
		//TODO command line arguments
		//	println(parameters.unnamed)
		//	println(parameters.named)
		val nbt = new NBTDriver("level.dat").readFromRoot()
		tree = nbt2tree("(root)", nbt(""))
		tree.expanded = true
		tview.root = tree
//		tview.editable = true
//		tview.cellFactory = NBTTreeCell.factory
		// region check
//		tview.background = new Background(Array(new BackgroundFill(Color.Blue, new CornerRadii(20), Insets.Empty)))

		println("Treeview.managed : " + tview.managed)
		
		new Scene(700,500){ _s =>
			content = new BorderPane(){
				center = tview
				// Property Binding
				tview.prefWidth <== _s.width
				tview.prefHeight <== _s.height
			}
			stylesheets = loadStylesheets(_s.stylesheets)
		}
	}

	def loadStylesheets(defaultstyles : Iterable[String]):Iterable[String] = {
		val slist = List(
			"stylesheets/NBTTreeView.css"
		)
		defaultstyles ++ slist.map(getResourcePath(_)).filterNot(_.isEmpty).map(_.get)
	}


	// [Error] new TreeItem("content")
	// [OK]    new javafx.scene.control.TreeItem("content")
	def nbt2tree(tname:String, tload:Payload):javafx.scene.control.TreeItem[String] = {
		val ttype = tload.ttype
		val icon = NBTIcons(ttype)
		val treevalue = 
			if (!ttype.hasName) // TAG_End => error
				"[ERROR : TAG_End]"
			else if (ttype.isContainer) // TAG_List | TAG_Compound => name only
				tname
			else tname + " : " + {ttype match { // other => name : value
					case TAG_Byte       => tload.value[Byte].toString
					case TAG_Short      => tload.value[Short].toString
					case TAG_Int        => tload.value[Int].toString
					case TAG_Long       => tload.value[Long].toString
					case TAG_Float      => tload.value[Float].toString
					case TAG_Double     => tload.value[Double].toString
					case TAG_String     => tload.value[String]
					case TAG_Byte_Array => tload.value[Array[Byte]].toString //TODO
					case TAG_Int_Array  => tload.value[Array[Int]].toString // TODO
					case _              => "[ERROR : TAG_Undefined]"
				}}
		
		
		val treeitem = new TreeItem[String](treevalue, icon)

		treeitem.children ++= {ttype match {
			case TAG_Compound => tload.value[Map[String,Payload]]()
			                          .map({ case (cname, cload) => nbt2tree(cname, cload) })
			case TAG_List     => tload.value[Vector[Payload]]().zipWithIndex
			                          .map({ case (cload, index) => nbt2tree(index.toString, cload) })
			case _            => List()
		}}

		treeitem
	}
}

//TODO Future work : GUI for Tag edit
// final class NBTTreeCell extends TreeCell[String] {}
// object NBTTreeCell {	
// 	val factory:(TreeView[String] => TreeCell[String]) = (_ => new NBTTreeCell())
// }



object ResourceManager {

	def getResourcePath(f:String):Option[String] = {
		// Class#getResource : URL@found, null@not-found
		//  - "/aaa/bbb/ccc.jpg" -> <root directory>/aaa/bbb/ccc.jpg
		//  - "aaa/bbb/ccc.jpg"  -> <package directory>/aaa/bbb/ccc.jpg
		Option[java.net.URL](this.getClass.getResource(f)) match {
			case Some(url) =>
				Option(url.toExternalForm)
			case None      =>
				println("[ERROR] Resource File Not Found : " + f)// + " in " + this.getClass.getResource(""))
				None
		}
	}

	object NBTIcons {
		// ImageView (subclass of Node) can only have ONE-parent
		// Don't cache a ImageView, Create new ImageView on demand
		def apply(ttype:TagType) = new ImageView(imgs(ttype))
		// load icon images
		val imgs:Map[TagType, Image] = 
			TagType.values.filter(_ != TAG_End).map(t => (t, tag2img(t))).toMap

		def tag2img (t:TagType) = {
			getResourcePath("images/icon_%s.png".format(t)) match {
				case Some(p) => new Image(p)
				case None    => new WritableImage(16,16)
			}
		}
	}

}
