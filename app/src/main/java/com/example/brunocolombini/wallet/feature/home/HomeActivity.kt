package com.example.brunocolombini.wallet.feature.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.feature.exchange.ExchangeFragment
import com.example.brunocolombini.wallet.feature.extract.ExtractActivity
import com.example.brunocolombini.wallet.util.delivery.BalanceEventType
import com.example.brunocolombini.wallet.util.enums.BalanceEventType
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, HomeContract.View {


    @Inject
    lateinit var presenter: HomeContract.Presenter

    lateinit var user: UserWallet


    override fun updateBalance(balanceType: BalanceEventType, balance: Double) {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val header: View = navView.getHeaderView(0)

        val fiatBalanceTextView: TextView = header.findViewById(R.id.fiat_balance)
        val btcBalanceTextView: TextView = header.findViewById(R.id.btc_balance)
        val britasBalanceTextView: TextView = header.findViewById(R.id.britas_balance)

        when (balanceType) {
            BalanceEventType.FIAT -> {
                fiatBalanceTextView.text = String.format(resources.getString(R.string.balance_fiat), balance)
            }
            BalanceEventType.BTC -> {
                btcBalanceTextView.text = String.format(resources.getString(R.string.balance_btc), balance)
            }
            BalanceEventType.BRITAS -> {
                britasBalanceTextView.text = String.format(resources.getString(R.string.balance_britas), balance)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        presenter.onAttachView()
        presenter.updateUserInformation()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        presenter.updateUserInformation()
        user = intent.extras.getSerializable(USER_EXTRA) as UserWallet
        configPagerAdapter()
    }

    private fun configPagerAdapter() {
        pager.adapter = PagerAdapter(this, user, supportFragmentManager)
        pagerTitle.setupWithViewPager(pager)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.onDestroy()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.extract -> {
                startActivity(ExtractActivity.getCallingIntent(this))
            }
            R.id.log_out -> {
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun updateUserInformation(extracts: List<Extract>) {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val header: View = navView.getHeaderView(0)

        val userNameTextView: TextView = header.findViewById(R.id.user_name)
        val fiatBalanceTextView: TextView = header.findViewById(R.id.fiat_balance)
        val btcBalanceTextView: TextView = header.findViewById(R.id.btc_balance)
        val britasBalanceTextView: TextView = header.findViewById(R.id.britas_balance)

        userNameTextView.text = user.username

        for (extract in extracts) {
            when (extract.coin) {
                resources.getString(R.string.fiat) -> {
                    fiatBalanceTextView.text = String.format(resources.getString(R.string.balance_fiat), extract.amount)
                }
                resources.getString(R.string.btc) -> {
                    btcBalanceTextView.text = String.format(resources.getString(R.string.balance_btc), extract.amount)
                }
                resources.getString(R.string.bts) -> {
                    britasBalanceTextView.text = String.format(resources.getString(R.string.balance_britas), extract.amount)
                }
            }

        }

    }

    companion object {
        fun getCallingIntent(context: Context, user: UserWallet): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(USER_EXTRA, user)
            return intent
        }

        const val USER_EXTRA = "USER"
    }
}


class PagerAdapter(
        private val context: Context,
        private val user: UserWallet,
        fragment: FragmentManager
) : FragmentStatePagerAdapter(fragment) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ExchangeFragment.newInstance(type = BalanceEventType.BTC, user = user)
            else -> ExchangeFragment.newInstance(type = BalanceEventType.BRITAS, user = user)
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        val title = when (position) {
            0 -> BalanceEventType.BTC.type
            else -> BalanceEventType.BRITAS.type
        }
        return context.getString(title)
    }

    override fun getCount() = BalanceEventType.values().size - 1
}
