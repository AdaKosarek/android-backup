package cz.mendelu.pef.fooddiary.communication

import cz.mendelu.pef.fooddiary.model.RecipeDetail
import cz.mendelu.pef.fooddiary.model.RecipesListResponse
import javax.inject.Inject


class FoodsRemoteRepositoryImpl @Inject constructor(
    private val api: FoodsAPI
) : IFoodsRemoteRepository {

    override suspend fun getAllRecipes(count: Int, type: String?): CommunicationResult<RecipesListResponse> {
        return processResponse {
            api.getAllRecipes(number = count, type = type)
        }
    }

    override suspend fun getRecipeById(id: Long): CommunicationResult<RecipeDetail> {
        return processResponse {
            api.getRecipeById(id)
        }
    }


}