<script lang="ts">
    import Sidebar from "components/Sidebar.svelte";
    import Profile from "components/Profile.svelte";
    import { CREATE_POST } from "@gql/post";
    import type { FetchResult } from "@apollo/client";
    import { mutation, setClient } from "svelte-apollo";
    import { clientUpload } from "@gql/client";
    import { navigate } from "svelte-routing";

    // @ts-ignore
    setClient(clientUpload);

    interface CreatePostResponse {
        createPost: {
            post_id: string;
        };
    }

    const toBase64 = (file: File) =>
        new Promise<string | ArrayBuffer | null>((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => resolve(reader.result);
            reader.onerror = reject;
        });

    let postImage: string | null = null;
    let inputFile: HTMLInputElement;
    let title: string;
    let description: string;
    async function changePostImage() {
        if (inputFile.files?.length === 0) return;

        const file = inputFile.files!![0];
        postImage = (await toBase64(file)) as string;
    }

    const createPostMutation = mutation(CREATE_POST);
    async function createPost() {
        if (postImage == null) return alert("Image required!");
        if (title == "" || description == "")
            return alert("Input can't be empty!");
        try {
            const image = inputFile.files!![0];
            await createPostMutation({
                variables: {
                    title,
                    description,
                    image,
                },
            });
            navigate("/");
        } catch (err) {
            console.error(err);
        }
    }
</script>

<div class="w-full flex">
    <Sidebar />
    <div
        id="post-preview"
        class="container h-[100vh] mx-auto overflow-y-scroll pb-10 w-[900px]"
    >
        <div class="grid grid-cols-1 place-items-center">
            <span class="text-3xl text-white font-semibold text-center mt-10">
                Otewe Posting
            </span>
            <!-- svelte-ignore a11y-missing-attribute -->
            <!-- svelte-ignore a11y-click-events-have-key-events -->
            <!-- svelte-ignore a11y-no-noninteractive-element-to-interactive-role -->
            <img
                class="w-[600px] mt-10 mb-1"
                src={postImage || "https://picsum.photos/800"}
                on:click={() => inputFile.click()}
                role="button"
            />
            <input
                bind:this={inputFile}
                on:change={changePostImage}
                type="file"
                class="hidden"
            />
            <span class="text-base text-white">
                *klik gambar untuk mengedit
            </span>
            <input
                bind:value={title}
                type="text"
                class="mt-10 w-[600px] bg-[#fff0] p-3 text-white text-lg border border-white outline-none focus:border-[#2d87e2] placeholder:text-gray-400 placeholder:italic"
                placeholder="Tambahkan judul"
            />
            <textarea
                class="mt-7 p-4 w-[600px] h-[200px] bg-[#ffffff00] rounded-lg text-white text-lg border border-white outline-none focus:border-[#2d87e2] placeholder:text-gray-400 placeholder:italic"
                bind:value={description}
                cols="30"
                rows="10"
                placeholder="Tambahkan deskripsi"
            />
            <!-- <div class="grid grid-cols-1 mt-10 w-[600px]">
                <div class="grid grid-cols-[25px_auto]">
                    <input type="checkbox" name="noComment" class="comment accent-[#2d87e2]">
                    <label for="noComment" class="labelComment text-xl text-white ml-3">Nonaktifkan Komentar</label>
                </div>
                <div class="grid grid-cols-[25px_auto] mt-4">
                    <input type="checkbox" name="private" id="" class="private accent-[#2d87e2]">
                    <label for="private" class="labelPrivate text-xl text-white ml-3">Jadikan Pribadi</label>
                </div>
            </div> -->
            <button
                class="bg-[#0E8388] hover:bg-[#2ec3c8] px-8 py-3 rounded-lg mt-7 text-white"
                on:click={createPost}>Post</button
            >
        </div>
    </div>
    <Profile />
</div>

<style lang="scss">
    #post-preview::-webkit-scrollbar {
        display: none;
    }
    #post-preview {
        -ms-overflow-style: none;
        scrollbar-width: none;
    }
</style>
