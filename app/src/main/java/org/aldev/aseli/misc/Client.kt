package org.aldev.aseli.misc

import com.apollographql.apollo3.ApolloClient

class Client {
    companion object {
        private const val baseUrl = "http://192.168.1.7:8080"
//        private const val url = "http://192.168.100.192:8080/query"
        private const val url = "$baseUrl/query"
        const val imagesUrl = "$baseUrl/images"
        fun setClient(): ApolloClient {
            return ApolloClient.Builder()
                .serverUrl(url)
                .build()
        }
    }
}