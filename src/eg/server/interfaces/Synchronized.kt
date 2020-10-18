package eg.server.interfaces

import java.io.Serializable

interface Synchronized: Serializable {
    fun download(data: ByteArray)
    fun upload(): ByteArray
}