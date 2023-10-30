<script lang="ts" context="module">
    export interface PostData {
        id: string,
        username: string,
        title: string,
        description: string,
        ril: number,
        fek: number,
        comments_total: number,
        user_ril: boolean,
        user_fek: boolean,
        image: string,
        user_profile_image: string
    }
</script>
<script lang="ts">
    import Fa from "svelte-fa";
    import { faComment, faEllipsisV, faShare, faThumbsDown, faThumbsUp } from "@fortawesome/free-solid-svg-icons";
    import { url } from "@gql/client";
    import type { FetchResult } from "@apollo/client";
    import { mutation, query, type ReadableQuery } from "svelte-apollo";
    import { FEK, RIL } from "@gql/post";
    import type { User } from "../routes/Profile.svelte";
    import { USER } from "@gql/user";
    import { link } from "svelte-routing";
    export let post: PostData;

    // Ril only
    const sendRil = mutation(RIL);
    let ril = post.ril;
    let user_ril = post.user_ril;

    interface RilResult {
        sendRil: boolean
    }
    async function onRil(){
        if(!checkForAuth()) return;
        try {
            // @ts-ignore
            const rilRes: FetchResult<RilResult> = await sendRil({ variables: { post_id: post.id } });
            const isRil = rilRes.data!!.sendRil!!;
            user_ril = isRil;
            ril = isRil ? ril + 1 : ril - 1; 
        } catch(err){
            console.error(err);
        }
    }


    // Fek only
    const sendFek = mutation(FEK);
    let fek = post.fek;
    let user_fek = post.user_fek;

    interface FekResult {
        sendFek: boolean
    }
    async function onFek(){
        if(!checkForAuth()) return;
        try {
            // @ts-ignore
            const fekRes: FetchResult<FekResult> = await sendFek({ variables: { post_id: post.id } });
            const isFek = fekRes.data!!.sendFek!!;
            user_fek = isFek;
            fek = isFek ? fek + 1 : fek - 1; 
        } catch(err){
            console.error(err);
        }
    }

    function checkForAuth(){
        const auth = localStorage.getItem("auth_key") != null;
        if(!auth){
            alert("You can't ril or fek before login or register!")
        }
        return auth;
    }

    const username = localStorage.getItem("username");
    let profile: string | null = null;
    let user: ReadableQuery<User> = query(USER, { variables: post.username });

    $: $user.data, (() => profile = $user.data?.user.profile_image ? `${url}/images/${$user.data?.user.profile_image}` : "https://picsum.photos/200")();
</script>
<div class="grid grid-cols-1 mx-auto my-10 w-[600px] rounded-xl drop-shadow-2xl bg-[#222]">
    <div class="flex items-center py-4 pr-4">
        <div class="grid grid-cols-2 place-items-center">
            {#if profile != null}
                <a href={"/profile" + (username === post.username ? "" : `/${post.username}`)} use:link>
                    <!-- svelte-ignore a11y-missing-attribute -->
                    <img src={profile}  class="w-[45px] h-[45px] rounded-full border border-white ms-5">
                </a>
            {/if}
            <!-- <Fa class="text-2xl text-white ms-5" icon={faUser}/> -->
            <a href={"/profile" + (username === post.username ? "" : `/${post.username}`)} use:link><span class="text-white text-lg ms-3">{post.username}</span></a>
        </div>
        <!-- <img src="/icon/menu-vertical.png" class="w-[30px] h-[30px] ml-auto"> -->
        <Fa class="text-3xl text-white ms-auto" icon={faEllipsisV}/>
    </div>
    <img src={`${url}/images/${post.image}`} loading="lazy" alt="Post" class="w-full">
    <div class="post-action flex flex-row gap-[4px] mt-4 px-4">
        <!-- <img src="/icon/nyata.png" class="w-[30px] h-[30px]"> -->
        <!-- svelte-ignore a11y-click-events-have-key-events -->
        <div class={"text-2xl " + (user_ril ? "text-green-500" : "text-white")}
        on:click={onRil} role="button" tabindex="0"><Fa icon={faThumbsUp}/></div>
        <span class="text-white text-lg ml-1">{ril}</span>
        <!-- <img src="/icon/nyata.png" class="w-[30px] h-[30px] ms-2 rotate-180"> -->
        <!-- svelte-ignore a11y-click-events-have-key-events -->
        <div class={"text-2xl ms-2 " + (user_fek ? "text-red-500" : "text-white")}
        on:click={onFek} role="button" tabindex="0"><Fa icon={faThumbsDown}/></div>
        <span class="text-white text-lg ml-1">{fek}</span>
        <!-- <img src="/icon/komen.png" class="w-[30px] h-[30px] ms-2"> -->
        <div><Fa class="text-2xl text-white ms-2" icon={faComment}/></div>
        <span class="text-white text-lg ml-1">{post.comments_total}</span>
        <!-- <img src="/icon/share.png" class="w-[30px] h-[30px] ms-2"> -->
        <Fa class="text-2xl text-white ms-2" icon={faShare}/>
    </div>

    <div class="post-detail text-lg w-[610px] px-4 pb-4 mt-4">
        <div class="text-white my-3 text-2xl">{post.title}</div>
        <div class="text-white">{post.description}</div>
        <!-- <div class="text-[#ADD8E6]">Baca selengkapnya...</div> -->
        <div class="flex text-white text-lg mt-5">
            <img src="https://picsum.photos/200" class="w-[50px] h-[50px] rounded-full mr-3">
            <div>
                <div class="font-bold">
                    Username
                </div>
                <div class="font-light">
                    Komen, Lorem ipsum dolor sit amet consectetur adipisicing elit. A, quisquam.
                </div> 
            </div>  
        </div>
    </div>
</div>