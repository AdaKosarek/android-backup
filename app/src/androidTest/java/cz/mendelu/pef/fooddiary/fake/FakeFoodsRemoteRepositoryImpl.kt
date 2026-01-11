package cz.mendelu.pef.fooddiary.fake

import cz.mendelu.pef.fooddiary.communication.CommunicationResult
import cz.mendelu.pef.fooddiary.communication.IFoodsRemoteRepository
import cz.mendelu.pef.fooddiary.mock.ServerMock
import cz.mendelu.pef.fooddiary.model.RecipeDetail
import cz.mendelu.pef.fooddiary.model.RecipesListResponse
import javax.inject.Inject

class FakeFoodsRemoteRepositoryImpl @Inject constructor() : IFoodsRemoteRepository {

    override suspend fun getAllRecipes(
        count: Int,
        type: String?
    ): CommunicationResult<RecipesListResponse> {

        return CommunicationResult.Success(
            ServerMock.allRecipes
        )

        /* pro errory
        return CommunicationResult.Error(
            CommunicationError(
                code = 400,
                message = "Failed to load recipes"
            )
        )
        */
    }

    override suspend fun getRecipeById(id: Long): CommunicationResult<RecipeDetail> {

        return CommunicationResult.Success(
            ServerMock.recipeDetail
        )

        /*
        return CommunicationResult.Error(
            CommunicationError(
                code = 404,
                message = "Recipe not found"
            )
        )
        */
    }

    override suspend fun searchRecipes(
        query: String
    ): CommunicationResult<RecipesListResponse> {

        val filtered = ServerMock.allRecipes.results
            ?.filter { recipe ->
                recipe.title?.contains(query, ignoreCase = true) == true
            }
            ?: emptyList()

        return CommunicationResult.Success(
            RecipesListResponse(results = filtered)
        )
    }
}