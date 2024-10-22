package com.betan.betanstoktakip.presentation.auth.login

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.betan.betanstoktakip.R
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.core.extensions.click
import com.betan.betanstoktakip.core.extensions.orEmpty
import com.betan.betanstoktakip.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(
    bindingInflater = FragmentLoginBinding::inflate
) {

    private val viewModel: LoginViewModel by viewModels()

    override fun setupViews(savedInstanceState: Bundle?) {
        setupListeners()
    }

    override fun collectData() {
        collect(viewModel.eventState, ::collectEventState)
        collect(viewModel.uiState, ::collectUiState)
        collect(viewModel.failState, ::collectFailState)
    }

    private fun setupListeners() {
        with(binding) {
            buttonLogin.click {
                val action = LoginContract.Action.Login(
                    email = editTextEmail.text.orEmpty(),
                    password = editTextPassword.text.orEmpty()
                )
                viewModel.invoke(action)
            }
        }
    }

    private fun collectUiState(uiState: LoginContract.UiState) {
        with(binding) {
            editTextEmail.setText(uiState.email)
        }
    }

    private fun collectEventState(event: LoginContract.Event) {
        when (event) {
            LoginContract.Event.GoToNextScreen -> goToNextScreen()
        }
    }

    private fun goToNextScreen() {
    }
}