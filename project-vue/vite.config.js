import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'
import alias from '@rollup/plugin-alias'
import { resolve } from 'path'

const projectRootDir = resolve(__dirname);

// 	resolve:{
// 		alias : {
// 			entries:[
// 				{ find: '@/', replacement: 'src/' },
// 			]
// 		}
// 	}
export default defineConfig({
	base:"./",
	publicDir:true,
	plugins: [
		vue(),alias()
	],
	optimizeDeps: {
		include: ['schart.js']
	},
	server:{
		port:3000,
		host:'0.0.0.0', //监听所有地址
		strictPort:false ,// 是否严格限制端口
		open:false, //自动打开浏览器
	},
	resolve:{
		alias: {
			"@": resolve(projectRootDir, "src"),
		},
	},
	build: {
		outDir: 'dist',
	}
})
