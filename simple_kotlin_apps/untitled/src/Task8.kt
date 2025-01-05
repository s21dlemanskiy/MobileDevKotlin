
// Реализуйте класс комплексного числа с реализацией операций +,-,*,/
//Complex(4.0 - 3.0i)
//Complex(-2.0 + 7.0i)
//Complex(13.0 + 1.0i)
//Complex(-0.20588236 + 0.32352942i)
class Task8 {
    class Complex(
        private val x: Float,
        private val y: Float
    ) {
//        fun toGeom() {
//
//        }
        operator fun plus(b: Complex): Complex {
            return Complex(x + b.x, y + b.y)
        }
        operator fun minus(b: Complex): Complex {
            return Complex(x - b.x, y - b.y)
        }
        operator fun times(b: Complex): Complex {
            return Complex(x * b.x - y * b.y, x * b.y + y * b.x)
        }
        operator fun div(b: Complex): Complex {
            //(x1 + iy1) / (x2 + iy2) =
            // (x1 + iy1) / (x2 + iy2) *  (x2 - iy2) / (x2 - iy2) =
            // (x1 + iy1) * (x2 - iy2) / (x2**2 + y2**2) =
            // (x1 * x2 + y2y1 + i(y1x2 - y2x1)) / (x2**2 + y2**2)
            val square = b.x * b.x + b.y * b.y
            return Complex((x * b.x + y * b.y) / square, (y * b.x - b.y * x) / square)
        }

        override fun toString(): String {
            return "Complex($x ${if (y > 0) '+'  else '-'} ${kotlin.math.abs(y)}i)"
        }

    }

    fun task8() {
        val a = Complex(1f, 2f)
        val b = Complex(3f, -5f)
        println(a + b)
        println(a - b)
        println(a * b)
        println(a / b)

    }

}