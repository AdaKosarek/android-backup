package cz.mendelu.pef.fooddiary.communication

import cz.mendelu.pef.fooddiary.model.ApiRecipe
import cz.mendelu.pef.fooddiary.model.RecipesListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface FoodsAPI {
    @GET("recipes/complexSearch")
    suspend fun getAllRecipes(
        @Query("number") number: Int = 50
    ): Response<RecipesListResponse>

    @GET("recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") recipeId: Long,
        @Query("includeNutrition") includeNutrition: Boolean = true
    ): Response<ApiRecipe>
}