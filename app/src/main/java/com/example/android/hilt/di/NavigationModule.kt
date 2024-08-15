package com.example.android.hilt.di

import com.example.android.hilt.navigator.AppNavigator
import com.example.android.hilt.navigator.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
// 这里我们没有用 object 而是用了抽象类,是因为这里我们是要使用@Bind 注解为接口提供实现接口的类,所以这方法并不需要实现,所以bindNavigator是一个抽象方法,module 也同样需要是一个抽象类
abstract class NavigationModule {

    // 这里我们实现了注入AppNavigator实例的需求1, 告诉 hilt 注入 AppNavigator 接口的实例的类是AppNavigatorImpl
    // 这里我们只需要告知 hilt 实现AppNavigator的类是AppNavigatorImpl即可,其他的样板代码 hilt 都可以自己实现了
    @Binds
    abstract fun bindNavigator(impl: AppNavigatorImpl): AppNavigator
}