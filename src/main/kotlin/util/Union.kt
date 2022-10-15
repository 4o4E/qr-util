package top.e404.qr_util.util

import org.jetbrains.skia.Image

class ImageOrInt(private val t: Any) {
    init {
        if (t !is Int && t !is Image) throw ImageOrIntUnionException(t)
    }

    val image = t is Image
    val int = t is Int

    val asImage: Image
        get() = if (image) t as Image
        else throw TypeException("value(type: ${t::class.java.name}) is not org.jetbrains.skia.Image")

    val asInt: Int
        get() = if (int) t as Int
        else throw TypeException("value(type: ${t::class.java.name}) is not kotlin.Int")
}

class TypeException(message: String) : Exception(message)

class ImageOrIntUnionException(value: Any) : Exception(
    "value(type: ${value::class.java.name}) is not org.jetbrains.skia.Image or kotlin.Int"
)