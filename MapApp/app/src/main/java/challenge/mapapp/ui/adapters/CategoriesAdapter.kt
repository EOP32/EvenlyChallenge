package challenge.mapapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import challenge.mapapp.R
import challenge.mapapp.ui.viewModels.MainViewModel.CategoriesAndResults

class CategoriesAdapter(
    private val listener: PlacesAdapter.ClickListener
): RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    private var categoriesAndResults: List<CategoriesAndResults> = emptyList()

    inner class ViewHolder(val containerView: View) : RecyclerView.ViewHolder(containerView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categoriesAndResults[position]
        holder.containerView.findViewById<TextView>(R.id.main_fragment_category_text).text = item.category
        holder.containerView.findViewById<TextView>(R.id.main_fragment_category_count).text = item.items.size.toString()
        val recyclerView = holder.containerView.findViewById<RecyclerView>(R.id.main_fragment_places_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(holder.containerView.context).apply {
            recyclerView.addItemDecoration(DividerItemDecoration(holder.containerView.context, orientation))
        }
        recyclerView.adapter = PlacesAdapter(item.items, listener)

        holder.containerView.setOnClickListener { handleClick(recyclerView, holder.containerView)}
    }

    private fun handleClick(recyclerView: RecyclerView, containerView: View) {
        if (recyclerView.visibility == View.VISIBLE) {
            recyclerView.visibility = View.GONE
            containerView.findViewById<ImageView>(R.id.main_fragment_arrow).also {
                it.setImageResource(R.drawable.keyboard_arrow_down)
            }
        } else {
            recyclerView.visibility = View.VISIBLE
            containerView.findViewById<ImageView>(R.id.main_fragment_arrow).also {
                it.setImageResource(R.drawable.keyboard_arrow_up)
            }
        }
    }

    override fun getItemCount(): Int {
        return categoriesAndResults.size
    }

    fun addNewItems(categories: List<CategoriesAndResults>) {
        this.categoriesAndResults = categories
        notifyDataSetChanged()
    }
}
