package br.edu.infnet.anotacoes

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.infnet.anotacoes.viewModel.LocationModel
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_location.*
import java.util.*


class LocationActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>? = null
    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var auth: FirebaseAuth;
    private final var TAG = "Anúncio"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        MobileAds.initialize(this) {}
        auth = Firebase.auth
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                adError?.message?.let { Log.d(TAG, it) }
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
                startAdSense()
            }
        })

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                mInterstitialAd = null
            }

        }

        val account = GoogleSignIn.getLastSignedInAccount(this)

        if(account != null) {
            email.text = "Email: ${account.email}"
        }
        if(auth.currentUser?.email != null) {
            email.text = "Email ${auth.currentUser?.email}"
        }
        layoutManager = LinearLayoutManager(this)

        val downloadFolder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val files = downloadFolder?.listFiles()
        val filesNames = arrayListOf<LocationModel>()
        if (files != null) {
            for (file in files) {
                val filename = file.name.toString().split('_');
                var text = file.readText();
                var date = filename[0]
                val title = filename[1].split('.')[0]
                filesNames.add(LocationModel(date, title, text))
            }
        }

        recyclerView.layoutManager = layoutManager
        adapter = RecyclerViewAdapter(filesNames, this)
        recyclerView.adapter = adapter

        sign_out_button.setOnClickListener{
            signOut();
        }
    }

    private fun signOut() {
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if(account != null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, OnCompleteListener<Void?> {
                })
        }
        if (auth.currentUser != null) {
            auth.signOut()
        }
        goToSignInActivity()
    }

    private fun goToSignInActivity() {
        var signInActivity = Intent(this, SignInActivity::class.java)
        startActivity(signInActivity)
        finish()
    }

    fun startAdSense() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }
}