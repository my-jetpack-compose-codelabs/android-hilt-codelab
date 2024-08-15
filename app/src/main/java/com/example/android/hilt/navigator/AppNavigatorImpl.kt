/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.hilt.navigator

import androidx.fragment.app.FragmentActivity
import com.example.android.hilt.R
import com.example.android.hilt.ui.ButtonsFragment
import com.example.android.hilt.ui.LogsFragment
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * Navigator implementation.
 */

// 我们要实现通过@Inject constructor的方法是进行注入,就要在 hilt 的模块里面提供参数activity: FragmentActivity
// 这里我们实现了需求2, 这是因为 hilt 给我提供一些预定义绑定, hilt 预定义绑定
// 预定义1: activity, application 等 android 组件的实例,也就是被注入的 android 组件本身我们可以不用指定了,但是要注意,要考虑 hilt 组件的层级, 如果组件是SingletonComponent,那么 activity 可能还没实例化所以无法提供, 但是组件是 FragmentComponent的话,可以保证 application 和 activity 都已经实例化了所以可以实现
// 预定义2: 可以提android 组件的上下文,如@ApplicationContext, 规则和上述一致,需要 hilt 组件够低才能提供
// 在一个 Activity 下使用同一个 navigator 实例即可,所以这里添加上作用域指定
@ActivityScoped
class AppNavigatorImpl @Inject constructor(private val activity: FragmentActivity) : AppNavigator {
    override fun navigateTo(screen: Screens) {
        val fragment = when (screen) {
            Screens.BUTTONS -> ButtonsFragment()
            Screens.LOGS -> LogsFragment()
        }

        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(fragment::class.java.canonicalName)
            .commit()
    }
}
