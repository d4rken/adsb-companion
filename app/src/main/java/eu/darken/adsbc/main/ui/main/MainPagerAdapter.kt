package eu.darken.adsbc.main.ui.main

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import eu.darken.adsbc.common.debug.logging.log
import kotlin.reflect.KClass


class MainPagerAdapter constructor(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val pages: List<Page>
) : FragmentStateAdapter(
    fragmentManager,
    lifecycle
) {

    private val fragmentFactory = fragmentManager.fragmentFactory

    init {
        log { "pages=$pages" }
    }

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        val targetPage = pages[position]
        log { "createFragment(): targetPage=$targetPage" }
        return fragmentFactory.instantiate(this.javaClass.classLoader!!, targetPage.fragmentClazz.qualifiedName!!)
    }

    data class Page(
        val fragmentClazz: KClass<out Fragment>,
        @StringRes val titleRes: Int
    )
}
