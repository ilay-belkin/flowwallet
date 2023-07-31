package org.dare2defi.flowwallet.key.util

fun ByteArray.toHex(): String {
    val sb = StringBuilder(size * 2)
    for (i in indices) {
        sb.append(String.format("%02x", get(i)))
    }
    return sb.toString()

}
