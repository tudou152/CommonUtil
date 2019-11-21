package com.lliujun.commonutil

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.lliujun.utils.extensions.android.preferences.BitmapPreference
import com.lliujun.utils.extensions.android.preferences.ObjectPreference
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val prefs: Lazy<SharedPreferences> = lazy {
        InstrumentationRegistry.getInstrumentation().targetContext.getSharedPreferences(
            "hehe",
            Context.MODE_PRIVATE
        )
    }

    private var user: User? by ObjectPreference(prefs, "user", null)
    private var user1: User? by ObjectPreference(prefs, "user1", null)
    private var bitmap: Bitmap? by BitmapPreference(prefs, "bitmap", null)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.lliujun.commonutil", appContext.packageName)
    }

    @Test
    fun testUser() {

        assertNull(user)
        assertNull(user1)

        Thread.sleep(1000)
        user = null
        user1 = User("王五", 19)

        Thread.sleep(2000L)

        assertNotNull(user1)
        assertEquals(user1?.name, "王五")
        assertEquals(user1?.age, 19)
    }
}
