package com.devrachit.ken.domain.usecases.getUserInfoUsecase
import com.devrachit.ken.data.repository.remote.LeetcodeRepositoryImpl
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.repository.LeetcodeRepository
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: LeetcodeRepository
) {
    operator fun invoke(username: String): Flow<Resource<LeetCodeUserInfo>> = flow {

        try {
            emit(Resource.Loading())
            val userInfo = repository.getUserInfo(username)

            userInfo.data?.let{
                emit(Resource.Success(it))
            }
        }
        catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }
}