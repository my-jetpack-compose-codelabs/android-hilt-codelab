package com.example.android.hilt.di

import com.example.android.hilt.data.LoggerDataSource
import com.example.android.hilt.data.LoggerInMemoryDataSource
import com.example.android.hilt.data.LoggerLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

// 问题: 因为我们绑定了两个LoggerDataSource的实现类, 所以在注入的时候,hilt 不知道应该提供哪个类的实例
// 解决: 定义限定符, 显示的告诉 hilt 这里需要哪个类的实例注入
@Qualifier
annotation class DatabaseLogger

@Qualifier
annotation class InMemoryLogger

// 这里是对最初的LoggerLocalDataSource实例的注入
@InstallIn(SingletonComponent::class)
@Module
abstract class LoggingDatabaseModule {

    @DatabaseLogger
    @Singleton
    @Binds
    abstract fun bindDatabaseLogger(impl: LoggerLocalDataSource): LoggerDataSource

}

// 这里是对新增的的LoggerInMemoryDataSource实例的注入
@InstallIn(ActivityComponent::class)
@Module
abstract class LoggingInMemoryModule {

    @InMemoryLogger
    @ActivityScoped
    @Binds
    abstract fun bindDatabaseLogger(impl: LoggerInMemoryDataSource): LoggerDataSource
}
