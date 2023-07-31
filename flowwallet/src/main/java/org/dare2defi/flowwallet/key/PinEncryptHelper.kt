package org.dare2defi.flowwallet.key

import android.content.Context
import org.dare2defi.flowwallet.key.storage.EncryptStorage
import org.dare2defi.flowwallet.util.md5
import org.dare2defi.flowwallet.util.sha512
import org.dare2defi.flowwallet.key.util.AES
import org.dare2defi.flowwallet.key.util.HKDF
import org.dare2defi.flowwallet.key.util.RandomBytesUtils

class PinEncryptHelper(context: Context, pinString: String, encryptStorage: EncryptStorage) :
    BaseEncryptHelper(context, KeyType.PIN, encryptStorage) {

    private var pin = pinString.toByteArray()

    private var wasAccessKeyGenerated = false

    init {
        if (!encryptStorage.isAccessKeyExists(KeyType.PIN)) {
            createAccessAndMasterKeys()
        }
    }

    fun resetPin(newPin: String): Boolean =
        if (checkPin()) {
            encryptStorage.setSignature(signPin(newPin.toByteArray()), KeyType.PIN)
            pin = newPin.toByteArray()
            true
        } else {
            false
        }

    fun checkPin(pinCandidate: ByteArray): Boolean {
        val signature = encryptStorage.getSignature(KeyType.PIN)
        return signature != null && signPin(pinCandidate).contentEquals(signature)

    }

    fun checkPin(): Boolean = checkPin(pin)

    override fun retrieveAccessKey(): ByteArray {
        if (!wasAccessKeyGenerated && !checkPin()) {
            throw IllegalStateException("You have to check PIN first")
        }
        return super.retrieveAccessKey()
    }

    private fun signPin() = signPin(pin)

    private fun signPin(pin: ByteArray) = pin.sha512().md5()

    private fun createAccessAndMasterKeys() {
        val accessKey = generateAccessKey(pin)
        val masterKey = generateMasterKey()
        val encryptedAccessKey = keystoreHelper.encrypt(accessKey)
        val encryptedMasterKey = AES.encrypt(masterKey, accessKey)
        encryptStorage.setAccessKey(encryptedAccessKey, KeyType.PIN)
        encryptStorage.setMasterKey(encryptedMasterKey, KeyType.PIN)
        encryptStorage.setSignature(signPin(), KeyType.PIN)
    }

    private fun generateMasterKey(): ByteArray {
        val masterPassword = RandomBytesUtils.getRandomBytes(64)
        val masterSalt = RandomBytesUtils.getRandomBytes(64)
        return HKDF.calculate(masterPassword, masterSalt)
    }

    override fun generateAccessKey(salt: ByteArray): ByteArray {
        wasAccessKeyGenerated = true
        return super.generateAccessKey(salt)
    }
}
