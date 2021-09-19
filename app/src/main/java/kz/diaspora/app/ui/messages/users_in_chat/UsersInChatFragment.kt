package kz.diaspora.app.ui.messages.users_in_chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.UsersInChatFragmentBinding
import kz.diaspora.app.domain.model.User
import kz.diaspora.app.ui.chats.ChatFragment
import kz.diaspora.app.ui.chats.ChatFragmentDirections
import kz.diaspora.app.ui.messages.MessagesFragmentArgs
import kz.diaspora.app.ui.messages.users_in_chat.adapter.UsersInChatAdapter

@AndroidEntryPoint
class UsersInChatFragment : Fragment(), UsersInChatAdapter.OnProjectClickListener {

    private val TAG: String = this::class.java.simpleName

    private val args: UsersInChatFragmentArgs by navArgs()
    private val viewModel: UsersInChatViewModel by viewModels()
    private var _binding: UsersInChatFragmentBinding? = null
    private val binding get() = _binding!!
    private var position = 0

    private val adapter: UsersInChatAdapter by lazy { UsersInChatAdapter(arrayListOf(), this) }

    companion object {
        fun newInstance(): Fragment {
            val fragment = UsersInChatFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.users_in_chat_fragment, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObservers()
        viewModel.getUsersList(args.model.id.toString())

        (activity as AppCompatActivity).supportActionBar?.title = viewModel.messagesData.value?.chat_name
    }

    private fun initView() {
        val mLayoutManager = LinearLayoutManager(context)
        binding.rvUsersInChat.layoutManager = mLayoutManager
        binding.rvUsersInChat.adapter = adapter
        viewModel.getUsersList(args.model.id.toString())

        binding.swipe.setOnRefreshListener {
            adapter.clear()
            viewModel.getUsersList(args.model.id.toString())
        }
    }

    private fun setObservers() {
        with(viewModel) {
            binding.tvNull.visibility = View.GONE
            binding.tvNull.visibility = View.VISIBLE

            usersListData.observe(viewLifecycleOwner, {
                binding.tvNull.visibility = View.GONE
                adapter.clear()
                adapter.add(it)

                usersListData.observe(viewLifecycleOwner, {
                    if (it.isNotEmpty() && adapter.isEmpty()) {
                        adapter.add(it.reversed())
                    }
                })


                /*viewModel.usersListData.value?.let { it1 -> adapter.add(it1) }
                binding.tvNull.visibility = View.GONE*/

               /* if (adapter.itemCount == 0) binding.tvNull.visibility = View.VISIBLE
                (activity as AppCompatActivity).supportActionBar?.title = ""
                args.model.chat_name?.let { it1 -> (activity as MainActivity).setToolbarTitle(it1) }
            })*/
                error.observe(viewLifecycleOwner, {
                Toast.makeText(context, "${it?.error}", Toast.LENGTH_LONG).show()
            })
            })
        }
    }


    override fun onEnterClick(user: User, position: Int) {
        val action = UsersInChatFragmentDirections.toProfileInfoFragment (viewModel.usersListData.value?.get(0)!!.id.toString())
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun refresh() {
        adapter.clear()
      //  viewModel.getUsersList(args.model.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        //adapter.clear()
    }


}