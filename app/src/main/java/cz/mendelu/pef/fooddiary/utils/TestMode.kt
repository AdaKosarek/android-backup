package cz.mendelu.pef.fooddiary.utils

object TestMode {
    val isRunningTest: Boolean
        get() = try {
            Class.forName("androidx.test.espresso.Espresso")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
}
