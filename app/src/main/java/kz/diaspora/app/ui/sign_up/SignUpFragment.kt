package kz.diaspora.app.ui.sign_up

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentSignUpBinding
import kz.diaspora.app.ui.MainActivity
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.forgot_password.ForgotPasswordFragment
import kz.diaspora.app.ui.profile.ProfileFragment
import kz.diaspora.app.ui.sign_in.AboutBSFragment
import kz.diaspora.app.ui.sign_in.SignInFragment


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: SignUpViewModel by viewModels()
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sign_up, container, false
        )
        setHasOptionsMenu(true)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObservers()
        setListeners()
    }


    private fun initView() {

    }

    private fun setObservers() {
        with(viewModel) {
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.status}", Toast.LENGTH_LONG).show()
//                Toast.makeText(
//                    context,
//                    "Заполните ячейку Откуда вы и Местоположение",
//                    Toast.LENGTH_LONG
//                ).show()
            })

            loginData.observe(viewLifecycleOwner, {
                if (it.access_token.isNotEmpty()) {
                    (activity as StartActivity).addFragment(SignInFragment())
                        Toast.makeText(activity, "Поздравлям Вы создали аккаунт", Toast.LENGTH_LONG).show()


                }
            })
        }
    }

    private fun setListeners() {
        binding.btnSignUp.setOnClickListener {
            viewModel.register(
                binding.etUsername.text.toString(),
                binding.etPassword.text.toString(),
                binding.etNameSurname.text.toString(),
                binding.etNameName.text.toString(),
                binding.etLogin.text.toString()
            )
        }
        binding.btnSignIn.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.txtIAgreeConf.setOnClickListener {
            val url = "https://diaspora.direct/storage/img/theme/soglashenie.pdf"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_auth, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_additional) {
            val fragment = AboutBSFragment()
            fragment.show(childFragmentManager, "AboutBSFragment")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}