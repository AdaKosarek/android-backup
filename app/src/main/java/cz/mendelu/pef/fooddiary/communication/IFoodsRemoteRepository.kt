package cz.mendelu.pef.fooddiary.communication

import cz.mendelu.pef.fooddiary.model.RecipeDetail
import cz.mendelu.pef.fooddiary.model.RecipesListResponse


interface IFoodsRemoteRepository : IBaseRemoteRepository {
    suspend fun getAllRecipes(count: Int = 50, type: String? = null): CommunicationResult<RecipesListResponse>
    suspend fun getRecipeById(id: Long): CommunicationResult<RecipeDetail>
}