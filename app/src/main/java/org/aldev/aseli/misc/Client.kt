package org.aldev.aseli.misc

import com.apollographql.apollo3.ApolloClient

class Client {
    companion object {
        private const val baseUrl = "http://192.168.1.8:8080"
        private const val url = "$baseUrl/query"
        const val imagesUrl = "$baseUrl/images"
        fun setClient(authKey: String? = null): ApolloClient {
            var client = ApolloClient.Builder().serverUrl(url)
            if(authKey != null) {
                client = client.addHttpHeader("Auth-Key", authKey)
            }
            return client.build()
        }
    }
}