/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package eu.se_bastiaan.tvnl

import android.app.Activity
import android.os.Bundle
import android.util.Log
import eu.se_bastiaan.tvnl.api.StartApi
import io.reactivex.functions.Consumer

/**
 * Loads [MainFragment].
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        StartApi().pageManager.getAbsolutePage("http://start-api.npo.nl/page/home")
                .subscribe(Consumer {
                    Log.d("MainActivity", it.toString())
                })
    }
}
