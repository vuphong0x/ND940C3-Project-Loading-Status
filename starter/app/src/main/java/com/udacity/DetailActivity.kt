package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FILE_NAME = "EXTRA_FILE_NAME"
        const val EXTRA_STATUS = "EXTRA_STATUS"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelNotifications()

        binding.contentDetail.apply {
            textFileName.text = intent.getStringExtra(EXTRA_FILE_NAME) ?: ""
            textStatus.text =
                if (intent.getIntExtra(
                        EXTRA_STATUS,
                        -1
                    ) == DownloadManager.STATUS_SUCCESSFUL
                ) getString(R.string.status_success)
                else getString(R.string.status_fail)

            buttonOK.setOnClickListener {
                finish()
            }
        }
    }
}
