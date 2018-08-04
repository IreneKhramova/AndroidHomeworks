package com.example.irene.khramovahomework9

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

class DownloadProgressResponseBody(var responseBody: ResponseBody, var progressListener: DownloadProgressListener?) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun source(): BufferedSource? {
        bufferedSource = bufferedSource ?: Okio.buffer(source(responseBody.source()))
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0

                progressListener?.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1L)
                return bytesRead
            }
        }
    }
}