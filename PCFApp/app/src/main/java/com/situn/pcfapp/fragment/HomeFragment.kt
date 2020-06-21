package com.situn.pcfapp.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.situn.imagessubredditviewer.app.services.ApiClient.client
import com.situn.imagessubredditviewer.app.services.ApiInterface
import com.situn.pcfapp.R
import com.situn.pcfapp.adapter.HomeLoadAdapter
import com.situn.pcfapp.database.AppDatabase
import com.situn.pcfapp.database.User
import com.situn.pcfapp.databinding.FragmentHomeBinding
import com.situn.pcfapp.model.HomeModel
import com.situn.pcfapp.model.HomeResponseModel
import com.situn.pcfapp.utils.Utility
import com.situn.pcfapp.utils.Utility.isOnline
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var adapter: HomeLoadAdapter
    private lateinit var contexts : Context
    private var homeModelList = ArrayList<HomeModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val fragmentHomeBinding : FragmentHomeBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        val view : View = fragmentHomeBinding.root

        initRecycler(fragmentHomeBinding);
        fetchData(fragmentHomeBinding);
        return view;
    }

    //Initialize recyclerview
    private fun initRecycler(fragmentHomeBinding: FragmentHomeBinding) {
        fragmentHomeBinding.rvHome.layoutManager = LinearLayoutManager(contexts, RecyclerView.VERTICAL, false)
        homeModelList = ArrayList<HomeModel>()
        adapter = HomeLoadAdapter(homeModelList)
        fragmentHomeBinding.rvHome.adapter = adapter
    }

    //Fetch data
    @RequiresApi(Build.VERSION_CODES.M)
    private fun fetchData(fragmentHomeBinding: FragmentHomeBinding) {
        if (isOnline(contexts))
            fetchHomeDataList(fragmentHomeBinding)
        else
            fetchHomeDataFromDB(fragmentHomeBinding)
    }

    private fun fetchHomeDataList(fragmentHomeBinding: FragmentHomeBinding) {
        Utility.showProgressDialog(contexts)
        val apiService =
            client!!.create(ApiInterface::class.java)

        val call: Call<List<HomeResponseModel>> = apiService.getHomeData()
        call.enqueue(object : Callback<List<HomeResponseModel>> {
            override fun onResponse(call: Call<List<HomeResponseModel>>, response: Response<List<HomeResponseModel>>) {

                Utility.hideProgressDialog()

                fragmentHomeBinding.rvHome.setVisibility(View.VISIBLE)
                fragmentHomeBinding.tvNoData.setVisibility(View.GONE)
                for (i in 0 ..(response.body()?.size!! - 1)) {
                    homeModelList.add(
                        HomeModel(
                            response.body()?.get(i)?.id.toString().toInt(),
                            response.body()?.get(i)?.name.toString(),
                            response.body()?.get(i)?.fullName.toString(),
                            response.body()?.get(i)?.owner?.avatarUrl.toString(),
                            response.body()?.get(i)?.owner?.url.toString()
                        )
                    )
                }

                saveToDb(homeModelList)
                adapter.notifyDataSetChanged()

            }

            override fun onFailure(call: Call<List<HomeResponseModel>>, t: Throwable) {
                fragmentHomeBinding.rvHome.setVisibility(View.GONE)
                fragmentHomeBinding.tvNoData.setVisibility(View.VISIBLE)
                Utility.hideProgressDialog()
            }
        })
    }

    private fun saveToDb(homeModelList: ArrayList<HomeModel>) {
        val db = Room.databaseBuilder(contexts, AppDatabase::class.java, "UserDB").allowMainThreadQueries().build()

        for (i in 0..(homeModelList.size - 1)) {
            val user = User(
                i+1, homeModelList.get(i).id.toString(),
                homeModelList.get(i).name,
                homeModelList.get(i).full_name,
                homeModelList.get(i).image_avatar,
                homeModelList.get(i).url
            )

            if (db.userDao().findUserWithId(homeModelList.get(i).id.toString().toInt()).size == 0) {
                db.userDao().insertAll(user)
            }
            else {
                db.userDao().updateUsers(user)
            }

        }
    }


    private fun fetchHomeDataFromDB(fragmentHomeBinding: FragmentHomeBinding) {
        val db = Room.databaseBuilder(contexts, AppDatabase::class.java, "UserDB").allowMainThreadQueries().build()

        val userListDB : List<User>
        userListDB = db.userDao().getAll();

        if (userListDB.size > 0) {
            fragmentHomeBinding.rvHome.setVisibility(View.VISIBLE)
            fragmentHomeBinding.tvNoData.setVisibility(View.GONE)
            for (i in 0..(userListDB.size - 1)) {
                homeModelList.add(
                    HomeModel(
                        userListDB.get(i).id.toString().toInt(),
                        userListDB.get(i).name.toString(),
                        userListDB.get(i).fullName.toString(),
                        userListDB.get(i).imageUrl.toString(),
                        userListDB.get(i).url.toString()
                    )
                )
            }
        }
        else{
            fragmentHomeBinding.rvHome.setVisibility(View.GONE)
            fragmentHomeBinding.tvNoData.setVisibility(View.VISIBLE)
        }
        adapter.notifyDataSetChanged()
    }
}