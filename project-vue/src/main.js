import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
// import store from './store'

import store from './store/index'
import installElementPlus from './plugins/element'
import './assets/css/icon.css'
import './assets/css/elementUi.css'
import './assets/font-awesome-4.7.0/css/font-awesome.min.css'

const app = createApp(App)
installElementPlus(app)
app
    .use(store)
    .use(router)
    .mount('#app')