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
import challenge.mapapp.R
import challenge.mapapp.data.local.Result
import challenge.mapapp.ui.viewModels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: MapViewModel by viewModels()
    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val supportMapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment?.getMapAsync(this)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                requireArguments().getString("category")?.let {
                    viewModel.getResultsData(it).collectLatest { showMarker(it) }
                }
            }
        }

        view.findViewById<FloatingActionButton>(R.id.share_button).setOnClickListener {
            Snackbar.make(it, "Not implemented yet", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showMarker(results: List<Result>) {
        results.map {
            map?.addMarker(MarkerOptions()
                .position(LatLng(it.lat?:0.0, it.lng?:0.0))
                .title(it.name))
        }
    }

    override fun onMapReady(readyMap: GoogleMap) {
        map = readyMap

        val evenly = LatLng(52.500342, 13.425170)
        map?.let {
            it.addMarker(MarkerOptions().position(evenly).title("Evenly HQ"))
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(evenly, 15f))
        }
    }
}