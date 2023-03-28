package com.example.stockmarketapp.data.repository

import com.example.stockmarketapp.data.local.StockDatabase
import com.example.stockmarketapp.data.mapper.toCompanyListing
import com.example.stockmarketapp.domain.model.CompanyListing
import com.example.stockmarketapp.domain.repository.StockRepository
import com.example.stockmarketapp.util.Resource
import com.opencsv.CSVReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val stockApi: StockApi,
    val database: StockDatabase
) : StockRepository {
    private val dao = database.dao
    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            val isDatabaseEmpty = localListings.isEmpty() && query.isBlank()
            val loadFromCacheOnly = !isDatabaseEmpty && !fetchFromRemote
            if (loadFromCacheOnly) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = stockApi.getListings()

            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Can't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Can't load data"))
            }

        }

    }

}