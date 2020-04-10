package jp.techacademy.jouchan.wan.autoslideshowapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.os.Handler
import android.Manifest
import android.content.pm.PackageManager
import android.content.ContentUris
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity(){

    private val PERMISSIONS_REQUEST_CODE = 100

    private var mTimer: Timer? = null
    private var mTimerSec = 0.0
    private var mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getContentsInfo()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }else {
            getContentsInfo()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }

    private fun getContentsInfo() {
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        button1.setOnClickListener{
            if(cursor!!.isLast){
                cursor.moveToFirst()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imageView.setImageURI(imageUri)
            }else{
                cursor.moveToNext()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imageView.setImageURI(imageUri)
            }
        }

        button2.setOnClickListener{
            if(cursor!!.isFirst){
                cursor.moveToLast()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imageView.setImageURI(imageUri)
            }else{
                cursor.moveToPrevious()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imageView.setImageURI(imageUri)
            }
        }

        button3.setOnClickListener{
            if (mTimer == null){
                mTimer = Timer()
                mTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        mTimerSec += 2
                        mHandler.post {
                            if(cursor!!.isLast){
                                cursor.moveToFirst()
                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                                imageView.setImageURI(imageUri)
                            }else{
                                cursor.moveToNext()
                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                                imageView.setImageURI(imageUri)
                            }
                        }
                    }
                }, 2000, 2000)
                button3.text = "停止"
                button1.isClickable = false
                button2.isClickable = false
            }else{
                mTimer!!.cancel()
                mTimer = null
                mTimerSec = 0.0
                button3.text = "再生"
                button1.isClickable = true
                button2.isClickable = true
            }
        }

    }
}

