import Vue from "vue";
// import "es6-promise/auto";
import router from "./router";
import Prototypes from "./prototypes";
import ViewDesign from "view-design";
import Components from "components";

// 引入接口
import "api";

// 引入系统主题
import "view-design/dist/styles/iview.css";
import "./assets/css/app.less";

Vue.config.devtools = true;

Vue.use(Prototypes);
Vue.use(Components);
Vue.use(ViewDesign);

new Vue({
	router,
	name: "order-management-system",
	template: '<transition name="fade" mode="out-in"><router-view></router-view></transition>',
}).$mount("#app");