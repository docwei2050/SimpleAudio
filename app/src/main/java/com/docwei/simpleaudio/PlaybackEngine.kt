package com.docwei.simpleaudio

import android.content.Context
import android.media.AudioManager
import android.os.Build

object PlaybackEngine {
    var mEngineHandle: Long = 0
    fun create(context: Context): Boolean {
        if (mEngineHandle == 0L) {
            setDefaultStreamValues(context)
            mEngineHandle = native_createEngine()
        }
        return mEngineHandle != 0L
    }

    private fun setDefaultStreamValues(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val myAudioMgr = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val sampleRateStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
            val defaultSampleRate = sampleRateStr.toInt()
            val framesPerBurstStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
            val defaultFramesPerBurst = framesPerBurstStr.toInt()
            native_setDefaultStreamValues(defaultSampleRate, defaultFramesPerBurst)
        }
    }

    fun delete() {
        if (mEngineHandle != 0L) {
            native_deleteEngine(mEngineHandle)
        }
        mEngineHandle = 0
    }

    fun setToneOn(isToneOn: Boolean) {
        if (mEngineHandle != 0L) native_setToneOn(mEngineHandle, isToneOn)
    }

    fun setAudioApi(audioApi: Int) {
        if (mEngineHandle != 0L) native_setAudioApi(mEngineHandle, audioApi)
    }

    fun setAudioDeviceId(deviceId: Int) {
        if (mEngineHandle != 0L) native_setAudioDeviceId(mEngineHandle, deviceId)
    }

    fun setChannelCount(channelCount: Int) {
        if (mEngineHandle != 0L) native_setChannelCount(mEngineHandle, channelCount)
    }

    fun setBufferSizeInBursts(bufferSizeInBursts: Int) {
        if (mEngineHandle != 0L) native_setBufferSizeInBursts(mEngineHandle, bufferSizeInBursts)
    }

    val currentOutputLatencyMillis:Double
        get() = if (mEngineHandle == 0L) 0.0 else native_getCurrentOutputLatencyMillis(mEngineHandle)
    val isLatencyDetectionSupported: Boolean
        get() = mEngineHandle != 0L && native_isLatencyDetectionSupported(mEngineHandle)

    // Native methods
    private external fun native_createEngine(): Long
    private external fun native_deleteEngine(engineHandle: Long)
    private external fun native_setToneOn(engineHandle: Long, isToneOn: Boolean)
    private external fun native_setAudioApi(engineHandle: Long, audioApi: Int)
    private external fun native_setAudioDeviceId(engineHandle: Long, deviceId: Int)
    private external fun native_setChannelCount(mEngineHandle: Long, channelCount: Int)
    private external fun native_setBufferSizeInBursts(engineHandle: Long, bufferSizeInBursts: Int)
    private external fun native_getCurrentOutputLatencyMillis(engineHandle: Long): Double
    private external fun native_isLatencyDetectionSupported(engineHandle: Long): Boolean
    private external fun native_setDefaultStreamValues(sampleRate: Int, framesPerBurst: Int)

    // Load native library
    init {
        System.loadLibrary("hello-oboe")
    }
}