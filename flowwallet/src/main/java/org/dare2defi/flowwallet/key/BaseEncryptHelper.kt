package org.dare2defi.flowwallet.key

import android.content.Context
import org.dare2defi.flowwallet.key.storage.EncryptStorage
import org.dare2defi.flowwallet.key.util.AES
import org.dare2defi.flowwallet.key.util.HKDF
import org.dare2defi.flowwallet.key.util.KeystoreHelper
import org.dare2defi.flowwallet.key.util.RandomBytesUtils

abstract class BaseEncryptHelper(context: Context, private val keyType: KeyType, val encryptStorage: EncryptStorage) {

    protected val keystoreHelper = KeystoreHelper(context, keyType, encryptStorage)
    internal var accessKey: ByteArray = ByteArray(0)
        get() {
            if (field.isEmpty()) {
                field = retrieveAccessKey()
            }
            return field
        }

    protected open fun retrieveAccessKey(): ByteArray {
        val encryptedAccessKey = encryptStorage.getAccessKey(keyType)
        return keystoreHelper.decrypt(encryptedAccessKey)
    }

    fun encrypt(decryptedData: String): ByteArray {
        return AES.encrypt(decryptedData.toByteArray(), retrieveMasterKey(accessKey))
    }

    fun decrypt(encryptedData: ByteArray) = String(AES.decrypt(encryptedData, retrieveMasterKey(accessKey)))

    internal fun retrieveMasterKey(accessKey: ByteArray): ByteArray {
        val encryptedMasterKey = encryptStorage.getMasterKey(keyType)
        return AES.decrypt(encryptedMasterKey, accessKey)
    }

    internal open fun generateAccessKey(salt: ByteArray): ByteArray {
        accessKey = HKDF.calculate(RandomBytesUtils.getRandomBytes(64), salt)
        return accessKey
    }

    fun destroy() {
        accessKey = ByteArray(0)
    }
}
