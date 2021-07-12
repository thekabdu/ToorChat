package kz.my_portfel.app.ui.pincode

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentDropPasswordBinding
import kz.diaspora.app.ui.StartActivity
import kz.diaspora.app.ui.sign_in.SignInFragment

@AndroidEntryPoint
class DropPasswordFragment : Fragment() {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: DropPasswordViewModel by viewModels()
    private var _binding: FragmentDropPasswordBinding? = null
    private val binding get() = _binding!!

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
        initView()
        setObservers()
        setListeners()
    }


    private fun initView() {

    }

    private fun setObservers() {
        with(viewModel) {
//            loginData.observe(viewLifecycleOwner, {
//                startActivity(Intent(requireContext(), MainActivity::class.java))
//                (activity as StartActivity).finish()
//            })

            error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })
        }

    }

    private fun setListeners() {
        binding.btnSend.setOnClickListener {
            (activity as StartActivity).addFragment(SignInFragment())
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
