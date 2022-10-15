package top.e404.qr_util.util

import com.google.zxing.common.BitMatrix

fun BitMatrix.forEach(
    block: (bit: Boolean, x: Int, y: Int) -> Unit
) {
    for (x in 0 until width) for (y in 0 until height) {
        block(get(x, y), x, y)
    }
}