package kz.diaspora.app.ui.chats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
//import com.github.nkzawa.socketio.client.IO
//import com.github.nkzawa.socketio.client.Socket
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.FragmentChatBinding
import kz.diaspora.app.domain.model.ChatModel
import kz.diaspora.app.ui.chats.adapter.ChatAdapter
import java.net.URISyntaxException

@AndroidEntryPoint
class ChatFragment : Fragment(), ChatAdapter.OnProjectClickListener {

    private val TAG: String = this::class.java.simpleName

    private val viewModel: ChatViewModel by viewModels()
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private var position = 0
//    private var socket: Socket? = null

    private val adapter: ChatAdapter by lazy { ChatAdapter(arrayListOf(), this) }

    companion object {
        fun newInstance(): Fragment {
            val fragment = ChatFragment()
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
            inflater, R.layout.fragment_chat, container, false
        )
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
        val mLayoutManager = LinearLayoutManager(context)
        binding.rvMainHome.layoutManager = mLayoutManager
        binding.rvMainHome.adapter = adapter
        viewModel.getChatsList()

        binding.swipe.setOnRefreshListener {
            adapter.clear()
            viewModel.getChatsList()
        }

    }

//    private fun initSocket() {
//        socket = IO.socket("https://diaspora.direct:3000")
//        socket!!.connect()
//        socket!!.emit("join_group", args.chatRoom)
//    }

    private fun setObservers() {
        with(viewModel) {
            binding.tvNullTwo.visibility = View.GONE
            binding.tvNullTwo.visibility = View.VISIBLE
            chatData.observe(viewLifecycleOwner, {
                binding.tvNullTwo.visibility = View.GONE
                adapter.clear()
                viewModel.chatData.value?.let { it1 -> adapter.add(it1) }
                binding.tvNull.visibility = View.GONE
                if (adapter.itemCount == 0) binding.tvNull.visibility = View.VISIBLE
            })
            status.observe(viewLifecycleOwner, {
//                if (it.message != null && it.message == true) {
//                    val action = ChatFragmentDirections.toMessages(chatData.value?.get(position)?.id.toString())
//                    Navigation.findNavController(requireView()).navigate(action)
//                    Toast.makeText(context, "Вы успешно присоединились в чат", Toast.LENGTH_LONG).show()
//                    viewModel.clear()
//                }
            })
            messageData.observe(viewLifecycleOwner, {
                adapter.clear()
                viewModel.getChatsList()
                if (adapter.isEmpty()){ Toast.makeText(context, "Заполните ячейку Откуда вы и Местоположение в профиле", Toast.LENGTH_LONG).show()}
            })
            error.observe(viewLifecycleOwner, {
//                Toast.makeText(context, "Заполните ячейку Откуда вы и Местоположение в профиле", Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun setListeners() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        adapter.clear()
        viewModel.getChatsList()
    }

    override fun onEnterClick(chatModel: ChatModel, position: Int) {
        val action = ChatFragmentDirections.toMessages(chatModel)
        Navigation.findNavController(requireView()).navigate(action)
    }

    override fun onUserListClick(chatModel: ChatModel, position: Int) {
        val action = ChatFragmentDirections.itemUsersList(chatModel)
        Navigation.findNavController(requireView()).navigate(action)
    }

    override fun onJoinClick(chatModel: ChatModel, position: Int) {
        this.position = position
        val action = ChatFragmentDirections.toMessages(chatModel)
        Navigation.findNavController(requireView()).navigate(action)
        Toast.makeText(context, "Вы успешно присоединились в чат", Toast.LENGTH_LONG).show()
        viewModel.runChat(chatModel.id)

        val room = mutableListOf<String>()
        chatModel.chat_room?.let { room.add(it) }

//        try {
//            socket = IO.socket("https://diaspora.direct:3000")
//            socket!!.connect()
//            socket!!.emit("join_group", room)
//            Log.d("success", "socket!!.id()")
//        } catch (e: URISyntaxException) {
//            e.printStackTrace()
//            Log.d("fail", "Failed to connect")
//        }
    }

    override fun onLikeClick(chat_id: Int) {
        viewModel.likePost(chat_id)
    }
}