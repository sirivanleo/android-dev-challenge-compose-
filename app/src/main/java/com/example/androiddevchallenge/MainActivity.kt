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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.chrisbanes.accompanist.coil.CoilImage
import okio.buffer
import okio.source

object PuppyData {
    var items = mutableListOf<PuppyEntity>()
}

class MainActivity : AppCompatActivity() {
    fun initPuppyData() {
        val listType = object : TypeToken<List<PuppyEntity>>() {}.type
        val puppyData: List<PuppyEntity> =
            assets.open("puppies.json").source().buffer().readUtf8()
                .let { Gson().fromJson(it, listType) }
        PuppyData.items.clear()
        PuppyData.items.addAll(puppyData)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPuppyData()
        setContent {
            MyTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            Text("Adopt a Pupper")
                        })
                    }
                ) {
                    navigationRoot()
                }
            }
        }
    }
}

@Composable
fun navigationRoot() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "puppyList") {
        composable("puppyList") { PuppyList(navController) }
        composable(
            "puppy/{puppyId}",
            arguments = listOf(navArgument("puppyId") { type = NavType.IntType })
        ) { backStackEntry ->
            PuppyDetail(
                navController,
                backStackEntry.arguments?.getInt("puppyId")
            )
        }
    }
}

// Start building your app here!
@Composable
fun PuppyList(navController: NavController) {
    Surface(color = MaterialTheme.colors.background) {
        val stateList = rememberLazyListState()
        LazyColumn(
            state = stateList,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(PuppyData.items) { puppyEntity ->
                PuppyCard(puppyEntity.name, puppyEntity.gender, puppyEntity.img) {
                    navController.navigate("puppy/${puppyEntity.id}")
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
            }
        }

    }
}

@Composable
fun PuppyCard(name: String, gender: String, img: String, clickListener: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .clickable(onClick = clickListener)
    ) {
        Column {
            CoilImage(
                data = img, contentDescription = "Puppy Image", fadeIn = true,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth, alignment = Alignment.TopCenter
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1F, true)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp)
                    )
                    Text(
                        text = gender,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 8.dp)
                    )
                }
                Icon(Icons.Outlined.Pets, "pets icon", modifier = Modifier.padding(end = 16.dp))
            }
        }
    }
}

@Composable
fun PuppyDetail(navController: NavController, puppyId: Int?) {
    val puppyEntity = PuppyData.items.filter { entry -> entry.id == puppyId }.first()
    Column() {
        CoilImage(
            data = puppyEntity.img,
            contentDescription = "Puppy Image",
            fadeIn = true,
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(4.dp))
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    puppyEntity.name,
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.h5
                )
                Text(puppyEntity.age, style = MaterialTheme.typography.subtitle2)
            }
            Spacer(Modifier.height(4.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    puppyEntity.gender,
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.subtitle2
                )
                Text(puppyEntity.breed)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                puppyEntity.bio,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

//@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    PuppyData.items.add(
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
    MyTheme(darkTheme = true) {
        navigationRoot()
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    PuppyData.items.add(
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
    MyTheme {
        navigationRoot()
    }
}

@Preview("Detail", widthDp = 360, heightDp = 640)
@Composable()
fun DetailPreview() {
    PuppyData.items.add(
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
    MyTheme {
        val navController = rememberNavController()
        PuppyDetail(navController = navController, puppyId = 1)
    }
}