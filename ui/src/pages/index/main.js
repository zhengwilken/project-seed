import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import api from '../../api'
import utils from '../../utils'
import messages from '../../messages'
import handler from '../../handler'
import page from '../../page'
import style from '../../styles/style.config'
import Antd from 'ant-design-vue'
import VueCookie from 'vue-cookie'

import 'ant-design-vue/dist/antd.min.css'
import '../../styles/index.css'

Vue.config.productionTip = false;

Vue.use(Antd);
Vue.use(VueCookie);

Vue.prototype.$api = api;
Vue.prototype.$messages = messages;
Vue.prototype.$utils = utils;
Vue.prototype.$handler = handler;
Vue.prototype.$page = page;
Vue.prototype.$style = style;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
