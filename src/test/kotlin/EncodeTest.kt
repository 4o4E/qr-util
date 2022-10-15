package top.e404.qr_util.test

import org.jetbrains.skia.ColorMatrix
import org.jetbrains.skia.Image
import org.junit.jupiter.api.Test
import top.e404.qr_util.encode.QrEncoder
import top.e404.qr_util.encode.painter.RectPainter
import top.e404.qr_util.encode.painter.RectPainterData
import top.e404.qr_util.encode.painter.RRectPainter
import top.e404.qr_util.encode.painter.RRectPainterData
import top.e404.qr_util.util.ImageOrInt
import top.e404.skiko.Colors
import top.e404.skiko.util.bytes
import java.io.File

class EncodeTest {
    @Test
    fun testRRectPainter() {
        val text = "https://buhuibaidu.me/?s=niubi666"
        val logo = Image.makeFromEncoded(File("image/logo.png").readBytes())
        val bg = Image.makeFromEncoded(File("image/bg.jpg").readBytes())
        val matrix = QrEncoder.encode(content = text)
        val image = RRectPainter.paint(
            matrix = matrix,
            margin = 3,
            hints = RRectPainterData(
                scale = 30F,
                spacing = 30F,
                color = Colors.BLACK.argb,
                bg = ImageOrInt(bg),
                bgImageColorMatrix = ColorMatrix(
                    1.1F, 0.5F, 0.5F, 0.0F, 0.0F,
                    0.5F, 1.1F, 0.5F, 0.0F, 0.0F,
                    0.5F, 0.5F, 1.1F, 0.0F, 0.0F,
                    0.0F, 0.0F, 0.0F, 1.0F, 0.0F
                ),
                logo = logo,
                logoScale = 5,
                logoRadius = 30F
            )
        )
        File("RRectPainter.png").writeBytes(image.bytes())
    }

    @Test
    fun testRectPainter() {
        val text = "https://buhuibaidu.me/?s=niubi666"
        val logo = Image.makeFromEncoded(File("image/logo.png").readBytes())
        val bg = Image.makeFromEncoded(File("image/bg.jpg").readBytes())
        val matrix = QrEncoder.encode(content = text)
        val image = RectPainter.paint(
            matrix = matrix,
            margin = 3,
            hints = RectPainterData(
                scale = 30F,
                color = Colors.BLACK.argb,
                bg = ImageOrInt(bg),
                bgImageColorMatrix = ColorMatrix(
                    1.1F, 0.5F, 0.5F, 0.0F, 0.0F,
                    0.5F, 1.1F, 0.5F, 0.0F, 0.0F,
                    0.5F, 0.5F, 1.1F, 0.0F, 0.0F,
                    0.0F, 0.0F, 0.0F, 1.0F, 0.0F
                ),
                logo = logo,
                logoScale = 5,
                logoRadius = 30F
            )
        )
        File("RectPainter.png").writeBytes(image.bytes())
    }
}