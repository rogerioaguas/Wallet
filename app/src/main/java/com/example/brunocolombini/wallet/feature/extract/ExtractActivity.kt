package com.example.brunocolombini.wallet.feature.extract

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_extract.*
import javax.inject.Inject

class ExtractActivity : DaggerAppCompatActivity(), ExtractContract.View {

    @Inject
    lateinit var presenter: ExtractContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extract)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        presenter.getAllExtracts()


    }

    override fun setRecycleView(extracts: List<Extract>) {
        this.recyclerView.adapter = ExtractAdapter(this, extracts)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }


    companion object {
        fun getCallingIntent(context: Context): Intent = Intent(context, ExtractActivity::class.java)
    }

}