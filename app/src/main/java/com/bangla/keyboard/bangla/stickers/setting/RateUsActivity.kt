package com.bangla.keyboard.bangla.stickers.setting

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.Toast
import com.bangla.keyboard.bangla.stickers.R
import com.bangla.keyboard.bangla.stickers.databinding.ActivityRateUsBinding
import com.bangla.keyboard.bangla.stickers.utils.Constant


class RateUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRateUsBinding
    private var ratingData = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateUsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                ratingData = rating
//                Toast.makeText(this, "Rating changed to: $rating", Toast.LENGTH_SHORT).show()
            }
        }

        binding.rateUsBtn.setOnClickListener {
            if (ratingData > 3.0 ||ratingData == 0.0f) {
                val uri =
                    Uri.parse("https://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                val intent1 = Intent(Intent.ACTION_VIEW, uri)
                try {
                    startActivity(intent1)
                } catch (e: Exception) {
                    Toast.makeText(this, "Unable to Rate this app" + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                val appName = getString(R.string.app_name)
                val appVersion = getAppVersion()
                val subject = "$appName V- $appVersion"

                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constant.EMAIL))
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.setPackage("com.google.android.gm")
                startActivity(intent)
            }
        }

    }

    override fun onBackPressed() {
        finish()
    }

    private fun getAppVersion(): String {
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}