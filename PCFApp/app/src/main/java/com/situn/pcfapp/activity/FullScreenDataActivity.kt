package com.situn.pcfapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.situn.pcfapp.R
import com.situn.pcfapp.databinding.ActivityFullScreenDataBinding

class FullScreenDataActivity : AppCompatActivity() {
    private lateinit var activityFullScreenDataBinding : ActivityFullScreenDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFullScreenDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_full_screen_data)

        setDataFullScreen()
    }

    //Set data on full screen
    private fun setDataFullScreen() {
        val imageUrl = intent.getStringExtra("ImageUrl")


        Glide.with(this).load(intent.getStringExtra("ImageUrl"))
            .placeholder(R.drawable.ic_launcher_background)
            .into(activityFullScreenDataBinding.ivImage);

        activityFullScreenDataBinding.tvId.setText(intent.getStringExtra("Id"))
        activityFullScreenDataBinding.tvName.setText(intent.getStringExtra("Name"))
        activityFullScreenDataBinding.tvFullName.setText(intent.getStringExtra("FullName"))
        activityFullScreenDataBinding.tvUrl.setText(intent.getStringExtra("Url"))
        activityFullScreenDataBinding.tvUrl.setMovementMethod(LinkMovementMethod.getInstance());

    }
}