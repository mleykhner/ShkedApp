package ru.mleykhner.shkedapp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import ru.mleykhner.shkedapp.data.models.AttachmentViewData
import org.koin.mp.KoinPlatform.getKoin

class FileNetworkService() {
    private val client: HttpClient = getKoin().get()
    private val fileStorage: FileStorage = getKoin().get()
    suspend fun downloadFile(attachmentViewData: AttachmentViewData, progress: (Float) -> Unit = {}): String {
        val response = client.get(attachmentViewData.fileURL) {
            onDownload { bytesSentTotal, contentLength ->
                progress(bytesSentTotal.toFloat() / contentLength.toFloat())
            }
        }
        val fileBytes: ByteArray = response.body()
        val filePath = fileStorage.saveFile(fileBytes, attachmentViewData.extension)
        return filePath
    }
}