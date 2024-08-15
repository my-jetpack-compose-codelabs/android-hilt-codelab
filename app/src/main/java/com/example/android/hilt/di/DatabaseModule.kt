package com.example.android.hilt.di

import android.content.Context
import androidx.room.Room
import com.example.android.hilt.data.AppDatabase
import com.example.android.hilt.data.LogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// SingletonComponent替代了ApplicationComponent
// 1. installIn定义了会装载在那个 hilt 组件里面,而不同的 hilt 组件会根据 android 类的生命周期创建销毁,也就是说SingletonComponent会和 app 的生命周期相同,ActivityComponent会和 activity 相同, 如果这个 module 装载在 ActivityComponent的组件里面的话, 就无法为 app 层级提供注入了
@InstallIn(SingletonComponent::class)
@Module
// 关于 object
// 1. object 默认是一个单例,我们在注入的时候可以保持一致, 且也不会重复实例化
// 2. object 是自动实例,其实我们就是直接写了一个实例,不需要显示的实例化了
// 3. object 是静态访问的,因为所有的代码我们都手动指定了,访问性更好
object DatabaseModule {

    // 使用 provide 注解,标注这个方法会提供注入的数据
    @Provides
    // 1. 这里的注解是为了指定提供数据的作用域,这个作用域指定需要和 hilt 组件绑定,如果是是SingletonComponent的话,一定要是Singleton
    // 2. 作用域指定可以省略,省略的时候每次注入都是一个全新的实例,比如在我们在同一个 activity 中需要注入两个类A的实例,如果有@Singleton指定,那么这两个实例是同一个实例,如果没有指定作用域,那么每次注入这个实例都是全新的
    @Singleton
    // 另外这里使用了预定义的限定符,因为有时候我们需要调用 context,我么就可以用@ApplicationContext, 或者是@ActivityContext 来获取到数据接受来源的的Context
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        // 我们这里使用了Room数据库的创建方法
        // 数据库的创建本来是在ServiceLocator中完成的,我们把创建的代码迁移到hilt 中,且标注为一个全局单例,这样在 app 任意位置都可以调用到同一个数据库了
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "logging.db"
        ).build()
    }

    // 1. 因为构造LoggerLocalDataSource,需要一个 logDao 才可以操纵数据库
    // 2. 而这个 logDao 又是AppDatabase的属性
    // 3. 所以提供 fragment 需要注入LoggerLocalDataSource实例去操作,查看 database -> 创建loggerLocalDataSource需要一个 logDao -> logDao 又需要一个 database -> 所以提供一个LoggerLocalDataSource实例需要提供 database 和 Dao
    // 4. @Provides方法中需要的参数会从@Inject的构造方法和@Provides方法中找寻数据源
    @Provides
    fun provideLogDao(database: AppDatabase): LogDao {
        return database.logDao()
    }
}