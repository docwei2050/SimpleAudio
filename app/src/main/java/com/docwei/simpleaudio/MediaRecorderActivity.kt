package com.docwei.simpleaudio

import android.app.Activity
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment.DIRECTORY_MUSIC
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.FileInputStream

/**
 * Created by liwk on 2021/3/21.
 */
class MediaRecorderActivity : Activity() {
    var recorder: MediaRecorder? = null
    var recorderFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)
    }

    fun startRecord(view: View) {
        if (recorder == null) {
            recorder = MediaRecorder()
        }
        recorder?.reset()
        recorder?.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        recorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

        recorderFile = File(getExternalFilesDir(DIRECTORY_MUSIC)?.absolutePath + "/recorder/" + System.currentTimeMillis() + ".m4a")
        if(recorderFile?.parentFile?.exists()==false){
            recorderFile?.parentFile?.mkdir()
        }
        recorderFile?.createNewFile()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            recorder?.setOutputFile(recorderFile)
        } else {
            recorder?.setOutputFile(recorderFile?.absolutePath)
        }

        recorder?.prepare()
        recorder?.start()
        Toast.makeText(this,"开始录制",Toast.LENGTH_SHORT).show()

    }

    fun stopRecord(view: View) {
        recorder?.stop()
        Toast.makeText(this,"停止录制",Toast.LENGTH_SHORT).show()
    }

    fun play(view: View) {
        val mediaPlayer = MediaPlayer();
        val fis = FileInputStream(recorderFile)
        mediaPlayer.setDataSource(fis.fd)
        mediaPlayer.setVolume(1f, 1f);
        mediaPlayer.isLooping = false;
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        recorder?.release()
    }

}