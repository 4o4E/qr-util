package top.e404.qr_util.test

import org.junit.jupiter.api.Test
import top.e404.qr_util.decode.QrDecoder.decode
import java.io.File
import javax.imageio.ImageIO

class DecodeTest {
    @Test
    fun t() {
        println(decode(ImageIO.read(File("out.png")), 0, 10))
        //println(decode(ImageIO.read(File("image/2.jpg")), 0xB584A2, 80))
        //println(decode(ImageIO.read(File("image/3.png"))))
    }
}