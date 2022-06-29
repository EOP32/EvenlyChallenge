package challenge.mapapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import challenge.mapapp.R
import challenge.mapapp.data.local.Result
import challenge.mapapp.ui.adapters.CategoriesAdapter
import challenge.mapapp.ui.adapters.PlacesAdapter
import challenge.mapapp.ui.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(), PlacesAdapter.ClickListener {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesAdapter = CategoriesAdapter(this)
        recyclerView = view.findViewById<RecyclerView?>(R.id.categories_recyclerview).apply { adapter = categoriesAdapter }
        recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), orientation))
        }

        viewModel.startWork()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoriesAndResults.collectLatest { categoriesAdapter.addNewItems(it) }
            }
        }
    }

    override fun onClick(result: Result) {
        findNavController().navigate(R.id.action_global_mapFragment, Bundle().apply { putString("category", result.category)})
    }
}