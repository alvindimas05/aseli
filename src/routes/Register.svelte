<script lang="ts">
	import { setClient, mutation } from "svelte-apollo";
    import { REGISTER } from "@gql/user";
    import client from "@gql/client";
    import type { FetchResult } from "@apollo/client";
    import { Link } from "svelte-routing";

	setClient(client);

    interface RegisterResponse {
        registerUser: {
            success: boolean,
            reason: string,
            auth_key: string,
        }
    }
    
    const registerUser = mutation(REGISTER);
    let registerSuccess = false;
    let failedReason: String = "";

    async function register(e: SubmitEvent & {
        currentTarget: EventTarget & HTMLFormElement;
    }){
        try {
            const data = new FormData(e.currentTarget);
            if(data.get("password") != data.get("verification_password")){
                registerSuccess = false;
                failedReason = "verification password";
                return
            }

            const res: FetchResult<RegisterResponse> = await registerUser({ variables: {
                username: data.get("username"),
                password: data.get("password"),
                verification_password: data.get("verification_password")
            }});
            
            registerSuccess = res.data!.registerUser.success;
            failedReason = res.data!.registerUser.reason;
            console.log(res.data!.registerUser.reason);
        } catch(err){
            console.log(err);
        }
    }
</script>

<div class="w-full h-[100vh]">
    <section class="items-center flex justify-center relative w-full h-screen">
        <div class="flex max-w-3xl p-20 px-20 items-center border border-white rounded-l">
            <div class="">
                <h1 class="font-semibold text-7xl text-white text-center">aseli</h1>
                <p class="text-sm mt-5 mb-5 text-yellow-400 text-center">
                <!-- svelte-ignore empty-block -->
                {#if failedReason == "verification password"}
                    Verification password failed!
                {:else if failedReason == "username used"}
                    Username already used!
                {:else}
                {/if}
                </p>
                <form on:submit|preventDefault={register}  class="flex flex-col gap-4">
                    <input class="text-white bg-transparent rounded-lg p-2 border border-[#0E8388]" type="text" name="username" placeholder="Buat username">
                    <!-- <input class="text-white bg-transparent rounded-lg p-2 border border-[#0E8388]" type="password" name="email" placeholder="Emailmu apa"> -->
                    <input class="text-white bg-transparent rounded-lg p-2 border border-[#0E8388]" type="password" name="password" placeholder="Buat password">
                    <input class="text-white bg-transparent rounded-lg p-2 border border-[#0E8388]" type="password" name="verification_password" placeholder="Konfirmasi password">
                    <!-- <label class="p-2 text-white inline-flex items-center text-sm">
                        <input class="mr-2" type="checkbox">
                        Setujuin persyaratan
                    </label> -->
                    <button type="submit" class="bg-[#0E8388] rounded-lg text-white py-2 hover:bg-cyan-500 mt-5">Register</button>
                    <div class=" mt-5 text-center text-white">
                        <span>Punya akun?</span>
                        <Link to="/login" class="w-1/2 text-cyan-500 hover:underline">Minimal masuk</Link>
                    </div>
                </form>
            </div>
        </div>
    </section>
</div>