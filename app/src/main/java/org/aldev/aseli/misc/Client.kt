package org.aldev.aseli.misc

import com.apollographql.apollo3.ApolloClient

class Client {
    companion object {
        private const val url = "http://192.168.100.192:8080/query"
        fun setClient(): ApolloClient {
            return ApolloClient.Builder()
                .serverUrl(url)
                .build()
        }
    }
}