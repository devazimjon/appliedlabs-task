package me.demo.yandexsimulator.di.module.viewmodel

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.demo.yandexsimulator.di.module.viewmodel.factory.ViewModelKey
import me.demo.yandexsimulator.ui.shared.MapSharedViewModel
import me.demo.yandexsimulator.ui.map.MapFragmentViewModel
import me.demo.yandexsimulator.ui.search.SearchFragmentViewModel

@Module
interface ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapSharedViewModel::class)
    fun bindMainActivityViewModel(mainViewModel: MapSharedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapFragmentViewModel::class)
    fun bindAppsViewModel(viewModel: MapFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchFragmentViewModel::class)
    fun bindSettingBottomSheetViewModel(viewModel: SearchFragmentViewModel): ViewModel
}