package top.e404.qr_util.encode

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QrEncoder {
    private val writer = MultiFormatWriter()
    private val encodeHints = mapOf<EncodeHintType, Any>(
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H,
        EncodeHintType.CHARACTER_SET to Charsets.UTF_8,
        EncodeHintType.MARGIN to 0,
    )

    /**
     * 生成二维码BitMatrix
     *
     * @param content 正文
     * @return 二维码图片
     */
    fun encode(content: String) = writer.encode(content, BarcodeFormat.QR_CODE, 0, 0, encodeHints)!!
}