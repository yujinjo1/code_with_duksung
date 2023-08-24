package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.Fragment2Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding : Fragment2Binding
    lateinit var adapter: MyRetrofitAdapter
    lateinit var reviewList: MutableList<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment2Binding.inflate(inflater, container, false)

        reviewList = mutableListOf<Review>()
        for(i in 1..6)
            reviewList.add(Review("제목"+i.toString(),i,"내용"+i.toString()))

        var manager = LinearLayoutManager(requireContext())

        binding.boardRecyclerView.layoutManager = manager
        adapter = MyRetrofitAdapter(requireContext(), reviewList)
        binding.boardRecyclerView.adapter = adapter

        manager.setReverseLayout(true)
        manager.setStackFromEnd(true)


        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            var title = it.data?.getStringExtra("title")?:""
            var star = it.data?.getIntExtra("star", 0)
            var content = it.data?.getStringExtra("content")?:""
            Log.d("mobileApp", "${title} / $star / $content")

            reviewList.add(Review(title!!, star!!, content!!))
            adapter.notifyDataSetChanged()
            binding.scrollViewReview.fullScroll(ScrollView.FOCUS_UP)
        }

        binding.mainFab.setOnClickListener {
//            if(MyApplication.checkAuth()) {
//                //startActivity(Intent(requireContext(), AddActivity::class.java))
//            }else {
//                Toast.makeText(requireContext(), "인증진행해주세요..", Toast.LENGTH_SHORT).show()
//            }
            var intent = Intent(requireContext(), AddActivity::class.java)
            binding.scrollViewReview.fullScroll(ScrollView.FOCUS_UP)
            requestLauncher.launch(intent)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.scrollViewReview.fullScroll(ScrollView.FOCUS_UP)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}