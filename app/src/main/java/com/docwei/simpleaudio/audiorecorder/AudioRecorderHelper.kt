package com.docwei.simpleaudio.audiorecorder

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import java.io.File
import java.io.FileOutputStream

/**
 * Created by liwk on 2021/3/21.
 */
object AudioRecorderHelper {
    var mAudioRecorder: AudioRecord? = null;
    var thread: Thread? = null
    var mBufferByte: ByteArray? = null
    var file: File? = null

    @Volatile
    var isRecording: Boolean = false
    fun startRecord(fl: File) {
        file = fl
        if (isRecording) {
            return
        }
        val minBufferSize = AudioRecord.getMinBufferSize(44100,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        if (minBufferSize == AudioRecord.ERROR_BAD_VALUE) {
            Log.e("simpleAudio", "invalid params")
            return;
        }
        mBufferByte = ByteArray(minBufferSize)
        mAudioRecorder = AudioRecord(MediaRecorder.AudioSource.MIC,
                44100, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, minBufferSize)
        if (mAudioRecorder?.state == AudioRecord.STATE_UNINITIALIZED) {
            Log.e("simpleAudio", "init fail")
            return
        }
        mAudioRecorder?.startRecording()
        thread = Thread(AudioRunnable())
        thread?.start()
        isRecording = true
    }

    fun stopRecord() {
        if (!isRecording) {
            return
        }
        isRecording = false
        mAudioRecorder?.stop()
        mAudioRecorder?.release()
        Log.e("simpleAudio", "stop AudioRecorder success")
    }

    class AudioRunnable : Runnable {
        override fun run() {
            val fos = FileOutputStream(file)
            fos.use {
                while (isRecording) {
                    val ret = mAudioRecorder?.read(mBufferByte!!, 0, mBufferByte!!.size)
                    Log.e("simpleAudio","ret-->${ret}")
                    if (ret == AudioRecord.ERROR_INVALID_OPERATION) {
                        Log.e("simpleAudio", "Error invalid operation")
                    } else if (ret == AudioRecord.ERROR_BAD_VALUE) {
                        Log.e("simpleAudio", "Error bad value")
                    } else {
                        fos.write(mBufferByte)
                    }
                }

            }
        }

    }

}