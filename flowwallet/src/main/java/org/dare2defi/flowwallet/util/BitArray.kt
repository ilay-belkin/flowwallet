package org.dare2defi.flowwallet.util

import java.lang.Math.pow
import kotlin.math.min

class BitArray(val size: Int) : Iterable<Boolean> {

    private val data = Array(size) { false }

    constructor(byte: Byte) : this(8) {
        for (i in 0 until data.size) {
            data[7 - i] = ((byte.toInt() shr i) and 1) == 1
        }
    }

    constructor(list: Collection<Boolean>) : this(list.size) {
        for ((i, bit) in list.withIndex()) {
            data[i] = bit
        }
    }

    fun get(position: Int) = data[position]

    fun slice(intRange: IntRange) = BitArray(data.slice(intRange))

    fun toBytes(): ByteArray {
        val result = mutableListOf<Byte>()
        for (i in 0 until data.size step 8) {
            val slice = slice(i until min(data.size, i + 8))
            result.add(slice.toByte())
        }
        return result.toByteArray()
    }

    fun toByte(): Byte {
        val binary = data.map { if (it) "1" else "0" }.joinToString("").padStart(8, '0')
        return binaryToDecimal(binary).toByte()
    }

    private fun binaryToDecimal(binary: String): Int {
        var sum = 0
        for ((i, bit) in binary.reversed().withIndex()) {
            sum += bit.toString().toInt() * pow(2.toDouble(), i.toDouble()).toInt()
        }
        return sum
    }

    override fun iterator() = data.iterator()
}