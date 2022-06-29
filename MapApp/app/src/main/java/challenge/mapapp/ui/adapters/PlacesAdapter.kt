package challenge.mapapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import challenge.mapapp.R
import challenge.mapapp.data.local.Result

class PlacesAdapter(
    private var results: List<Result> = emptyList(),
    private var listener: ClickListener
): RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    inner class ViewHolder(val containerView: View) : RecyclerView.ViewHolder(containerView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.places_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = results[position]

        holder.containerView.findViewById<TextView>(R.id.places_title).text = item.name
        holder.containerView.findViewById<TextView>(R.id.places_address).text = item.address
        holder.containerView.setOnClickListener {
            listener.onClick(item)
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun addNewItems(results: List<Result>) {
        this.results = results
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onClick(result: Result)
    }
}