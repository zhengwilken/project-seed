import Vue from 'vue'
import App from './App'
import api from '../../api'
import utils from '../../utils'
import Antd from 'ant-design-vue'

import 'ant-design-vue/dist/antd.min.css'
import '../../styles/index.css'

Vue.config.productionTip = false;

Vue.use(Antd);

Vue.prototype.$api = api;
Vue.prototype.$utils = utils;

new Vue({
  render: h => h(App)
}).$mount('#app');