package ru.mleykhner.shkedapp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.mleykhner.shkedapp.data.remote.FileStorage
import ru.mleykhner.shkedapp.data.models.AttachmentViewData
import org.koin.mp.KoinPlatform.getKoin

class FileNetworkService(): KoinComponent {
    private val client: HttpClient by inject()
    private val fileStorage: FileStorage by inject()
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