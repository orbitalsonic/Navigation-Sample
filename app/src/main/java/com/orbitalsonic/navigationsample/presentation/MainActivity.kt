package com.orbitalsonic.navigationsample.presentation

import android.content.IntentSender
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.common.IntentSenderForResultStarter
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.orbitalsonic.navigationsample.R
import com.orbitalsonic.navigationsample.databinding.ActivityMainBinding
import com.orbitalsonic.navigationsample.helpers.lifecycle.launchWhenResumed
import com.orbitalsonic.navigationsample.presentation.base.activities.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private var navController: NavController? = null

    private var forceUpdateLauncher: ActivityResultLauncher<IntentSenderRequest>? =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                // Do your work here
            }
        }

    override fun onCreated() {
        initNavController()
        initNavListener()
        forceUpdate()
    }

    private fun initNavController() {
        navController =
            (supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment).navController
    }

    private fun initNavListener() {
        navController?.addOnDestinationChangedListener(navDestinationListener)
    }

    private val navDestinationListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {}
                R.id.settingsFragment -> {}
                R.id.languageFragment -> {}
                else -> {}
            }
        }

    private fun forceUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask: Task<AppUpdateInfo> =
            appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    val starter =
                        IntentSenderForResultStarter { intent, _, fillInIntent, flagsMask, flagsValues, _, _ ->
                            val request = IntentSenderRequest.Builder(intent)
                                .setFillInIntent(fillInIntent)
                                .setFlags(flagsValues, flagsMask)
                                .build()
                            launchWhenResumed {
                                try {
                                    forceUpdateLauncher?.launch(request)
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            }
                        }

                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        starter,
                        1234
                    )
                } catch (exception: IntentSender.SendIntentException) {
                    exception.printStackTrace()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }.addOnFailureListener { ex: Exception ->
            ex.printStackTrace()
        }
    }

    override fun onDestroy() {
        navController?.removeOnDestinationChangedListener(navDestinationListener)
        super.onDestroy()
    }
}