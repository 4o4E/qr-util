package top.e404.qr_util.decode

import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs

object QrDecoder {
    private val hints = mutableMapOf<DecodeHintType, Any>(
        // 解码设置编码方式为 utf-8，
        DecodeHintType.CHARACTER_SET to Charsets.UTF_8,
        // 优化精度
        DecodeHintType.TRY_HARDER to true,
        // 复杂模式，开启PURE_BARCODE模式
        //DecodeHintType.PURE_BARCODE to true,
    )
    private const val white = -0x1
    private const val black = -0x1000000

    /**
     * 二值化
     *
     * @param c rgb颜色
     * @param range 颜色浮动范围
     */
    fun BufferedImage.binaryzation(
        c: Int? = null,
        range: Int = 30,
    ) = apply {
        var rgb: Int
        if (c != null) {
            val cr = c and 0xff0000 shr 16
            val cg = c and 0xff00 shr 8
            val cb = c and 0xff
            for (x in 0 until width) for (y in 0 until height) {
                rgb = getRGB(x, y)
                val r = rgb and 0xff0000 shr 16
                val g = rgb and 0xff00 shr 8
                val b = rgb and 0xff
                setRGB(x, y, if (abs(cr - r) + abs(cg - g) + abs(cb - b) > range) white else black)
            }
            return@apply
        }
        var min: Int? = null
        var max: Int? = null
        for (x in 0 until width) for (y in 0 until height) {
            rgb = getRGB(x, y)
            if (min == null || rgb < min) min = rgb
            else if (max == null || rgb > max) max = rgb
        }
        val split = (min!! + max!!) / 2
        for (x in 0 until width) for (y in 0 until height) {
            setRGB(x, y, if (getRGB(x, y) > split) white else black)
        }
    }

    /**
     * 转为灰度图
     */
    fun BufferedImage.gray() = BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY).also {
        it.graphics.drawImage(this, 0, 0, null)
    }

    /**
     * 解码
     *
     * @param input 图片输入
     * @param c 指定二维码颜色
     * @param range 颜色模糊范围
     * @return 若未找到则返回null
     */
    fun decode(
        input: BufferedImage,
        c: Int? = null,
        range: Int = 80,
    ): String? {
        var image = input
        // 缩放
        val scale = 500.0 / image.width
        val w = (image.width * scale).toInt()
        val h = (image.height * scale).toInt()
        image = BufferedImage(w, h, image.type).also {
            it.graphics.drawImage(image, 0, 0, w, h, null)
        }
        val source = BufferedImageLuminanceSource(image.binaryzation(c, range).gray().also {
            ImageIO.write(it, "png", File("t.png"))
        })
        return try {
            QRCodeReader().decode(BinaryBitmap(HybridBinarizer(source)), hints).text
        } catch (e: Exception) {
            return null
        }
    }
}
