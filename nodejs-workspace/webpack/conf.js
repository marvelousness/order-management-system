const PATH = require("path");
const VueLoaderPlugin = require("vue-loader/lib/plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");

/**
 * 解析物理地址
 */
function resolve(pathname) {
	return PATH.resolve(__dirname, "../" + pathname);
}

/**
 * 公共配置
 */
module.exports = {
	entry: {
		app: resolve("src/main.js")
	},
	resolve: {
		extensions: [".js", ".vue", ".less", ".json"],
		modules: [
			resolve("src"),
			resolve("node_modules"),
			resolve("src/views/core/components")
		],
		alias: {
			src: resolve("src/"),
			api: resolve("src/api/"),
			utils: resolve("src/utils/"),
			vue: resolve("node_modules/vue/dist/vue.js"),
			components: resolve("src/views/core/components/")
		}
	},
	plugins: [
		new VueLoaderPlugin(),
		new HtmlWebpackPlugin({
			template: resolve("index.html"),
			inject: true
		})
	]
};
