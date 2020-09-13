// 为 components 新增 VUE组件安装器
import DepartmentTree from './department-tree';

const components = {
	DepartmentTree
};

const install = function(Vue, opts = {}) {
	Object.keys(components).forEach(key => {
		Vue.component(key, components[key]);
	});
};

if(typeof window !== 'undefined' && window.Vue) {
	install(window.Vue);
}

const API = {
	version: '1.0',
	install,
	...components
};

export default API;