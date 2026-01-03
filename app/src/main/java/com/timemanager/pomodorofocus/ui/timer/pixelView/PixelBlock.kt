package com.timemanager.pomodorofocus.ui.timer.pixelView

data class PixelBlock(
    val width: Int,
    val height: Int,
    val pixels: BooleanArray
) {
    fun get(x: Int, y: Int): Boolean = pixels[y * width + x]
    fun set(x: Int, y: Int, value: Boolean) {
        pixels[y * width + x] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PixelBlock

        if (width != other.width) return false
        if (height != other.height) return false
        if (!pixels.contentEquals(other.pixels)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + pixels.contentHashCode()
        return result
    }
}

fun Char.toPixelBlock(): PixelBlock {
    return when (this) {
        ':' -> PixelBlock(
            1, 5,
            booleanArrayOf(false, true, false, true, false)
        )

        ' ' -> PixelBlock(
            1, 5,
            booleanArrayOf(false, false, false, false, false)
        )

        /*  # # #
            # - #
            # - #
            # - #
            # # #   */

        '0' -> PixelBlock(
            3, 5,
            booleanArrayOf(
                true, true, true,
                true, false, true,
                true, false, true,
                true, false, true,
                true, true, true,
            )
        )

        /*   - # -
             # # -
             - # -
             - # -
             # # #   */

        '1' -> PixelBlock(
            3, 5,
            booleanArrayOf(
                false, true, false,
                true, true, false,
                false, true, false,
                false, true, false,
                true, true, true
            )
        )

        /*   # # #
             - - #
             - # -
             # - -
             # # #   */

        '2' -> PixelBlock(
            3, 5,
            booleanArrayOf(
                true, true, true,
                false, false, true,
                false, true, false,
                true, false, false,
                true, true, true
            )
        )

        /*   # # #
             - - #
             - # #
             - - #
             # # #   */

        '3' -> PixelBlock(
            3, 5,
            booleanArrayOf(
                true, true, true,
                false, false, true,
                false, true, true,
                false, false, true,
                true, true, true
            )
        )

        /*   # - #
             # - #
             # # #
             - - #
             - - #   */

        '4' -> PixelBlock(
            3, 5,
            booleanArrayOf(
                true, false, true,
                true, false, true,
                true, true, true,
                false, false, true,
                false, false, true
            )
        )

        /*   # # #
             # - -
             # # #
             - - #
             # # #   */

        '5' -> PixelBlock(
            3, 5,
            booleanArrayOf(
                true, true, true,
                true, false, false,
                true, true, true,
                false, false, true,
                true, true, true
            )
        )

        /*   # # #
             # -
             # # #
             # - #
             # # #   */

        '6' -> PixelBlock(
            3, 5,
            booleanArrayOf(
                true, true, true,
                true, false, false,
                true, true, true,
                true, false, true,
                true, true, true
            )
        )

        /*   # # #
             - - #
             - - #
             - - #
             - - #   */

        '7' -> PixelBlock(
            3, 5,
            booleanArrayOf(
                true, true, true,
                false, false, true,
                false, false, true,
                false, false, true,
                false, false, true,
            )
        )

        /*   # # #
             # - #
             # # #
             # - #
             # # #   */

        '8' -> PixelBlock(
            3, 5,
            booleanArrayOf(
                true, true, true,
                true, false, true,
                true, true, true,
                true, false, true,
                true, true, true
            )
        )

        /*   # # #
             # - #
             # # #
             - - #
             # # #   */

        '9' -> PixelBlock(
            3, 5,
            booleanArrayOf(
                true, true, true,
                true, false, true,
                true, true, true,
                false, false, true,
                true, true, true
            )
        )

        else -> PixelBlock(
            3, 5,
            booleanArrayOf(
                true, true, true,
                true, true, true,
                true, true, true,
                true, true, true,
                true, true, true
            )
        )
    }
}
