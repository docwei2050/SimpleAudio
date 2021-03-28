package com.docwei.simpleaudio.audiorecorder

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.AudioTrack
import android.util.Log
import java.io.File
import java.io.FileInputStream

/**
 * Created by liwk on 2021/3/21.
 */
class AudioTrackPlayer {
    var audioTrack:AudioTrack?=null
    var file: File?=null
    var minBufferSize:Int=0
    var thread:Thread?=null
    @Volatile
    var isPlaying:Boolean=false
    fun play(fl: File){
        file=fl
        if(isPlaying){
            return
        }
        minBufferSize =AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        if(minBufferSize== AudioRecord.ERROR_BAD_VALUE){
            Log.e("simpleAudio", "invalid params")
            return;
        }
        audioTrack=AudioTrack(AudioManager.STREAM_MUSIC,
                44100, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM)
        if (audioTrack?.state == AudioTrack.STATE_UNINITIALIZED) {
            Log.e("simpleAudio", "AudioTrack initialize fail")
            return
        }
        isPlaying=true
        thread= Thread(Runnable {
            kotlin.run {
                val fis=FileInputStream(file)
                fis.use {
                    val tempBuffer=ByteArray(minBufferSize)
                    while(it.available()>0){
                        val ret=it.read(tempBuffer)
                        if(ret==AudioTrack.ERROR_BAD_VALUE||ret==AudioTrack.ERROR_INVALID_OPERATION){
                            continue
                        }
                        if(ret!=0&&ret!=-1&&isPlaying){
                            audioTrack?.write(tempBuffer,0,ret)
                            audioTrack?.play()
                        }
                    }

                }
            }
        })
        thread?.start()
    }

    fun stopPlay(){
        if(!isPlaying){
            return;
        }
        if(audioTrack?.state==AudioTrack.PLAYSTATE_PLAYING){
            audioTrack?.stop()
        }

        audioTrack?.release()
        isPlaying=false
    }

}


