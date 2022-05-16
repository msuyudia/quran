package com.suy.quran.ui.splash

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.suy.quran.R
import com.suy.quran.databinding.ActivitySplashBinding
import com.suy.quran.ui.AlertBSDFragment
import com.suy.quran.ui.BaseActivity
import com.suy.quran.ui.quran.QuranActivity

import dagger.hilt.android.AndroidEntryPoint
import java.lang.RuntimeException

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivitySplashBinding.inflate(layoutInflater)

    companion object {
        private const val REQUEST_CODE = 200
    }

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }
    private val appUpdateManagerListener by lazy {
        InstallStateUpdatedListener {
            if (it.installStatus() == InstallStatus.DOWNLOADED) AlertBSDFragment.newInstance(
                R.mipmap.ic_launcher_round,
                "Update Aplikasi Al-Quran Indonesia",
                "Silahkan tutup dan buka kembali aplikasi Al-Quran Indonesia",
                "",
                "Siap",
                object : AlertBSDFragment.Callback {
                    override fun onLeftButtonClicked() {
                        //Just for dismiss
                    }

                    override fun onRightButtonClicked() {
                        appUpdateManager.completeUpdate()
                    }
                }
            ).showDialog(supportFragmentManager)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        appUpdateManager.registerListener(appUpdateManagerListener)
    }

    override fun onResume() {
        super.onResume()
        checkUpdate()
    }

    override fun onDestroy() {
        appUpdateManager.unregisterListener(appUpdateManagerListener)
        super.onDestroy()
    }

    private fun checkUpdate() {
        appUpdateManager.appUpdateInfo.addOnCompleteListener { task ->
            if (task.isComplete) {
                if (task.isSuccessful) {
                    val info = task.result
                    if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ||
                        info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                    ) {
                        if (info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                            startUpdate(info, AppUpdateType.IMMEDIATE)
                        } else if (info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                            startUpdate(info, AppUpdateType.FLEXIBLE)
                        }
                    } else openApp()
                } else openApp()
            } else openApp()
        }
    }

    private fun openApp() {
        startActivity(QuranActivity.getIntent(this))
        finish()
    }

    private fun startUpdate(appUpdateInfo: AppUpdateInfo, appUpdateType: Int) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                appUpdateType,
                this,
                REQUEST_CODE
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> showToast("Updating")
                RESULT_CANCELED -> AlertBSDFragment.newInstance(
                    R.drawable.ic_warning,
                    "Update Dibatalkan",
                    "Kamu membatalkan update aplikasi Al-Quran Indonesia. Ulangi update?",
                    "Tidak",
                    "Iya",
                    object : AlertBSDFragment.Callback {
                        override fun onLeftButtonClicked() {
                            onBackPressed()
                        }

                        override fun onRightButtonClicked() {
                            checkUpdate()
                        }
                    }
                ).showDialog(supportFragmentManager)
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> AlertBSDFragment.newInstance(
                    R.drawable.ic_warning,
                    "Update Gagal",
                    "Gagal update aplikasi Al-Quran Indonesia. Apakah Kamu ingin mengulang update?",
                    "Tidak",
                    "Iya",
                    object : AlertBSDFragment.Callback {
                        override fun onLeftButtonClicked() {
                            onBackPressed()
                        }

                        override fun onRightButtonClicked() {
                            checkUpdate()
                        }
                    }
                ).showDialog(supportFragmentManager)
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}