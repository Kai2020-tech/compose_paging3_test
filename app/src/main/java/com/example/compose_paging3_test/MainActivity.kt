package com.example.compose_paging3_test

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cxc.cxctoday.ui.home.recommend.hot_tags.HomeAllTagsFragment
import com.example.compose_paging3_test.base.BaseActivity
import com.example.compose_paging3_test.base.BaseViewModel
import com.example.compose_paging3_test.databinding.ActivityHomeHotTagsBinding

class MainActivity : BaseActivity<ActivityHomeHotTagsBinding>() {


    override fun initViewBinding(): ActivityHomeHotTagsBinding =
        ActivityHomeHotTagsBinding.inflate(layoutInflater)

    override fun initParam(data: Bundle) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        replaceFragment(R.id.fcv_hot_tags, HomeAllTagsFragment.newInstance())
    }

    override fun getViewModel(): BaseViewModel? = null

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}