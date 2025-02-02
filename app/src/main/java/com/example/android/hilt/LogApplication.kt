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

package com.example.android.hilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// 添加 hilt 的注解, 因为注入也是依赖于生命周期的,所以根据继承的基类,这里的注解需要是HiltAndroidApp
@HiltAndroidApp
// 已经不需要在 app 的 onCreate 的时候初始化 serviceLocator, 因为 hilt 会负责注入
class LogApplication : Application()
