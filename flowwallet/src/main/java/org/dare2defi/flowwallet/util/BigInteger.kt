package org.dare2defi.flowwallet.util

import java.math.BigInteger

fun BigInteger.toByteArrayWithoutLeadingZeroByte(): ByteArray {
    var array = this.toByteArray()
    if (array[0].toInt() == 0) {
        val tmp = ByteArray(array.size - 1)
        System.arraycopy(array, 1, tmp, 0, tmp.size)
        array = tmp
    }
    return array
}