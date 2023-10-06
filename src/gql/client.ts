import { ApolloClient, HttpLink, InMemoryCache } from "@apollo/client/core";
import { loadErrorMessages, loadDevMessages } from "@apollo/client/dev";

const url = "http://localhost:8080";
const httpLink = new HttpLink({
    uri: url + "/query",
    headers: {
        "Auth-Key": localStorage.getItem("auth_key") || ""
    }
})

const client = new ApolloClient({
    cache: new InMemoryCache(),
    link: httpLink
});

loadDevMessages();  
loadErrorMessages();

export { url };
export default client;