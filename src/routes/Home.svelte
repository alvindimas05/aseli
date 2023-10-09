<script lang="ts">
    import Fa from "svelte-fa";
	import { query, setClient, type ReadableQuery } from "svelte-apollo";
    import client, { url } from "@gql/client";
    import { faComment, faEllipsisV, faShare, faThumbsDown, faThumbsUp, faUser } from "@fortawesome/free-solid-svg-icons";
    import { POSTS } from "@gql/post";
    import Sidebar from "components/Sidebar.svelte";

    setClient(client);

    interface PostsResponse {
        posts: {
            id: string,
            username: string,
            title: string,
            description: string,
            ril: number,
            fek: number,
            comments_total: number,
            user_ril: boolean,
            user_fek: boolean,
            image: string
        }[]
    }

    const posts: ReadableQuery<PostsResponse> = query(POSTS);
</script>
<style lang="scss">
    .konten::-webkit-scrollbar {
        display: none;
    }
    .konten {
        -ms-overflow-style: none;
        scrollbar-width: none;
    }
</style>
<div class="w-full flex">
    <Sidebar/>
    <div class="konten container h-[100vh] mx-auto overflow-y-scroll w-[900px]">
        {#if $posts.data}
            {#each $posts.data.posts as post}
                <div class="grid grid-cols-1 mx-auto my-10 w-[600px] rounded-xl drop-shadow-2xl bg-[#222]">
                    <div class="flex items-center py-4 pr-4">
                        <div class="grid grid-cols-2 place-items-center">
                            <!-- <img src="/icon/profil.png" class="w-[45px] h-[45px] rounded-full border border-white"> -->
                            <Fa class="text-2xl text-white ms-5" icon={faUser}/>
                            <span class="text-white text-lg ms-3">{post.username}</span>
                        </div>
                        <!-- <img src="/icon/menu-vertical.png" class="w-[30px] h-[30px] ml-auto"> -->
                        <Fa class="text-3xl text-white ms-auto" icon={faEllipsisV}/>
                    </div>
                    <img src={`${url}/images/${post.image}`} alt="Post"  class="w-full">
        
                    <div class="post-action flex flex-row gap-[4px] mt-4 px-4">
                        <!-- <img src="/icon/nyata.png" class="w-[30px] h-[30px]"> -->
                        <Fa class="text-2xl text-white" icon={faThumbsUp}/>
                        <span class="text-white text-lg ml-1">{post.ril}</span>
                        <!-- <img src="/icon/nyata.png" class="w-[30px] h-[30px] ms-2 rotate-180"> -->
                        <Fa class="text-2xl text-white ms-2" icon={faThumbsDown}/>
                        <span class="text-white text-lg ml-1">{post.fek}</span>
                        <!-- <img src="/icon/komen.png" class="w-[30px] h-[30px] ms-2"> -->
                        <Fa class="text-2xl text-white ms-2" icon={faComment}/>
                        <span class="text-white text-lg ml-1">{post.comments_total}</span>
                        <!-- <img src="/icon/share.png" class="w-[30px] h-[30px] ms-2"> -->
                        <Fa class="text-2xl text-white ms-2" icon={faShare}/>
                    </div>
            
                    <div class="post-detail text-lg w-[610px] px-4 pb-4 mt-4">
                        <div class="text-white my-3 text-2xl">{post.title}</div>
                        <div class="text-white">{post.description}</div>
                        <!-- <div class="text-[#ADD8E6]">Baca selengkapnya...</div> -->
                        <!-- <div class="text-[#d5d5d5] mt-7">Lihat semua 123 komen</div> -->
                    </div>
                </div>
            {/each}
        {/if}
    </div>
    <div class="w-[325px] h-screen">
        <div class="h-full pl-4 pr-3 pt-12 overflow-y-auto">
            <div class="grid grid-cols-1 text-white rounded-xl bg-[#1F1913] pt-4">
                <span class="text-xl mx-4 my-2">Trending</span>
                <div class="grid grid-cols-1 hover:bg-[#4F4943] my-2 px-4">
                    <div class="text-[#707070]">Meme</div>
                    <div class="text-xl">Top singko orang hitam</div>
                    <div class="text-[#707070]">180 postingan</div>
                </div>
                <div class="grid grid-cols-1 hover:bg-[#4F4943] my-2 px-4">
                    <div class="text-[#707070]">Ingfo</div>
                    <div class="text-xl">Disaat bapak hitam ini melihat hpnya gaiss</div>
                    <div class="text-[#707070]">217 postingan</div>
                </div>
                <div class="grid grid-cols-1 hover:bg-[#4F4943] my-2 px-4">
                    <div class="text-[#707070]">Tumtor</div>
                    <div class="text-xl">Tutorial menikahi vtuber</div>
                    <div class="text-[#707070]">180 postingan</div>
                </div>
                <div class="grid grid-cols-1 hover:bg-[#4F4943] my-2 px-4">
                    <div class="text-[#707070]">Vingral</div>
                    <div class="text-xl">Cipung ternyata lebih suka Ronaldo daripada Messi?</div>
                    <div class="text-[#707070]">180 postingan</div>
                </div>
                <div class="text-lg text-[#4488ee] py-3 px-4 hover:bg-[#4F4943] rounded-t rounded-xl">Tampilkan lebih banyak</div>
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