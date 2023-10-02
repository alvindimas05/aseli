import { gql } from "@apollo/client";

export const LOGIN = gql`
mutation LoginUser($username: String!, $password: String!){
	loginUser(input: { username: $username, password: $password }){
		success auth_key
	}
}`;

export const REGISTER = gql`
mutation RegisterUser($username: String!, $password: String!, $verification_password: String!){
	registerUser(input: {username: $username, password: $password, verification_password: $verification_password }){
		success reason auth_key
	}
}`;