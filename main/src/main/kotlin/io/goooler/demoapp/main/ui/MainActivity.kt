package io.goooler.demoapp.main.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayoutMediator
import com.permissionx.guolindev.PermissionX
import io.goooler.demoapp.adapter.vp.CommonFragmentStateAdapter
import io.goooler.demoapp.base.util.unsafeLazy
import io.goooler.demoapp.common.base.BaseThemeActivity
import io.goooler.demoapp.common.router.RouterPath
import io.goooler.demoapp.main.databinding.MainActivityBinding
import io.goooler.demoapp.main.ui.fragment.MainFragment
import io.goooler.demoapp.main.ui.fragment.MainPagingFragment
import io.goooler.demoapp.main.ui.fragment.MainSmartRefreshFragment

@Route(path = RouterPath.main)
class MainActivity : BaseThemeActivity() {

    private val binding by unsafeLazy { MainActivityBinding.inflate(layoutInflater) }

    private val pagerAdapter by unsafeLazy {
        CommonFragmentStateAdapter(supportFragmentManager, lifecycle)
    }

    private val titles = listOf(
        "首页", "smartRefresh", "paging"
    )

    private val fragments = listOf(
        MainFragment.newInstance(),
        MainSmartRefreshFragment.newInstance(),
        MainPagingFragment.newInstance()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewPager.offscreenPageLimit = fragments.size
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
        pagerAdapter.setData(fragments)

        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Core fundamental are based on these permissions",
                    "OK",
                    "Cancel"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "You need to allow necessary permissions in Settings manually",
                    "OK",
                    "Cancel"
                )
            }
            .request { _, _, _ -> }
    }

    /**
     * 不杀掉进程，直接返回桌面
     */
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            val intent = Intent(Intent.ACTION_MAIN)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
        } else {
            super.onBackPressed()
        }
    }
}
