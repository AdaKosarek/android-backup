package cz.mendelu.pef.fooddiary.communication

import cz.mendelu.pef.fooddiary.model.ApiRecipe
import cz.mendelu.pef.fooddiary.model.RecipesListResponse
import javax.inject.Inject


class FoodsRemoteRepositoryImpl @Inject constructor(
    private val api: FoodsAPI
) : IFoodsRemoteRepository {

    override suspend fun getAllRecipes(count: Int): CommunicationResult<RecipesListResponse> {
        return processResponse {
            api.getAllRecipes(count)
        }
    }

    override suspend fun getRecipeById(id: Long): CommunicationResult<ApiRecipe> {
        return processResponse {
            api.getRecipeById(id)
        }
    }
}