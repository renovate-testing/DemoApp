package io.goooler.demoapp.base.type

import androidx.annotation.StringDef
import io.goooler.demoapp.base.type.SpKeys.Companion.SP_FIRST_RUN

// todo 似乎无效，后续优化
@StringDef(
    SP_FIRST_RUN
)
annotation class SpKeys {
    companion object {
        const val SP_FIRST_RUN = "firstRun"
    }
}

@StringDef
annotation class MkKeys