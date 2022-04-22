package kz.diaspora.app.ui.forgot_password.resetPaasword

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentForgotPasswordBinding
import kz.diaspora.app.databinding.ResetPasswordFragmentBinding
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.forgot_password.ForgotPasswordViewModel
import kz.diaspora.app.ui.sign_in.SignInFragment
import kz.my_portfel.app.ui.pincode.DropPasswordFragment

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: ResetPasswordViewModel by viewModels()
    private var _binding: ResetPasswordFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
                inflater, R.layout.reset_password_fragment, container, false
        )
        setHasOptionsMenu(true)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()
    }

    private fun setObservers() {
        with(viewModel) {
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })
        }

    }

    private fun setListeners() {
        binding.btnDrop.setOnClickListener {
            viewModel.resetPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString(),binding.etRePassword.text.toString())
            (activity as StartActivity).addFragment(SignInFragment())
            Toast.makeText(activity, "Пароль успешно изменен!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_auth, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
