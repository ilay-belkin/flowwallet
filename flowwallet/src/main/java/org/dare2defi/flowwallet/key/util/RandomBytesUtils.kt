package org.dare2defi.flowwallet.key.util

import java.security.SecureRandom

internal object RandomBytesUtils {

    fun getRandomBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        SecureRandom().nextBytes(bytes)
        return bytes
    }
}