package com.example.educonnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.R
import com.example.educonnect.adapter.NewsAdapter
import com.example.educonnect.dataclass.Article
import com.example.educonnect.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private val articles = mutableListOf<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_article, container, false)


        recyclerView = view.findViewById(R.id.ItemsForNews)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        newsAdapter = NewsAdapter(articles)
        recyclerView.adapter = newsAdapter

        fetchArticles()

        return view
    }

    private fun fetchArticles() {
        val apiKey = "2e716340a06e430b89737a46b748aa95"

        CoroutineScope(Dispatchers.IO).launch {
            try {

                val response = RetrofitInstance.api.getTechNews(apiKey = apiKey)
                if (response.isSuccessful) {
                    val fetchedArticles = response.body()?.articles ?: emptyList()


                    withContext(Dispatchers.Main) {
                        articles.clear()
                        articles.addAll(fetchedArticles)
                        newsAdapter.notifyDataSetChanged()
                    }
                } else {

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to fetch news: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
