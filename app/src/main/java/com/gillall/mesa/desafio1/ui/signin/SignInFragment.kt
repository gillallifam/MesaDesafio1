package com.gillall.mesa.desafio1.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.*
import com.facebook.*
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.FacebookSdk.setAutoLogAppEventsEnabled
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.gillall.mesa.desafio1.R
import com.gillall.mesa.desafio1.databinding.SigninFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.br.gillall.mesa1.work.RefreshDataWorker
import java.util.*
import java.util.concurrent.TimeUnit


class SignInFragment : Fragment() {

    private lateinit var signInBinding: SigninFragmentBinding
    private lateinit var viewModel: SignInViewModel
    private lateinit var navController: NavController
    private lateinit var bundle: Bundle
    private lateinit var hasToken: String
    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.prefsName), Context.MODE_PRIVATE
        )
        val editor = sharedPref!!.edit()

        hasToken = sharedPref.getString("token", "").toString()

        signInBinding =
            DataBindingUtil.inflate(inflater, R.layout.signin_fragment, container, false)
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        viewModel.token.observe(viewLifecycleOwner, { resp ->
            println(resp.token)
            editor.putString("token", resp.token)
            editor.apply()
            RefreshDataWorker.token = resp.token
            delayedInit()
            bundle = bundleOf("token" to resp.token)
            navController.navigate(R.id.action_signInFragment_to_feedFragment, bundle)
        })

        navController = NavHostFragment.findNavController(this)

        signInBinding.btnLoginEntrar.setOnClickListener {
            viewModel.login(
                signInBinding.editTexPersonEmail.text.toString(),
                signInBinding.editTextTextPassword.text.toString()
            )
        }

        FacebookSdk.sdkInitialize(context)
        //AppEventsLogger.activateApp(context)
        setAutoLogAppEventsEnabled(false);
        callbackManager = CallbackManager.Factory.create();


        val EMAIL = "email"

        signInBinding.loginButton.setReadPermissions(Arrays.asList(EMAIL))
        signInBinding.loginButton.fragment = this
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        signInBinding.loginButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    // App code
                    println(loginResult)
                }

                override fun onCancel() {
                    // App code
                    println("kdflsfksldk")
                }

                override fun onError(exception: FacebookException) {
                    // App code
                    println(exception)
                }
            })

        val accessToken = AccessToken.getCurrentAccessToken()
        val isFaceLogged = accessToken != null && !accessToken.isExpired

        if ( isFaceLogged || hasToken.isNotEmpty()){
            println("Auto login")
            if(isFaceLogged){
                //Ver como proceder
                //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            }

            if(false && hasToken.isNotEmpty()){
                bundle = bundleOf("token" to hasToken)
                navController.navigate(R.id.action_signInFragment_to_feedFragment, bundle)
            }
        }


        signInBinding.btnLoginCadastrar.setOnClickListener { navController.navigate(R.id.action_signInFragment_to_signUpFragment) }

        signInBinding.btnLoginEntrar.callOnClick()

        return signInBinding.root
    }

    /**
     * Setup WorkManager background job.
     */
    private fun setupRecurringWork() {
        println("setup worker")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(30, TimeUnit.SECONDS).setConstraints(
            constraints
        ).build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
    }

    private fun delayedInit() {
        CoroutineScope(Dispatchers.Default).launch {
            setupRecurringWork()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}