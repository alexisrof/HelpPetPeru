package com.example.helppetperu;

import android.content.Context;
import org.junit.Test;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        //Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        //assertEquals("com.example.helppetperu", appContext.getPackageName());
    }
}
