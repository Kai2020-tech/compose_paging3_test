package com.example.compose_paging3_test.base

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job
import org.koin.androidx.viewmodel.ext.android.stateViewModel

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    protected var binding: VB? = null
        private set
    private var viewJob: Job? = null
    private var loadingDialog: AlertDialog? = null

    protected abstract fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): VB

    protected abstract fun initParam(data: Bundle)
    protected abstract fun initView(savedInstanceState: Bundle?)
    protected abstract fun bindFragmentListener(context: Context)
    protected abstract fun getViewModel(): BaseViewModel?
    inline fun <reified K : BaseViewModel> generateVM(): Lazy<K> {
        return stateViewModel(state = { arguments ?: Bundle() })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bindFragmentListener(context)
        activity?.onBackPressedDispatcher?.addCallback(this, onBackPressedCallback())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initParam(arguments ?: Bundle.EMPTY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = initViewBinding(inflater, container, savedInstanceState)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewJob()
        initView(savedInstanceState)
    }

    open fun setViewJob() {
    }

    override fun onDestroyView() {
        binding = null
        viewJob?.cancel()
        viewJob = null
        loadingDialog?.dismiss()
        loadingDialog = null
        super.onDestroyView()
    }


    protected fun popFragment(name: String? = null, include: Boolean = false) {
        childFragmentManager.run {
            if (backStackEntryCount == 0) return
            if (name != null) {
                popBackStack(name, if (include) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0)
            } else {
                popBackStack()
            }
        }
    }

    fun replaceFragment(
        @IdRes containerViewId: Int,
        fragment: Fragment,
        tag: String? = fragment::class.java.name,
        addToBackStack: Boolean = true
    ) {
        parentFragmentManager.commit(parentFragmentManager.isStateSaved) {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            replace(containerViewId, fragment, tag)
            if (addToBackStack) {
                addToBackStack(tag)
            }
        }
    }

    protected fun showDialog(fragment: DialogFragment) {
        val fm = childFragmentManager
        if (fm.isStateSaved || fm.isDestroyed) {
            return
        }
        fragment.show(fm, fragment.javaClass.name)
    }


    open fun onBackPressedCallback(): OnBackPressedCallback {
        return object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (this@BaseFragment.isVisible && childFragmentManager.backStackEntryCount != 0) {
                    popFragment()
                } else {
                    isEnabled = false
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
            }
        }
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