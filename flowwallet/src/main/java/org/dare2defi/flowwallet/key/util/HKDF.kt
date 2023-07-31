package org.dare2defi.flowwallet.key.util

import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


internal object HKDF {
    fun calculate(password: ByteArray, salt: ByteArray): ByteArray {
        try {
            val mac = Mac.getInstance("HmacSHA256")
            mac.init(SecretKeySpec(salt, "HmacSHA256"))
            return mac.doFinal(password)
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError(e)
        } catch (e: InvalidKeyException) {
            throw AssertionError(e)
        }
    }
}
