<script lang="ts">
	import { setClient, mutation } from "svelte-apollo";
    import { LOGIN } from "@gql/user";
    import client from "@gql/client";
    import type { FetchResult } from "@apollo/client";
    import { Link, link, navigate } from "svelte-routing";
    import checkSessionRedirect from "misc/Session";
    import { onMount } from "svelte";

    onMount(checkSessionRedirect);
	setClient(client);

    interface LoginResponse {
        loginUser: {
            success: boolean,
            auth_key: string
        }
    }
    
    const loginUser = mutation(LOGIN);
    let loginSuccess: boolean | null = null

    async function login(e: SubmitEvent & {
        currentTarget: EventTarget & HTMLFormElement;
    }){
        try {
            const data = new FormData(e.currentTarget);
            const username = data.get("username")
            const res: FetchResult<LoginResponse> = await loginUser({ variables: {
                username: data.get("username"),
                password: data.get("password")
            }});
            loginSuccess = res.data!.loginUser.success;
            if(loginSuccess){
                localStorage.setItem("auth_key", res.data!.loginUser.auth_key);
                localStorage.setItem("username", username as string);
                navigate("/");
            }
            return loginSuccess;
        } catch(err){
            console.log(err);
            return false;
        }
    }
</script>
<style lang="scss">
    @import url('https://fonts.googleapis.com/css2?family=Chango&family=Dancing+Script:wght@600&family=IBM+Plex+Sans:wght@600&family=Poppins:wght@500&family=Quantico&display=swap');
    #logo {
        font-family: 'Quantico';
    }
</style>
<div class="w-full h-[100vh]">
    <section class="items-center flex justify-center relative w-full h-screen md:text-lg">
        <div class="flex max-w-3xl p-20 items-center rounded-lg md:border md:border-white">
            <div>
                <a href="/" use:link><h1 class="font-semibold text-7xl text-white text-center mb-3" id="logo">aseli</h1></a>
                <p class="text-sm text-yellow-400 text-center mb-3" class:hidden={loginSuccess != false}>Wrong username or password!</p>

                <form on:submit|preventDefault={login}  class="flex flex-col gap-4">
                    <input class="text-white bg-transparent rounded-lg p-2 border border-[#0E8388]" type="text" name="username" placeholder="Username">
                    <input class="text-white bg-transparent rounded-lg p-2 border border-[#0E8388]" type="password" name="password" placeholder="Password">
                    <!-- <label class="p-2 text-white inline-flex items-center">
                        <input class="mr-2 rounded-3xl" type="checkbox" checked >
                        Ingatin
                    </label> -->
                    <p class="p-2 text-right text-cyan-500 hover:text-cyan-300">Forgot Password?</p>
                    <button class="bg-[#0E8388] rounded-lg text-white py-2 hover:bg-cyan-500">Login</button>
                    <div class=" mt-5 text-center text-white">
                        <span>Gak punya akun?</span>
                        <Link to="/register"  class="w-1/2 text-cyan-500 hover:underline">Buat dong</Link>
                    </div>
                </form>
            </div>
        </div>
    </section>
</div>