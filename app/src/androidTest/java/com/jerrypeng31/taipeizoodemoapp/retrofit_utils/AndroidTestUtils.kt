package com.jerrypeng31.taipeizoodemoapp.retrofit_utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class AndroidTestUtils {
    companion object{
        fun loadTestData(data: String): String {
            val stream: InputStream = javaClass.classLoader.getResourceAsStream(data)
            val reader = BufferedReader(InputStreamReader(stream))
            val builder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                builder.append(String.format("%s", line))
            }
            reader.close()
            return builder.toString()
        }
    }
}