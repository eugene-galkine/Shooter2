package eg.server.net

import java.net.ServerSocket

class TCPServer(port: Int): Thread() {
    private val serverSocket = ServerSocket(port)

    init {
        start()
    }

    fun getLocalPort(): Int {
        return serverSocket.localPort
    }

    override fun run() {
        while (true) {
            try {
                val connectionSocket = serverSocket.accept()
                connectionSocket.tcpNoDelay = true
                TCPConnection(connectionSocket)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}