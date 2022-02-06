package eu.darken.adsbc.main

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import androidx.test.core.app.launchActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eu.darken.adsbc.main.ui.MainActivity
import eu.darken.adsbc.main.ui.MainActivityVM
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import testhelper.BaseUITest

@HiltAndroidTest
class MainActivityTest : BaseUITest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @BindValue
    val mockViewModel = mockk<MainActivityVM>(relaxed = true)

    @Before fun init() {
        hiltRule.inject()

        mockViewModel.apply {
            every { state } returns liveData { }
            every { onGo() } just Runs
        }
    }

    @Test fun happyPath() {
        launchActivity<MainActivity>()

        verify { mockViewModel.onGo() }
    }
}