package cz.petstore2025

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class MyHiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        // Tady musim zmenit na to HiltTestApplication
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}