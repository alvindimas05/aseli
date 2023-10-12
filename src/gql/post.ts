import { gql } from "@apollo/client";

export const POSTS = gql`
query {
    posts {
        id username title description ril fek user_ril user_fek image comments_total
    }
}`;

export const RIL = gql`
mutation SendRil($post_id: String!){
    sendRil(post_id: $post_id)
}`;

export const FEK = gql`
mutation SendFek($post_id: String!){
    sendFek(post_id: $post_id)
}`;

export const USER_POSTS = gql`
query Posts($username: String!) {
    posts(filter: { username: $username }) {
        image
    }
}`;