
import { createStore } from 'vuex'
import {navi} from './modules/navigation'
import dictionary from "./modules/dictionary";

export default createStore({
    modules: {
        navi,
        dictionary,
    },
    strict: true,
})