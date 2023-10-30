<script lang="ts">
    import { clientUpload, url } from "@gql/client";
    import { USER_POSTS } from "@gql/post";
    import { CHANGE_PROFILE_IMAGE, USER } from "@gql/user";
    import type { Posts, User } from "@routes/Profile.svelte";
    import { type ReadableQuery, mutation, query, setClient } from "svelte-apollo";
    import { navigate } from "svelte-routing";

    // @ts-ignore
    setClient(clientUpload);

    const username = localStorage.getItem("username");
    const user: ReadableQuery<User> = query(USER, { variables: { username } });
    let profile: string | null;
    let posts: ReadableQuery<Posts> = query(USER_POSTS, { variables: { username } });;
    
    $: $user.data, (() => {
        if(!$user.data) return;

        if($user.data.user.id === "") navigate("/");
        profile = !profile && $user.data?.user.profile_image ? `${url}/images/${$user.data?.user.profile_image}` : "https://picsum.photos/200";
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
    };

</script>
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