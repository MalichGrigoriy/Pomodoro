package com.timemanager.pomodorofocus.ui.timer.pixelView

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle

fun drawOvalOnBitmap( height: Int, width: Int,): ImageBitmap {

    val androidBitmap = ImageBitmap(width = width, height = height)
    val canvas = Canvas(androidBitmap)
    val roundedX = (width.toFloat() / height.toFloat())

    val paint = Paint()
    paint.color = Color.Black
    paint.style = PaintingStyle.Fill

    canvas.drawRoundRect(
        left = 0f,
        top = 0f,
        right = width.toFloat(),
        bottom = height.toFloat(),
        radiusX = width.toFloat()/roundedX,
        radiusY = height.toFloat(),
        paint = paint
    )

    return androidBitmap
}
