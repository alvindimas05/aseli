<script lang="ts" context="module">
    export interface CommentData {
        id: string;
        username: string;
        comment: string;
    }
</script>

<script lang="ts">
    export let comment: CommentData;

    import { url } from "@gql/client";
    import { USER } from "@gql/user";
    import type { User } from "@routes/Profile.svelte";
    import { query, type ReadableQuery } from "svelte-apollo";

    const username = localStorage.getItem("username");
    let profile: string | null = null;
    let user: ReadableQuery<User> = query(USER, { variables: username });

    $: $user.data,
        (() =>
            (profile = $user.data?.user.profile_image
                ? `${url}/images/${$user.data?.user.profile_image}`
                : "https://picsum.photos/200"))();
</script>

<div class="flex text-white text-lg mt-5">
    <!-- svelte-ignore a11y-missing-attribute -->
    <img src={profile} class="w-[50px] h-[50px] rounded-full mr-3" />
    <div>
        <div class="font-bold">{comment.username}</div>
        <div class="font-light">
            {comment.comment}
        </div>
    </div>
</div>
