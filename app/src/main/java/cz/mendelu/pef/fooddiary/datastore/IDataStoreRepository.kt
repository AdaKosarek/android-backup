package cz.mendelu.pef.fooddiary.datastore

interface IDataStoreRepository {
    suspend fun setFirstRun()
    suspend fun getFirstRun(): Boolean
}