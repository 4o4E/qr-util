package top.e404.qr_util.decode

import com.google.zxing.LuminanceSource
import java.awt.image.BufferedImage

class BufferedImageLuminanceSource(private val image: BufferedImage) : LuminanceSource(image.width, image.height) {
    //要求是返回一个以行为主的数组
    override fun getMatrix() = ByteArray(width * height).apply {
        image.raster.getDataElements(0, 0, width, height, this)
    }

    //要求是返回图片中某一行的亮度数据
    override fun getRow(y: Int, row: ByteArray?): ByteArray {
        var arr = row ?: ByteArray(width)
        if (arr.size < width) arr = ByteArray(width)
        image.raster.getDataElements(0, 0 + y, width, 1, arr)
        return arr
    }
}