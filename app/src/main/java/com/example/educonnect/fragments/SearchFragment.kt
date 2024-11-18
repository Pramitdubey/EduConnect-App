package com.example.educonnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.R
import com.example.educonnect.adapter.CategoriesRecyclerAdapter
import com.example.educonnect.dataclass.Categories
import java.util.Locale

class SearchFragment : Fragment() ,CategoriesRecyclerAdapter.OnItemClickListener{
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoriesRecyclerAdapter: CategoriesRecyclerAdapter
    private lateinit var searchView: SearchView
    private var searchList: ArrayList<Categories> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        categoriesRecyclerView = view.findViewById(R.id.recyclerViewForSearch)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchView = view.findViewById(R.id.searchView)

        val categories = listOf(
            Categories("Science", R.drawable.baseline_science_24),
            Categories("Psychology", R.drawable.baseline_psychology_24),
            Categories("BioTech", R.drawable.baseline_biotech_24)
        )

        categoriesRecyclerAdapter = CategoriesRecyclerAdapter(categories,this)
        categoriesRecyclerView.adapter = categoriesRecyclerAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText?.toLowerCase(Locale.getDefault()) ?: ""
                searchList.clear()

                if (searchText.isNotEmpty()) {
                    categories.forEach {
                        if (it.title.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            searchList.add(it)
                        }
                    }
                    categoriesRecyclerAdapter.updateData(searchList)
                } else {
                    categoriesRecyclerAdapter.updateData(categories)
                }
                return true
            }
        })

        return view
    }

    override fun onItemClick(category: Categories) {
        val newFragment = TeachersListFragment.newInstance(category)
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_container, newFragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}
