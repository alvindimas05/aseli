import { gql } from "@apollo/client";

export const POSTS = gql`
query {
	posts {
        id username title description ril fek comments_total user_ril user_fek image
	}
}`;