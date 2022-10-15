package top.e404.qr_util.encode.painter

import com.google.zxing.common.BitMatrix
import org.jetbrains.skia.*
import top.e404.qr_util.encode.QrPainter
import top.e404.qr_util.util.forEach
import top.e404.skiko.util.drawImageRRect
import top.e404.skiko.util.subCenter
import top.e404.skiko.util.withCanvas

object RectPainter : QrPainter<RectPainterData> {
    override fun paint(matrix: BitMatrix, margin: Int, hints: RectPainterData): Image {
        val (scale, c, bg, bgMatrix, logo, logoScale, logoRadius) = hints
        val m = (margin * scale).toInt()
        val mm = (margin * scale / 2).toInt()
        val surface = Surface.makeRasterN32Premul(
            (matrix.width * scale + m).toInt(),
            (matrix.height * scale + m).toInt()
        )
        val paint = Paint().apply { color = c }
        val bgPaint = if (bg.int) Paint().apply { color = bg.asInt } else null
        val bgImg = if (bg.image) bg.asImage else null
        surface.withCanvas {
            val rect = Rect.makeWH(surface.width.toFloat(), surface.height.toFloat())
            if (bg.int) drawRect(rect, bgPaint!!)
            else drawImageRect(bgImg!!.subCenter(), rect, Paint().apply {
                bgMatrix?.let { colorFilter = ColorFilter.makeMatrix(bgMatrix) }
            })
            // 需要绘制logo
            if (logo != null) {
                val left = (matrix.width - logoScale) / 2
                val wRange = left..matrix.width - left
                val top = (matrix.height - logoScale) / 2
                val hRange = top..matrix.width - top
                matrix.forEach { bit, x, y ->
                    if ((x in wRange && y in hRange)
                        || !bit
                    ) return@forEach
                    drawRect(
                        r = Rect.makeXYWH(
                            l = x * scale + mm,
                            t = y * scale + mm,
                            w = scale,
                            h = scale,
                        ),
                        paint = paint
                    )
                }
                drawImageRRect(
                    image = logo.subCenter(),
                    rRect = RRect.makeXYWH(
                        l = (left + .5F) * scale + mm + 1,
                        t = (top + .5F) * scale + mm + 1,
                        w = logoScale * scale,
                        h = logoScale * scale,
                        radius = logoRadius
                    )
                )
                return@withCanvas
            }
            matrix.forEach { bit, x, y ->
                if (bit) drawRect(
                    r = Rect.makeXYWH(
                        l = x * scale + mm,
                        t = y * scale + mm,
                        w = scale,
                        h = scale,
                    ),
                    paint = paint
                )
            }
        }
        return surface.makeImageSnapshot()
    }
}

