package cz.mendelu.pef.fooddiary.ui.screens.saveddetail

interface SavedDetailActions {
    fun onDeleteMeal()
    fun onToggleFavorite()

    //edit
    fun onToggleEdit()
    fun onEditNameChange(value: String)
    fun onEditNoteChange(value: String)
    fun onEditPlaceNameChange(value: String)
}
