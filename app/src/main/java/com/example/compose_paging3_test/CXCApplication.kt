package com.example.compose_paging3_test

import android.app.Application
import android.webkit.WebView
import androidx.lifecycle.SavedStateHandle
import com.cxc.cxctoday.data.repo.home.HomeRepo
import com.cxc.cxctoday.data.repo.home.IHomeRepo
import com.cxc.cxctoday.ui.home.recommend.hot_tags.HomeAllTagsViewModel
import com.example.compose_paging3_test.data.NetworkModule
import com.facebook.stetho.Stetho
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class CXCApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        setupKoin()

        if (BuildConfig.DEBUG) {
//            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }

    private fun setupKoin() {
        val utilModule = module {
            single<Gson> {
                Gson()
            }
        }

        val remoteModule = module {
            single { NetworkModule.provideOkHttpClient() }
            single { NetworkModule.provideRetrofit(get()) }
            single { NetworkModule.provideHomeApi(get()) }
        }

        val repoModule = module {
            single<IHomeRepo> { HomeRepo(get()) }
        }

        val vmModule = module {
            //home
            viewModel { (handle: SavedStateHandle) -> HomeAllTagsViewModel(handle, get()) }

        }

        startKoin {
            androidContext(this@CXCApplication)
            modules(
                utilModule,
                remoteModule,
                repoModule,
                vmModule
            )
        }
    }
}