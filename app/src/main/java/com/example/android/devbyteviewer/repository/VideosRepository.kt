/*
 * Copyright (C) 2019 Google Inc.
 *gi
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Kemmy MO Jones.
 * Date: Nov 20th, 2022.
 * Topic: Data Persistence (PathWay 2 - Use Room For Data Persistence => Repository Pattern).
 */

package com.example.android.devbyteviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.example.android.devbyteviewer.network.DevByteNetwork
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for fetching dev byte videos from the network and storing them on disk
 */
// TODO: Implement the VideosRepository class
class VideosRepository(private val database: VideosDatabase) {

    //Creating Live Data to Hold A List of The DevByte Videos
    val videos: LiveData<List<DevByteVideo>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    }//end: videos: LiveData

    //Creating a function that refreshes the videos.
    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            val playlist = DevByteNetwork.devbytes.getPlaylist()
            database.videoDao.insertAll(playlist.asDatabaseModel())
        }

    } //end: refreshVideos

}//end: VideosRepository class
