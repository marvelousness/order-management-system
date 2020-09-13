import Vue from "vue";
import VueRouter from "vue-router";

import Forbidden from "../views/core/403.vue";
import NotFound from "../views/core/404.vue";
import ServerException from "../views/core/500.vue";
import Login from "../views/core/Login.vue";

import Dashboard from "../views/core/Dashboard.vue";
import Application from "../views/core/Application.vue";
import SystemDepartment from "../views/system/SystemDepartment";
import SystemUser from "../views/system/SystemUser";
import CustomerEditor from "../views/customer/CustomerEditor";
import CustomerList from "../views/customer/CustomerList";
import OrderEditor from "../views/order/OrderEditor";
import OrderList from "../views/order/OrderList";

const routes = [{
	path: "*",
	hidden: true,
	redirect: {
		path: "/not-found"
	}
}, {
	name: "403",
	hidden: true,
	path: "/forbidden",
	component: Forbidden
}, {
	name: "404",
	hidden: true,
	path: "/not-found",
	component: NotFound
}, {
	name: "500",
	hidden: true,
	path: "/server-exception",
	component: ServerException
}, {
	name: "Login",
	hidden: true,
	path: "/login",
	component: Login
}, {
	path: "/",
	name: "App",
	component: Application,
	children: [{
		path: "",
		name: "Dashboard",
		meta: {
			icon: "md-clock",
			label: "仪表盘"
		},
		component: Dashboard
	}, {
		path: "/system",
		name: "System",
		meta: {
			icon: "md-settings",
			label: "系统设置"
		},
		component: {
			name: "System",
			template: "<router-view></router-view>",
		},
		children: [{
			meta: {
				icon: "ios-contacts",
				label: "组织结构"
			},
			path: "/system/department",
			name: "SystemDepartment",
			component: SystemDepartment
		}, {
			meta: {
				icon: "md-contacts",
				label: "用户账号管理"
			},
			path: "/system/users",
			name: "SystemUser",
			component: SystemUser
		}]
	}, {
		meta: {
			icon: "md-people",
			label: "客户管理"
		},
		path: "/customer",
		name: "Customer",
		component: {
			name: "System",
			template: "<router-view></router-view>",
		},
		children: [{
			meta: {
				icon: "ios-person-add-outline",
				label: "新建客户",
				defaultPath: "/customer/editor"
			},
			path: "/customer/editor/:id?",
			name: "CustomerEditor",
			component: CustomerEditor
		}, {
			meta: {
				icon: "md-list-box",
				label: "客户列表"
			},
			path: "/customer/list",
			name: "CustomerList",
			component: CustomerList
		}]
	}, {
		meta: {
			icon: "ios-folder",
			label: "订单管理"
		},
		path: "/order",
		name: "Order",
		component: {
			name: "System",
			template: "<router-view></router-view>",
		},
		children: [{
			meta: {
				icon: "md-document",
				label: "新建订单",
				defaultPath: "/order/editor"
			},
			path: "/order/editor/:customer?",
			name: "OrderEditor",
			component: OrderEditor
		}, {
			meta: {
				icon: "md-list-box",
				label: "订单列表"
			},
			path: "/order/list",
			name: "OrderList",
			component: OrderList
		}]
	}]
}];


const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
	// 为了处理项目经常出现的 NavigationDuplicated 异常，特修改 VueRouter 的原型中的push 函数
	let croute = this.currentRoute;
	// 如果当前路由实例中存在对应的地址，这里需要循环匹配下当前的地址是否是已经被匹配上，已经匹配的地址，不再执行 push
	let pushed = false;
	if(croute && croute.matched instanceof Array) {
		for(let i = 0; i < croute.matched.length; i++) {
			let m = croute.matched[i];
			if(m && m.regex && typeof m.regex.test === "function" && m.regex.test(location)) {
				pushed = true;
				break;
			}
		}
	}
	if(pushed === false) {
		originalPush.call(this, location).catch(err => {
			console.error(err);
		});
	}
};

/**
 * 创建路由对象
 */
const router = new VueRouter({
	mode: "history",
	fallback: false,
	scrollBehavior: () => ({
		y: 0
	}),
	routes: routes
});

// 路由开始之前
router.beforeEach((to, from, next) => {
	// console.log("before to:", to);
	// console.log("before from:", from);
	return next();
});

// 路由完成之后
router.afterEach(route => {
	// console.log("after:", route);
});

Vue.use(VueRouter);

export default router;