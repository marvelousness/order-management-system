// 引入需要修改原型的脚本
import "./date.prototype";

// TODO 异常处理器
import EXCEPTION_HANDLER from './exception-handler.js';
import ERROR_HANDLER from './error-handler.js';

const install = function(Vue, opts = {}) {
	// 为前端VUE指定错误处理程序
	Vue.prototype.$error = EXCEPTION_HANDLER;
	if(Vue.config) {
		Vue.config.silent = true;
		Vue.config.errorHandler = ERROR_HANDLER;
	}
};

if(typeof window !== 'undefined' && window.Vue) {
	install(window.Vue);
}

export default {
	version: '1.0',
	install
};