package kz.diaspora.app.ui.forgot_password

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentForgotPasswordBinding
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.sign_up.SignUpFragment
import kz.my_portfel.app.ui.pincode.DropPasswordFragment

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: ForgotPasswordViewModel by viewModels()
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_forgot_password, container, false
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
            loginData.observe(viewLifecycleOwner, {
//                startActivity(Intent(requireContext(), MainActivity::class.java))
//                (activity as StartActivity).finish()
            })

            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })
        }

    }

    private fun setListeners() {
        binding.btnDrop.setOnClickListener {
//            viewModel.login(binding.etEmail.text.toString(), binding.etNewPassword.text.toString())
            (activity as StartActivity).addFragment(DropPasswordFragment())
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
