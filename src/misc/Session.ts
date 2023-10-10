import { navigate } from "svelte-routing";

export default function checkSessionRedirect(){
    if(localStorage.getItem("auth_key") == null) return;
    navigate("/");
}