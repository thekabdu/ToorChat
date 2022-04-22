package kz.diaspora.app.ui.forgot_password

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.item_chat.*
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentForgotPasswordBinding
import kz.diaspora.app.ui.MainActivity
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.sign_up.SignUpFragment
import kz.my_portfel.app.ui.pincode.DropPasswordFragment

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: ForgotPasswordViewModel by viewModels()
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    val bundle= Bundle()

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
        setObservers()
        setListeners()

    }

    private fun setObservers() {
        with(viewModel) {

                emailData.observe(viewLifecycleOwner, {
                    (activity as StartActivity).addFragment(DropPasswordFragment())

                })
                error.observe(viewLifecycleOwner, {
                    Toast.makeText(
                            context,
                            "Такой почты не существует",
                            Toast.LENGTH_LONG
                    ).show()
                })


        }

    }

    private fun setListeners() {
        binding.btnDrop.setOnClickListener {
            viewModel.forgotEmail(binding.etEmail.text.toString())
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
