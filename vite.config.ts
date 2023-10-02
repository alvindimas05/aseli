import { defineConfig } from 'vite'
import { svelte } from '@sveltejs/vite-plugin-svelte'
import postcss from './postcss.config.js'
import tsconfigPaths from 'vite-tsconfig-paths'

// https://vitejs.dev/config/
export default defineConfig({
  css: { postcss },
  plugins: [svelte(), tsconfigPaths()],
})
