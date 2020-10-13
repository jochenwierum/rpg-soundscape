const CompressionPlugin = require("compression-webpack-plugin");

module.exports = {
	devServer: {
		port: 8080,
		proxy: {
			"/api": {
				target: "http://localhost:8008/",
				changeOrigin: true
			}
		}
	},
	outputDir: "build/webpack",

	pluginOptions: {
		webpackBundleAnalyzer: {
			openAnalyzer: false,
			reportFilename: "../../report.html",
			defaultSizes: "gzip"
		}
	},

	configureWebpack: {
		plugins:
		process.env.NODE_ENV === "production"
		? [
			new CompressionPlugin({
				filename: "[path][base].gz[query]",
				algorithm: "gzip",
				test: /\.js$|\.css$|\.html$/,
				threshold: 10240,
				minRatio: 0.8
			}),
			new CompressionPlugin({
				filename: "[path][base].br[query]",
				algorithm: "brotliCompress",
				test: /\.(js|css|html|svg)$/,
				compressionOptions: {
					level: 11
				},
				threshold: 10240,
				minRatio: 0.8
			})
		]
		: []
	}
};

