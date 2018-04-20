package com.example.brunocolombini.wallet.feature.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.util.delivery.BalanceEventType
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.w3c.dom.Text
import java.util.*
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, HomeContract.View {


    @Inject
    lateinit var presenter: HomeContract.Presenter

    @Inject
    lateinit var changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>

    override fun updateBalance(balanceType: BalanceEventType?, balance: Double) {
        when (balanceType) {
            BalanceEventType.FIAT -> {
                fiat_balance.text = balance.toString()
            }
            BalanceEventType.BTC -> {
                btc_balance.text = balance.toString()
            }
            BalanceEventType.BRITAS -> {
                britas_balance.text = balance.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        presenter.onAttachView()

        fab.setOnClickListener { view ->
            changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.FIAT, Random().nextDouble()))
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        updateUserInformation(intent.extras.getSerializable(USER_EXTRA) as UserWallet)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.extract -> {
            }
            R.id.log_out -> {
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun updateUserInformation(user: UserWallet) {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val header: View = navView.getHeaderView(0)

        val userNameTextView: TextView = header.findViewById(R.id.user_name)
        val fiatBalanceTextView: TextView = header.findViewById(R.id.fiat_balance)
        val btcBalanceTextView: TextView = header.findViewById(R.id.btc_balance)
        val britasBalanceTextView: TextView = header.findViewById(R.id.britas_balance)

        userNameTextView.text = user.username
        fiatBalanceTextView.text = (fiatBalanceTextView.text as String).replace("%d", user.fiat_balance.toString())
        btcBalanceTextView.text = (btcBalanceTextView.text as String).replace("%d", user.btc_balance.toString())
        britasBalanceTextView.text = (britasBalanceTextView.text as String).replace("%d", user.britas_balance.toString())
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
