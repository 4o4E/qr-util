package top.e404.qr_util.encode.painter

import org.jetbrains.skia.ColorMatrix
import org.jetbrains.skia.Image
import top.e404.qr_util.encode.QrPainterHints
import top.e404.qr_util.util.ImageOrInt
import top.e404.skiko.Colors

data class RRectPainterData(
    var scale: Float = 30F,
    var spacing: Float = scale / 20F,
    var radius: Float = 8F,
    var color: Int = Colors.BLACK.argb,
    var bg: ImageOrInt = ImageOrInt(Colors.WHITE.argb),
    var bgImageColorMatrix: ColorMatrix? = null,
    var logo: Image? = null,
    var logoScale: Int = 5,
    var logoRadius: Float = 8F
) : QrPainterHints