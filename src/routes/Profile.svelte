<script lang="ts">
    import Sidebar from "components/Sidebar.svelte";
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
            <img src="https://placehold.co/400" alt="konten" class="konten1">
            <img src="https://placehold.co/400" alt="konten" class="konten2">
            <img src="https://placehold.co/400" alt="konten" class="konten3">
            <img src="https://placehold.co/400" alt="konten" class="konten4">
            <img src="https://placehold.co/400" alt="konten" class="konten5">
            <img src="https://placehold.co/400" alt="konten" class="konten6">
            <img src="https://placehold.co/400" alt="konten" class="konten7">
            <img src="https://placehold.co/400" alt="konten" class="konten8">
            <img src="https://placehold.co/400" alt="konten" class="konten9">
        </div>
    </div>

    <div class="w-[325px] h-screen">
        <div class="h-full pl-4 pr-3 pt-10 overflow-y-auto  border-l border-[#656565]">

            <div class="grid grid-cols-1 place-items-center mx-3">
                <img src="https://picsum.photos/200" class="w-[150px] h-[150px] rounded-full border border-white">
                <div class="text-white text-3xl mt-4">abyankuhhh</div>
                <div class="flex w-full justify-between items-center text-white text-center mt-8 text-xl">
                    <div class="grid grid-cols-1">
                        <span class="font-bold">100</span>
                        <span class="text-lg">Pengikut</span>
                    </div>
                    <div class="grid grid-cols-1">
                        <span class="font-bold">100</span>
                        <span class="text-lg">Diikuti</span>
                    </div>
                    <div class="grid grid-cols-1">
                        <span class="font-bold">100</span>
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