
/*
# Self Type Aannotation / Self References

------------------------------------------------------------------------
class C { self =>
	println(self) // ...C@3b22cdd0
	println(this) // ...C@3b22cdd0
}
------------------------------------------------------------------------

# Explicitly Typed Self References

-> extendsに近い
*/

object TestSelfType {
	
	class Apple (val price:Int)

	def main(args :Array[String]) {
		val a = new Apple(100) { self =>
			val tax = self.price * 0.08
		}
	}
/* scalac -print TestSelfType
...
final class anon$1 extends TestSelfType$Apple { self: <$anon: TestSelfType$Apple> => 
	private[this] val tax: Double = _;
	<stable> <accessor> def tax(): Double = anon$1.this.tax;
	def <init>(): <$anon: TestSelfType$Apple> = {
		anon$1.super.<init>(100);
		anon$1.this.tax = anon$1.this.price().*(0.08);
		()
	}
}
*/
}



