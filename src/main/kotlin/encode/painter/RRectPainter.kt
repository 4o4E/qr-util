package top.e404.qr_util.encode.painter

import com.google.zxing.common.BitMatrix
import org.jetbrains.skia.*
import top.e404.qr_util.encode.QrPainter
import top.e404.qr_util.util.forEach
import top.e404.skiko.util.drawImageRRect
import top.e404.skiko.util.subCenter
import top.e404.skiko.util.withCanvas

object RRectPainter : QrPainter<RRectPainterData> {
    override fun paint(matrix: BitMatrix, margin: Int, hints: RRectPainterData): Image {
        val (scale, radius, c, bg, bgMatrix, logo, logoScale, logoRadius) = hints
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
            val size = scale * .85f
            val s = scale * .075f
            val ss = scale * .15f
            val rect = Rect.makeWH(surface.width.toFloat(), surface.height.toFloat())
            if (bg.int) drawRect(rect, bgPaint!!)
            else drawImageRect(bgImg!!.subCenter(), rect, Paint().apply {
                bgMatrix?.let { colorFilter = ColorFilter.makeMatrix(bgMatrix) }
            })
            /**
             * 绘制定位点
             */
            fun fix() {
                listOf(
                    3 to 3,
                    3 to matrix.height - 4,
                    matrix.width - 4 to 3,
                ).forEach { (x, y) ->
                    // 中心
                    drawRRect(
                        r = RRect.makeXYWH(
                            l = (x - 1) * scale + mm + s,
                            t = (y - 1) * scale + mm + s,
                            w = scale * 3 - ss,
                            h = scale * 3 - ss,
                            radius = radius
                        ),
                        paint = paint
                    )
                    // 外圈
                    drawDRRect(
                        outer = RRect.makeXYWH(
                            l = (x - 3) * scale + mm + s,
                            t = (y - 3) * scale + mm + s,
                            w = scale * 7 - ss,
                            h = scale * 7 - ss,
                            radius = radius * 3
                        ),
                        inner = RRect.makeXYWH(
                            l = (x - 2) * scale + mm - s,
                            t = (y - 2) * scale + mm - s,
                            w = scale * 5 + ss,
                            h = scale * 5 + ss,
                            radius = radius
                        ),
                        paint = paint
                    )
                }
            }
            // 需要绘制logo
            if (logo != null) {
                val left = (matrix.width - logoScale) / 2
                val wRange = left..matrix.width - left
                val top = (matrix.height - logoScale) / 2
                val hRange = top..matrix.width - top
                matrix.forEach { bit, x, y ->
                    if ((x in wRange && y in hRange)
                        || !bit
                        || (x <= 6 && y <= 6)
                        || (x >= matrix.width - 7 && y <= 6)
                        || (x <= 6 && y >= matrix.height - 7)
                    ) return@forEach
                    // bit点
                    drawRRect(
                        r = RRect.makeXYWH(
                            l = x * scale + mm + s,
                            t = y * scale + mm + s,
                            w = size,
                            h = size,
                            radius = radius
                        ),
                        paint = paint
                    )
                }
                fix()
                // logo
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
                if (!bit
                    || (x <= 6 && y <= 6)
                    || (x >= matrix.width - 7 && y <= 6)
                    || (x <= 6 && y >= matrix.height - 7)
                ) return@forEach
                // bit点
                drawRRect(
                    r = RRect.makeXYWH(
                        l = x * scale + mm + s,
                        t = y * scale + mm + s,
                        w = size,
                        h = size,
                        radius = radius
                    ),
                    paint = paint
                )
            }
            // 绘制定位点
            fix()
        }
        return surface.makeImageSnapshot()
    }
}

