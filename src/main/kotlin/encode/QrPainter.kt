package top.e404.qr_util.encode

import com.google.zxing.common.BitMatrix
import org.jetbrains.skia.Image

/**
 * 用于将二维码的BitMatrix绘制成图片
 */
interface QrPainter<T : QrPainterHints> {
    /**
     * 绘制二维码
     *
     * @param matrix 二维码的BitMatrix
     * @param hints 绘制参数
     * @return 绘制完成的图片
     */
    fun paint(matrix: BitMatrix, margin: Int, hints: T): Image
}

interface QrPainterHints