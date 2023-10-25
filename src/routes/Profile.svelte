<script lang="ts" context="module">
    export interface User {
        user: {
            id: string,
            profile_image: string
        }
    }
</script>
<script lang="ts">
    export let search_username: string | undefined = undefined;

    import { clientUpload, url } from "@gql/client";
    import { USER_POSTS } from "@gql/post";
    import { CHANGE_PROFILE_IMAGE, USER } from "@gql/user";
    import Sidebar from "components/Sidebar.svelte";
    import { query, setClient, type ReadableQuery, mutation } from "svelte-apollo";
    import { navigate } from "svelte-routing";

    // @ts-ignore
    setClient(clientUpload);

    interface Posts {
        posts: {
            image: string
        }[]
    }
    const username = search_username ?? localStorage.getItem("username");
    const user: ReadableQuery<User> = query(USER, { variables: { username } });
    const posts: ReadableQuery<Posts> = query(USER_POSTS, { variables: { username } });
    let profile: string | null = null;

    $: $user.data, (() => {
        if(!$user.data) return;

        if($user.data.user.id === "") navigate("/");
        profile = $user.data?.user.profile_image ? `${url}/images/${$user.data?.user.profile_image}` : "https://picsum.photos/200";
    })();

    const toBase64 = (file: File) => new Promise<string | ArrayBuffer | null>((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = reject;
    });

    const mutateProfile = mutation(CHANGE_PROFILE_IMAGE);
    let inputFile: HTMLInputElement
    async function changeProfileImage(){
        if(inputFile.files?.length === 0) return;

        const file = inputFile.files!![0]
        profile = (await toBase64(file)) as string;

        await mutateProfile({
            variables: { image: file }
        });
    }
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

    <div class="w-[325px] h-screen">
        <div class="h-full pl-4 pr-3 pt-10 overflow-y-auto  border-l border-[#656565]">

            <div class="grid grid-cols-1 place-items-center mx-3">
                {#if profile != null}
                    <input bind:this={inputFile} on:change={changeProfileImage} type="file" class="hidden">
                    <!-- svelte-ignore a11y-missing-attribute -->
                    <!-- svelte-ignore a11y-no-noninteractive-element-to-interactive-role -->
                    <!-- svelte-ignore a11y-click-events-have-key-events -->
                    <img role="button" on:click={() => inputFile.click()} src={profile}  class="w-[150px] h-[150px] rounded-full border border-white">
                {/if}
                <div class="text-white text-3xl mt-4">{username}</div>
                <div class="flex w-full justify-between items-center text-white text-center mt-8 text-xl">
                    <div class="grid grid-cols-1">
                        <span class="font-bold">0</span>
                        <span class="text-lg">Pengikut</span>
                    </div>
                    <div class="grid grid-cols-1">
                        <span class="font-bold">0</span>
                        <span class="text-lg">Diikuti</span>
                    </div>
                    <div class="grid grid-cols-1">
                        <span class="font-bold">{$posts.data?.posts.length ?? 0}</span>
                        <span class="text-lg">Postingan</span>
                    </div>
                </div>
                <div class="text-white text-xl text-left mt-10">
                    <div class="font-bold">Bio</div>
                    <div class="text-lg">Lorem ipsum dolor sit amet consectetur adipisicing elit. Unde repudiandae explicabo provident nostrum placeat?</div> <!--dikei max char jo lali :)-->
                </div>
                <div class="flex w-full justify-between mt-10">
                    <button class="bg-[#0E8388] rounded-xl text-white hover:bg-[#2ec3c8] py-3 w-[130px]">Edit Profil</button>
                    <button class="bg-[#0E8388] rounded-xl text-white hover:bg-[#2ec3c8] py-3 w-[130px]">Bagikan Profil</button>
                </div>
                <div class="text-sm text-[#707070] text-center px-3 mt-3 ">
                    <span class="hover:underline cursor-pointer">Persyaratan Layanan</span>
                    <span>.</span>
                    <span class="hover:underline cursor-pointer">Kebijakan Privasi</span>
                    <span>.</span>
                    <span class="hover:underline cursor-pointer">Aksesibilitas</span>
                    <span>.</span>
                    <span class="hover:underline cursor-pointer">Informasi Iklan </span>
                    <span>.</span>
                    <span class="hover:underline cursor-pointer">Kebijakan Penggunaan Cookie</span>
                </div>
                <div class="copyright text-md text-white text-center mt-3">
                    © 2023 aseli
                </div>
            </div>
        </div>
    </div>
</div>