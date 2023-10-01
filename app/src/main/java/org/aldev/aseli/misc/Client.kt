package org.aldev.aseli.misc

import android.util.Log
import com.apollographql.apollo3.ApolloClient

class Client {
    companion object {
        private const val baseUrl = "http://192.168.1.7:8080"
//        private const val url = "http://192.168.100.192:8080/query"
        private const val url = "$baseUrl/query"
        const val imagesUrl = "$baseUrl/images"
        fun setClient(authKey: String? = null): ApolloClient {
            var client = ApolloClient.Builder().serverUrl(url)
            if(authKey != null) {
                Log.d("Auth Key", authKey)
                client = client.addHttpHeader("Auth-Key", authKey)
            }
            return client.build()
        }
    }
}