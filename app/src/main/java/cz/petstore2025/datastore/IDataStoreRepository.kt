package cz.petstore2025.datastore

interface IDataStoreRepository {
    suspend fun setLoginSuccessful()
    suspend fun getLoginSuccessful(): Boolean
}