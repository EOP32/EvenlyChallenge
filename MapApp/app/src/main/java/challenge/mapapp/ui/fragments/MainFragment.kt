package challenge.mapapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Operation
import androidx.work.WorkInfo
import challenge.mapapp.R
import challenge.mapapp.data.local.Result
import challenge.mapapp.ui.adapters.CategoriesAdapter
import challenge.mapapp.ui.adapters.PlacesAdapter
import challenge.mapapp.ui.viewModels.MainViewModel
import challenge.mapapp.workers.WebServiceWorker
import com.google.android.material.snackbar.Snackbar
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.workResult().observe(viewLifecycleOwner) { handleWorkInfo(it) }
            }
        }
    }

    private fun handleWorkInfo(workInfoList: List<WorkInfo>) {
        if (workInfoList.isEmpty()) {
            return
        }
        val workInfo = workInfoList[0]

        if (workInfo.state.isFinished) {
            val result = workInfo.outputData.getString(WebServiceWorker.RESULT)

            if (result.equals(WebServiceWorker.FAILURE)) {
                Snackbar.make(requireView(), "Some error happened", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(result: Result) {
        findNavController().navigate(R.id.action_global_mapFragment, Bundle().apply { putString("category", result.category)})
    }
}