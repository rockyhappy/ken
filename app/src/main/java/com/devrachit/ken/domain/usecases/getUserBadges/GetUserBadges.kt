package com.devrachit.ken.domain.usecases.getUserBadges

import android.util.Log
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.userAgent
import javax.inject.Inject

class GetUserBadgesUseCase  @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy,
    private val networkManager: NetworkManager) {
    operator fun invoke(username :String) :Flow<Resource<Any?>> = flow{
        emit(Resource.Loading())
        try{
            val response= remoteRepository.fetchUserBadges(username = username )
            emit(Resource.Success(response))
            Log.d("GetUserBadgesUseCase", "invoke: $response")
        }catch (e:Exception){
            emit(Resource.Error(e.message?:"Unknown Error"))
        }

    }
}