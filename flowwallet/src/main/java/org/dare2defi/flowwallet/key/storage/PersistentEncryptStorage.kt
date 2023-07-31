package org.dare2defi.flowwallet.key.storage

import android.annotation.SuppressLint
import android.content.SharedPreferences
import org.dare2defi.flowwallet.key.KeyType
import org.dare2defi.flowwallet.util.hexToByteArray
import org.dare2defi.flowwallet.util.toHexString
import java.security.SecureRandom


@SuppressLint("ApplySharedPref")
class PersistentEncryptStorage(
    private val prefs: SharedPreferences
) : EncryptStorage {

    private fun keystoreIv(keyType: KeyType) = "${keyType.name}_KEYSTORE_IV"
    private fun accessKey(keyType: KeyType) = "${keyType.name}_ACCESS_KEY"
    private fun masterKey(keyType: KeyType) = "${keyType.name}_MASTER_KEY"
    private fun signature(keyType: KeyType) = "${keyType.name}_SIGNATURE"

    private val biometrySalt = "BIOMETRY_SALT"
    private val biometryEnabled = "BIOMETRY_ENABLED"


    override fun setKeystoreIv(keyType: KeyType, iv: ByteArray) {
        prefs.edit().putString(keystoreIv(keyType), iv.toHexString()).commit()
    }

    override fun getKeystoreIv(keyType: KeyType) =
        prefs.getString(keystoreIv(keyType), null)!!.hexToByteArray()

    override fun getAccessKey(keyType: KeyType) =
        prefs.getString(accessKey(keyType), null)!!.hexToByteArray()

    override fun getMasterKey(keyType: KeyType) =
        prefs.getString(masterKey(keyType), null)!!.hexToByteArray()

    override fun isAccessKeyExists(keyType: KeyType) = prefs.contains(accessKey(keyType))

    override fun getSignature(keyType: KeyType) = prefs.getString(signature(keyType), null)!!.hexToByteArray()

    override fun setAccessKey(key: ByteArray, keyType: KeyType) {
        prefs.edit().putString(accessKey(keyType), key.toHexString()).commit()
    }

    override fun setMasterKey(key: ByteArray, keyType: KeyType) {
        prefs.edit().putString(masterKey(keyType), key.toHexString()).commit()
    }

    override fun setSignature(key: ByteArray, keyType: KeyType) {
        prefs.edit().putString(signature(keyType), key.toHexString()).commit()
    }

    override fun getBiometrySalt(): ByteArray {
        val saltHex = prefs.getString(biometrySalt, null)
        saltHex?.let {
            return it.hexToByteArray()
        }
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        prefs.edit().putString(biometrySalt, bytes.toHexString()).commit()
        return bytes
    }

    override fun removeBiometrySalt() {
        prefs.edit().remove(biometrySalt).commit()
    }

    override fun removeAccessKey(keyType: KeyType) {
        prefs.edit().remove(accessKey(keyType)).commit()
    }

    override fun removeMasterKey(keyType: KeyType) {
        prefs.edit().remove(masterKey(keyType)).commit()
    }

    override fun setBiometryEnabled(isEnable: Boolean) {
        prefs.edit().putBoolean(biometryEnabled, isEnable).commit()
    }

    override fun isBiometryEnabled() = prefs.getBoolean(biometryEnabled, false)

}
