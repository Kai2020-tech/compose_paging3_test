package com.example.compose_paging3_test.base

import android.app.AlertDialog
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected var binding: VB? = null
        private set
    private var viewJob: Job? = null
    private var connectivityManager: ConnectivityManager? = null
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var loadingDialog: AlertDialog? = null
    inline fun <reified K : BaseViewModel> generateVM(): Lazy<K> {
        return stateViewModel(state = { intent.extras ?: Bundle() })
    }

    protected abstract fun initViewBinding(): VB
    protected abstract fun initParam(data: Bundle)
    protected abstract fun initView(savedInstanceState: Bundle?)
    protected abstract fun getViewModel(): BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = initViewBinding().apply {
            setContentView(root)
        }

        if (savedInstanceState == null) {
            initParam(intent.extras ?: Bundle.EMPTY)
        }

//        viewJob = getViewModel()?.eventFlow?.onEach {
//        }?.launchIn(lifecycleScope)
        initView(savedInstanceState)

        setConnectivityManager()
    }

    override fun onDestroy() {
        loadingDialog?.dismiss()
        loadingDialog = null
        binding = null
        connectivityManager?.unregisterNetworkCallback(networkCallback)
        super.onDestroy()
    }


    fun addFragmentFadeIn(
        @IdRes containerViewId: Int,
        fragment: Fragment,
        tag: String? = fragment::class.java.name
    ) {
        supportFragmentManager.commit(supportFragmentManager.isStateSaved) {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            add(containerViewId, fragment, tag)
        }
    }

    fun addFragment(
        @IdRes containerViewId: Int,
        fragment: Fragment,
        tag: String? = fragment::class.java.name
    ) {
        supportFragmentManager.commit(supportFragmentManager.isStateSaved) {
            add(containerViewId, fragment, tag)
        }
    }

    fun replaceFragment(
        @IdRes containerViewId: Int,
        fragment: Fragment,
        tag: String? = fragment::class.java.name,
        addToBackStack: Boolean = true
    ) {
        supportFragmentManager.commit(supportFragmentManager.isStateSaved) {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            replace(containerViewId, fragment, tag)
            if (addToBackStack) {
                addToBackStack(tag)
            }
        }
    }

    fun hideFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(0, android.R.anim.fade_out)
            hide(fragment)  //改在fragment中監聽onHiddenChanged, 隱藏後再remove(應用淡出效果)
            commit()
        }
    }

    protected fun popFragment(name: String? = null, include: Boolean = false): Boolean {
        supportFragmentManager.run {
            if (backStackEntryCount == 0) {
                return false
            }
            if (name != null) {
                popBackStack(name, if (include) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0)
            } else {
                popBackStack()
            }
            return true
        }
    }

    protected fun showDialog(fragment: DialogFragment) {
        val fm = supportFragmentManager
        if (fm.isStateSaved || fm.isDestroyed) {
            return
        }
        fragment.show(fm, fragment.javaClass.name)
    }

    fun clearFragments() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
    }

    private fun setConnectivityManager() {
        connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                lifecycleScope.launch {
                    onAvailable()
                }
            }

            override fun onLost(network: Network) {
                lifecycleScope.launch {
                    onLost()
                }
            }
        }

        if ((connectivityManager?.activeNetwork == null)) {
            lifecycleScope.launch {
                onLost()
            }
        }
        connectivityManager?.registerDefaultNetworkCallback(networkCallback)
    }

    open suspend fun onAvailable() {
    }

    open suspend fun onLost() {
    }

    fun showLoadingDialog() {
        if (loadingDialog == null) {
        }
        if (loadingDialog?.isShowing != true)
            loadingDialog?.show()
    }

    fun finishLoadingDialog() {
        loadingDialog?.apply {
            if (this.isShowing) dismiss()
        }
    }
}