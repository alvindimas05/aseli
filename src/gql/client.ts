import { ApolloClient, HttpLink, InMemoryCache } from "@apollo/client/core";
import { loadErrorMessages, loadDevMessages } from "@apollo/client/dev";

const httpLink = new HttpLink({
    uri: "http://localhost:8080/query",
    credentials: "include"
})

const client = new ApolloClient({
    cache: new InMemoryCache(),
    link: httpLink
});

loadDevMessages();  
loadErrorMessages();

export default client;