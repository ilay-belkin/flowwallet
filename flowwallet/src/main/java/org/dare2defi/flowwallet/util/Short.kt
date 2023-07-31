package org.dare2defi.flowwallet.util

import java.nio.ByteBuffer

fun Short.toByteArray(): ByteArray = ByteBuffer.allocate(Short.SIZE_BYTES).putShort(this).array()
