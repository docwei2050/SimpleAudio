package com.docwei.simpleaudio.audiorecorder

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import com.docwei.simpleaudio.R
import java.io.File

/**
 * Created by liwk on 2021/3/21.
 */
class AudioRecorderActivity : Activity() {
    var btnRecord: Button? = null
    var path: String = ""
    var audioTrackPlayer: AudioTrackPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
        btnRecord = findViewById<Button>(R.id.btn_record)
        path = getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath + "/audiorecord/" + System.currentTimeMillis() + ".pcm"
    }

    fun record(view: View) {
        if (!btnRecord!!.isSelected) {
            AudioRecorderHelper.startRecord(createFile(path)!!)
        } else {
            AudioRecorderHelper.stopRecord()
        }

        btnRecord!!.isSelected = !btnRecord!!.isSelected
        btnRecord!!.text = if (btnRecord!!.isSelected) "停止录制" else "开始录制"

    }

    fun audioTrackPlay(view: View) {
        if (!view.isSelected) {
            audioTrackPlayer = AudioTrackPlayer()
            val file = File(path)
            audioTrackPlayer?.play(file)
        } else {
            audioTrackPlayer?.stopPlay()
        }
        view.isSelected = !view.isSelected
        (view as Button)!!.text = if (view.isSelected) "停止播放" else "开始播放"

    }

    fun changeFormatWav(view: View) {

    }

    fun playWav(view: View) {

    }

    private fun createFile(path: String): File? {
        val tempFile = File(path)
        if (!tempFile.exists()) {
            tempFile.createNewFile()
        } else {
            tempFile.delete()
        }
        return tempFile
    }


}