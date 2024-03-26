package com.kkyoungs.wearablewatch.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

@Composable
fun BarcodeView(
    barcodeValue: String,
    barcodeWidth: Dp,
    barcodeHeight: Dp
) {
    val bitMatrix = MultiFormatWriter().encode(
        barcodeValue,
        BarcodeFormat.CODE_128,
        barcodeWidth.toPx().toInt(),
        barcodeHeight.toPx().toInt()
    )
    val totalBars = bitMatrix.width
    val barHeight = barcodeHeight.toPx()

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val barWidth = size.width / totalBars
        val barcodeWidth = barWidth * totalBars
        val startX = (size.width - barcodeWidth) / 2
        val startY = (size.height - barHeight) / 2
        for (x in 0 until totalBars) {
            val color = if (bitMatrix[x, 0]) Color.Black else Color.White
            drawRect(
                color = color,
                topLeft = Offset(startX + x * barWidth, startY),
                size = Size(barWidth, barHeight)
            )
        }
    }
}
@Composable
fun Dp.toPx(): Float {
    return this.value * LocalDensity.current.density
}