package org.dare2defi.flowwallet.util

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun Int.toByteArray(byteOrder: ByteOrder = ByteOrder.nativeOrder()): ByteArray =
    ByteBuffer
        .allocate(Int.SIZE_BYTES)
        .putInt(this)
        .order(byteOrder)
        .array()