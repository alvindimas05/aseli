import { ApolloClient, HttpLink, InMemoryCache } from "@apollo/client/core";
import { createUploadLink } from "apollo-upload-client";

const url = "http://localhost:8080";
const httpLink = new HttpLink({
    uri: url + "/query",
    headers: {
        "Auth-Key": localStorage.getItem("auth_key") || ""
    }
});

const uploadLink = createUploadLink({
    uri: url + "/query",
    headers: {
        "Auth-Key": localStorage.getItem("auth_key") || ""
    }
});

const client = new ApolloClient({
    cache: new InMemoryCache(),
    link: httpLink
});

const clientUpload = new ApolloClient({
    cache: new InMemoryCache(),
    link: uploadLink
});

export { url, clientUpload };
export default client;