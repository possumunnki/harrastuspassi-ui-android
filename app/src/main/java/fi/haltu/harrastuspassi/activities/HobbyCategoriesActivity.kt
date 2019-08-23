package fi.haltu.harrastuspassi.activities

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import fi.haltu.harrastuspassi.R
import fi.haltu.harrastuspassi.adapters.CategoryListAdapter
import fi.haltu.harrastuspassi.models.Category
import fi.haltu.harrastuspassi.utils.jsonArrayToCategoryList
import org.json.JSONArray import java.io.IOException
import java.net.URL
import android.content.Intent
import android.view.Menu
import android.view.MenuItem


class HobbyCategoriesActivity : AppCompatActivity() {

    private var categoryList = ArrayList<Category>()
    private lateinit var listView: RecyclerView
    private var selectedCategories: HashSet<Int> = HashSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hobby_categories)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Valitse harrastus"
        selectedCategories = intent.extras!!.getSerializable("EXTRA_SELECTED_ITEMS") as HashSet<Int>

        val categoryAdapter = CategoryListAdapter(categoryList, this, selectedCategories) { category: Category -> categoryItemClicked(category)}
        getCategories().execute()
        listView = this.findViewById(R.id.category_list_view)

        listView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = categoryAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            selectedCategories = data!!.extras!!.getSerializable("EXTRA_SELECTED_ITEMS") as HashSet<Int>
            val categoryAdapter = CategoryListAdapter(categoryList, this, selectedCategories) { category: Category -> categoryItemClicked(category)}
            listView.apply {
                layoutManager = LinearLayoutManager(this.context)
                adapter = categoryAdapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_filters, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.save -> finish()
        }
        return true
    }

    override fun finish() {
        val intent = Intent()
        Log.d("finish1", selectedCategories.toString())
        intent.putExtra("EXTRA_SELECTED_ITEMS", selectedCategories)
        setResult(1, intent)
        super.finish()
    }

    private fun categoryItemClicked(category: Category) {

        if(selectedCategories.contains(category.id!!)) {
            selectedCategories.remove(category.id!!)

        } else {
            selectedCategories.add(category.id!!)
        }
        listView.adapter!!.notifyDataSetChanged()
        val text = category.name
        val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
        toast.show()
    }

    companion object {
        const val ERROR = "error"
        const val NO_INTERNET = "no_internet"
    }

    internal inner class getCategories: AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): String {
            return try {
                URL(getString(R.string.API_URL) + "/hobbycategories/?include=child_categories&parent=null").readText()
            } catch (e: IOException) {
                return ERROR
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            when(result) {
                ERROR -> {
                    Log.d("HobbyCategory", "Error")
                }
                else -> {
                    val jsonArray = JSONArray(result)
                    categoryList.clear()
                    categoryList.addAll(jsonArrayToCategoryList(jsonArray))
                    listView.adapter!!.notifyDataSetChanged()
                }
            }
        }
    }
}
