const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = {
    mode: 'production',
    entry: path.join(__dirname, './schooltestingsystem/index.tsx'),
    resolve: {
        extensions: ['.ts', '.tsx', '.js'],
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: '/node_modules/',
            },
            {
                test: /\.css$/,
                use: [
                    MiniCssExtractPlugin.loader,
                    'css-loader'
                ]
            }
        ],
    },
    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, '..', 'resources', 'static'),
        publicPath: '/static/',
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: path.join(__dirname, 'schooltestingsystem', 'index.html'),
        }),
        new MiniCssExtractPlugin({
            filename: 'style.css',
        })
    ],
}
