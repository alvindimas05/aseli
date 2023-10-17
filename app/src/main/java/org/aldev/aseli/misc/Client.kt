package org.aldev.aseli.misc

import com.apollographql.apollo3.ApolloClient

class Client {
    companion object {
        private const val baseUrl = "http://192.168.1.5:8080"
        private const val url = "$baseUrl/query"
        const val imagesUrl = "$baseUrl/images"
        const val randomImageUrl = "https://picsum.photos/200"
        fun setClient(authKey: String? = null): ApolloClient {
            var client = ApolloClient.Builder().serverUrl(url)
            if(authKey != null) {
                client = client.addHttpHeader("Auth-Key", authKey)
            }
            return client.build()
        }
    }
}