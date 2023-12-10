<script lang="ts" context="module">
    export interface PostsResponse {
        posts: PostData[];
    }
</script>

<script lang="ts">
    import { query, setClient, type ReadableQuery } from "svelte-apollo";
    import client from "@gql/client";
    import { POSTS } from "@gql/post";
    import Sidebar from "components/Sidebar.svelte";
    import Post, { type PostData } from "components/Post.svelte";

    // @ts-ignore
    setClient(client);

    const posts: ReadableQuery<PostsResponse> = query(POSTS);
</script>

<div class="w-full flex">
    <Sidebar />
    <div class="konten container h-[100vh] mx-auto overflow-y-scroll w-[900px]">
        {#if $posts.data}
            {#each $posts.data.posts as post}
                <Post {post} refetch={posts.refetch} />
            {/each}
        {/if}
    </div>
    <div class="w-[325px] h-screen">
        <!-- <div class="h-full pl-4 pr-3 pt-12 overflow-y-auto">
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
        </div> -->
    </div>
</div>

<style lang="scss">
    .konten::-webkit-scrollbar {
        display: none;
    }
    .konten {
        -ms-overflow-style: none;
        scrollbar-width: none;
    }
</style>
