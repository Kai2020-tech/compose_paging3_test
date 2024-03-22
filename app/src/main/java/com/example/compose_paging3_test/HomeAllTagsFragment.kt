package com.cxc.cxctoday.ui.home.recommend.hot_tags

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.compose_paging3_test.base.BaseFragment
import com.example.compose_paging3_test.base.BaseViewModel
import com.example.compose_paging3_test.databinding.FragmentComposeViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeAllTagsFragment : BaseFragment<FragmentComposeViewBinding>() {

    companion object {
        fun newInstance() = HomeAllTagsFragment()
    }

    private val vm by viewModel<HomeAllTagsViewModel>()

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentComposeViewBinding = FragmentComposeViewBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.apply {
            composeView.setContent {
                AllTagsScreen(vm)
            }
        }
    }

    override fun bindFragmentListener(context: Context) {
    }

    override fun getViewModel(): BaseViewModel = vm

}