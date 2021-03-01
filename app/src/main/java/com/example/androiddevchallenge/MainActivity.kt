/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.imageLoader
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.chrisbanes.accompanist.coil.CoilImage
import okio.buffer
import okio.source

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listType = object : TypeToken<List<PuppyEntity>>() {}.type
        val puppyData: List<PuppyEntity> =
            assets.open("puppies.json").source().buffer().readUtf8()
                .let { Gson().fromJson(it, listType) }
        val imageLoader = this.imageLoader
        setContent {
            MyTheme {
                PuppyList(puppyData)
            }
        }
    }
}

// Start building your app here!
@Composable
fun PuppyList(puppyData: List<PuppyEntity>) {
    Surface(color = MaterialTheme.colors.background) {
        val stateList = rememberLazyListState()
        LazyColumn(
            state = stateList,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
        ) {
            items(puppyData) { puppyEntity ->
                PuppyCard(puppyEntity.name, puppyEntity.img, puppyEntity.id)
            }
        }

    }
}


@Composable
fun PuppyCard(name: String, img: String, id: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
        CoilImage(
            data = img, contentDescription = "Puppy Image", fadeIn = true,
            modifier = Modifier.size(120.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(text = name, style = MaterialTheme.typography.subtitle1)
    }
}

//@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        PuppyList(
            arrayListOf(
                PuppyEntity(
                    id = 1,
                    name = "Max",
                    img = "",
                    age = "2m",
                    bio = "",
                    breed = "",
                    gender = "Male",
                    weight = "1lb"
                )
            )
        )
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        PuppyList(
            arrayListOf(
                PuppyEntity(
                    id = 1,
                    name = "Max",
                    img = "",
                    age = "2m",
                    bio = "",
                    breed = "",
                    gender = "Male",
                    weight = "1lb"
                )
            )
        )
    }
}