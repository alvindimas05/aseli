<script lang="ts" context="module">
    export interface User {
        user: {
            id: string,
            profile_image: string
        }
    }
    export interface Posts {
        posts: {
            image: string
        }[]
    }
</script>
<script lang="ts">
    export let search_username: string | undefined = undefined;

    import { clientUpload, url } from "@gql/client";
    import { USER_POSTS } from "@gql/post";
    import { USER } from "@gql/user";
    import Profile from "components/Profile.svelte";
    import Sidebar from "components/Sidebar.svelte";
    import { query, setClient, type ReadableQuery } from "svelte-apollo";
    import { navigate } from "svelte-routing";

    // @ts-ignore
    setClient(clientUpload);

    const username = search_username ?? localStorage.getItem("username");
    const user: ReadableQuery<User> = query(USER, { variables: { username } });
    const posts: ReadableQuery<Posts> = query(USER_POSTS, { variables: { username } });
    // let profile: string | null = null;

    $: $user.data, (() => {
        if(!$user.data) return;

        if($user.data.user.id === "") navigate("/");
        // profile = $user.data?.user.profile_image ? `${url}/images/${$user.data?.user.profile_image}` : "https://picsum.photos/200";
    })();
</script>
<style>
    @import url('https://fonts.googleapis.com/css2?family=Chango&family=Dancing+Script:wght@600&family=IBM+Plex+Sans:wght@600&family=Poppins:wght@500&family=Quantico&display=swap');

    .profil::-webkit-scrollbar {
        display: none;
    }
    .profil {
        -ms-overflow-style: none;
        scrollbar-width: none;
    }
</style>
<div class="w-full flex">
    <Sidebar/>
    <div class="profil container h-[100vh] mx-auto overflow-y-scroll pb-10 w-[900px]">
       
        <!-- <div class="grid grid-cols-[30%_auto]">
            <img src="icon/profil.png" class="border border-white rounded-full w-[200px] h-[200px] m-auto" alt="profil">
            <div class="profil-detail text-white">
                <h1 class="text-3xl mb-5">abyankuhh</h1>
                    <div class="grid grid-cols-3 text-xl mb-5">
                    <div>
                        <span class="font-bold">123</span>
                        <span>Postingan</span>
                    </div>
                    <div>
                        <span class="font-bold">400</span>
                        <span>Pengikut</span>
                    </div>
                    <div>
                        <span class="font-bold">250</span>
                        <span>Diikuti</span>
                    </div>
                </div>
                <div class="bio grid grid-cols-1 text-lg mb-7">
                    <span>Rajane Devlop</span>
                    <span>Top Singko Momento</span>
                    <span>Reflek Nikahin Zeta</span>
                </div>
                <div class="grid grid-cols-[30%_30%] gap-5">
                    <button class=" bg-[#0E8388] rounded-full text-white hover:bg-[#2ec3c8]">Edit Profil</button>
                    <button class="py-2 bg-[#0E8388] rounded-full text-white">Bagikan Profil</button>
                </div>
            </div>
        </div>
        <div class="container mx-auto mt-16 pt-5 border-t-[1px] border-white flex space-x-[33%] items-center justify-center w-full">
            <div class="border-b-[4px] border-white w-[100px] rounded-b">
                <img src="icon/grid.png" class="pb-3 w-[40px] h-[50px] mx-auto">
            </div>
            <div class="border-b-[4px] border-[#d8d4d4] w-[100px] rounded-b">
                <img src="icon/post-list.png" class="pb-3 w-[40px] h-[50px] mx-auto">
            </div>
        </div> -->
        <div class="container mx-auto mt-10 grid grid-cols-3 gap-2 place-items-center w-[700px]">
            {#if $posts.data}
                {#each $posts.data.posts as post}
                    <img class="aspect-square object-cover h-full" src={`${url}/images/${post.image}`} alt="konten">
                {/each}
            {/if}
        </div>
    </div>
    <Profile/>
</div>