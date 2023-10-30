package com.bangla.keyboard.bangla.stickers.setting

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bangla.keyboard.bangla.stickers.R
import com.bangla.keyboard.bangla.stickers.databinding.ActivitySettingBinding
import com.bangla.keyboard.bangla.stickers.utils.Constant
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SettingActivity : AppCompatActivity() {
    lateinit var binding:ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.whatsShareLL.setOnClickListener {


            try {
                val drawable = resources.getDrawable(
                    R.mipmap.app_icon,
                    theme
                ) // your_drawable_id is the image in your drawable you want to share.
                val bitmap = (drawable as BitmapDrawable).bitmap

                val file = File(externalCacheDir, "my_image.png")
                val fOut: OutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                fOut.flush()
                fOut.close()
                file.setReadable(true, false)

                // Use FileProvider to get URI for the file, which is required for sharing the file
                val uri: Uri = FileProvider.getUriForFile(
                    this,
                    "$packageName.provider",  // Change to your application ID
                    file
                )
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    setPackage("com.whatsapp")
                    putExtra(
                        Intent.EXTRA_TEXT, Constant.app_share_message + "\n" + "\n" +
                                "\n" + "Download Now : " + "https://play.google.com/store/apps/details?id=" + applicationContext.packageName
                    )

                    type = "image/png"
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }



                startActivity(shareIntent)
            } catch (e: Exception) {
                Toast.makeText(this, "issue : ${e.message}", Toast.LENGTH_SHORT).show()
                d("CHAGAN",e.message.toString())
            }
        }
       binding.switchVibration.setOnClickListener(View.OnClickListener { view: View? ->
            if (binding.switchVibration.isChecked()) {
                getSharedPreferences("options", MODE_PRIVATE)
                    .edit().putBoolean("vibration_on", true).commit()
            } else {
                getSharedPreferences("options", MODE_PRIVATE)
                    .edit().putBoolean("vibration_on", false).commit()
            }
        })
        binding.llShare.setOnClickListener(View.OnClickListener { view: View? ->
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_message) + getString(R.string.play_store_url) + packageName
            )
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        })
        binding.llOurOtherApps.setOnClickListener(View.OnClickListener { view: View? ->
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(resources.getString(R.string.moreAppUrl))
                )
            )
        })
        binding.llOurWebsite.setOnClickListener(View.OnClickListener { view: View? ->
            try {
                val url = resources.getString(R.string.websiteUrl)
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(
                    ContextCompat.getColor(
                        this,
                        R.color.green
                    )
                )
                val customTabsIntent = builder.build()
                customTabsIntent.intent.setPackage("com.android.chrome")
                customTabsIntent.launchUrl(this, Uri.parse(url))
            } catch (ex: java.lang.Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(resources.getString(R.string.websiteUrl))
                    )
                )
            }
        })
        binding.llPrivacyPolicy.setOnClickListener(View.OnClickListener { view: View? ->
            try {
                val url = resources.getString(R.string.privacyPolicyUrl)
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(
                    ContextCompat.getColor(
                        this,
                        R.color.green
                    )
                )
                val customTabsIntent = builder.build()
                customTabsIntent.intent.setPackage("com.android.chrome")
                customTabsIntent.launchUrl(this, Uri.parse(url))
            } catch (ex: java.lang.Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(resources.getString(R.string.privacyPolicyUrl))
                    )
                )
            }
        })
binding.llRating.setOnClickListener{
    startActivity(Intent(Intent(this,RateUsActivity::class.java)))
}

    }
}