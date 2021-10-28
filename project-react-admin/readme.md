# 开始
项目通过 npx create-react-app project-react-admin 创建 创建后运行yarn start 命令，正常启动， http://localhost:3000

##添加依赖 
[elementUi](https://elemefe.github.io/element-react/#/zh-CN/quick-start): yarn add element-react    
[react-hot-loader](http://gaearon.github.io/react-hot-loader/getstarted/) :yarn add react-hot-loader 热加载组件，elementui需要。   
element-theme-default: yarn add element-theme-default   
[react-router](https://github.com/remix-run/react-router): yarn add react-router, yarn add react-router-dom        
[react-redux](https://react-redux.js.org/introduction/getting-started): yarn add  react-redux,状态管理服务，如果要用typescript ,需要安装 redux-typescript     
[redux-saga](https://redux-saga.js.org/): yarn add redux-saga, 应用程序库，参见 [中文版](https://redux-saga-in-chinese.js.org/) 使用说明   
[redux-logger](https://github.com/LogRocket/redux-logger) yarn add redux-logger 日志中间件   
[moment](https://momentjs.com/docs/) yarn add moment 日期处理中间件

##目录结构

api: 调用后台api,与后台交互使用，包含公共代码等
components:公共组件，主要是指框架类组件或都通用组件树，弹窗等。
router: 路由组件
reducers: 前端状态控制器
utils:工具类
##功能模块


#项目运行与部署
### yarn start

Runs the app in the development mode.\ Open http://localhost:3000 to view it in the browser.

The page will reload if you make edits.\ You will also see any lint errors in the console.

### yarn test

Launches the test runner in the interactive watch mode.\ See the section about https://facebook.github.io/create-react-app/docs/running-tests[running tests] for more information.

### yarn build

Builds the app for production to the `build` folder.\ It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\ Your app is ready to be deployed!

See the section about https://facebook.github.io/create-react-app/docs/deployment[deployment] for more information.

### yarn eject

*Note: this is a one-way operation.
Once you `eject`, you can’t go back!*

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time.
This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them.
All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them.
At this point you’re on your own.

You don’t have to ever use `eject`.
The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature.
However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

## Learn More

You can learn more in the https://facebook.github.io/create-react-app/docs/getting-started[Create React App documentation].

To learn React, check out the https://reactjs.org/[React documentation].

### Code Splitting

This section has moved here: https://facebook.github.io/create-react-app/docs/code-splitting

### Analyzing the Bundle Size

This section has moved here: https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size

### Making a Progressive Web App

This section has moved here: https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app

### Advanced Configuration

This section has moved here: https://facebook.github.io/create-react-app/docs/advanced-configuration

### Deployment

This section has moved here: https://facebook.github.io/create-react-app/docs/deployment

### `yarn build` fails to minify

This section has moved here: https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify