package me.demo.yandexsimulator.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import me.demo.yandexsimulator.R
import me.demo.yandexsimulator.databinding.FragmentSearchBinding
import me.demo.yandexsimulator.di.AppComponent
import me.demo.yandexsimulator.domain.MapState
import me.demo.yandexsimulator.ui.search.SearchFragmentViewModel.*
import me.demo.yandexsimulator.ui.search.adapter.DecodeAddressAdapter
import me.demo.yandexsimulator.ui.shared.MapSharedViewModel
import me.demo.yandexsimulator.utils.hideKeyboard
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val activityViewModel by activityViewModels<MapSharedViewModel> { factory }
    private val viewModel by viewModels<SearchFragmentViewModel> { factory }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var adapter: DecodeAddressAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureInitialState()
        initUI()
        initVM()
    }

    private fun configureInitialState() {
        viewModel.setCurrentActiveField(LocationField.NONE)
        binding.fromLocationEt.setText(activityViewModel.fromPoint.value?.name ?: "")
        binding.toLocationEt.setText(activityViewModel.toPoint.value?.name ?: "")
    }

    private fun initUI() {
        adapter = DecodeAddressAdapter {
            changeQuery("")
            requireContext().hideKeyboard(binding.root)
            activityViewModel.onPointChangedInSearch(it)
            viewModel.decodeAddressSelected(it)
            when (activityViewModel.mapState.value!!) {
                MapState.PICK_FROM_LOCATION -> {
                    viewModel.setCurrentActiveField(LocationField.LOCATION_TO)
                    binding.toLocationEt.requestFocus()
                    binding.fromLocationEt.setText(it.name)
                }
                MapState.PICK_TO_LOCATION -> {
                    viewModel.setCurrentActiveField(LocationField.NONE)
                    findNavController().popBackStack()
                }
            }
        }
        binding.addressList.apply {
            adapter = this@SearchFragment.adapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                    .apply {
                        ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
                            setDrawable(it)
                        }
                    }
            )
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.fromMapIv.setOnClickListener {
            activityViewModel.mapState.value = MapState.PICK_FROM_LOCATION
            findNavController().popBackStack()
        }

        binding.toMapIv.setOnClickListener {
            activityViewModel.mapState.value = MapState.PICK_TO_LOCATION
            findNavController().popBackStack()
        }

        binding.fromLocationEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.setCurrentActiveField(LocationField.LOCATION_FROM)
                activityViewModel.mapState.value = MapState.PICK_FROM_LOCATION
                changeQuery(binding.fromLocationEt.text.toString())
            }
        }

        binding.toLocationEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.setCurrentActiveField(LocationField.LOCATION_TO)
                activityViewModel.mapState.value = MapState.PICK_TO_LOCATION
                changeQuery(binding.toLocationEt.text.toString())
            }
        }

        binding.fromLocationEt.doOnTextChanged { text, _, _, _ ->
            if (viewModel.currentActiveField.value == LocationField.LOCATION_FROM)
                changeQuery(text.toString())
        }

        binding.toLocationEt.doOnTextChanged { text, _, _, _ ->
            if (viewModel.currentActiveField.value == LocationField.LOCATION_TO)
                changeQuery(text.toString())
        }
    }

    private fun changeQuery(query: String) {
        viewModel.submitQuery(query)
        adapter?.queryText = query
    }

    private fun initVM() {
        viewModel.loadingState.observe(viewLifecycleOwner){
            binding.progress.isVisible = true
        }
        viewModel.searchResults.observe(viewLifecycleOwner) {
            binding.progress.isVisible = false
            adapter?.submitList(it)
        }
    }

    //MARK - Callbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponent.get().inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}