package kz.my_portfel.app.ui.pincode

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.common.config.GservicesValue.value
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_drop_password.*
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.item_chat.*
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentDropPasswordBinding
import kz.diaspora.app.domain.model.ForgotEmail
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.forgot_password.resetPaasword.ResetPasswordFragment
import kz.diaspora.app.ui.sign_in.SignInFragment

@AndroidEntryPoint
class DropPasswordFragment : Fragment() {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: DropPasswordViewModel by viewModels()
    private var _binding: FragmentDropPasswordBinding? = null
    private val binding get() = _binding!!
    private val code = Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_drop_password, container, false
        )
        setHasOptionsMenu(true)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }
    private fun setObservers() {
        with(viewModel) {
            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })
        }

    }


    private fun setListeners() {
        binding.btnSend.setOnClickListener {
            viewModel.forgotCode(binding.etEmail.text.toString(), binding.etCode.text.toString().toInt())

            Toast.makeText(activity, "Придумайте новый пароль", Toast.LENGTH_LONG).show()
            (activity as StartActivity).addFragment(ResetPasswordFragment())
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
