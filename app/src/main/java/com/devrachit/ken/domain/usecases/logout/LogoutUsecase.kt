package com.devrachit.ken.domain.usecases.logout

import android.provider.ContactsContract
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class LogoutUseCase @Inject constructor(
    private val dataStoteRepository: DataStoreRepository,
    private val localRepository: LeetcodeLocalRepository,

) {
    operator fun invoke(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
//            localRepository.clearCache()
            dataStoteRepository.clearPrimaryUsername()
            emit(Resource.Success(true))
        }
        catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }
}