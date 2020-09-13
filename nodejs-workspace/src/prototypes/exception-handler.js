import redirect from "utils/redirect.js";

/**
 * 被允许忽略的URL的地址
 */
const IGNORANCES = [
	"/session/logout",
	"/login"
];
/**
 * 被排除的异常，属于这些类抛出的异常不被认为是异常，而是后端发送的消息
 */
const EXCEPTIONS = [
	// 服务器调用异常
	'org.marvelousness.springboot.basic.exception.ServiceInvokeException',
	// 参数异常
	'org.marvelousness.springboot.basic.exception.ParameterException',
	// 授权异常
	'org.marvelousness.springboot.basic.exception.AuthenticationException',
	// 未知的账号
	'org.apache.shiro.authc.UnknownAccountException',
	// 账户已经被锁定
	'org.apache.shiro.authc.LockedAccountException',
	// 账户已经被禁用
	'org.apache.shiro.authc.DisabledAccountException',
	// 账户异常
	'org.apache.shiro.authc.AccountException',
	// 运行时异常
	'java.lang.RuntimeException'
];

/**
 * 判断对象是否是来自后端的异常对象
 * @param {Object} stackTrace
 */
function isException(stackTrace) {
	if (stackTrace && stackTrace.analyzeException) {
		for (let i = 0; i < EXCEPTIONS.length; i++) {
			if (stackTrace.analyzeException.indexOf(EXCEPTIONS[i]) > -1) {
				return false;
			}
		}
		return true;
	}
	return false;
}

/**
 * 判断是否已经离线
 * @param {Object} err
 */
function isOffline(err) {
	if (err && err.response && err.response) {
		if ((err.response.status === 401 ? true : false) && err.response.data === "UNAUTHORIZED") {
			return true;
		}
	}
	return false;
}

let isOpening = false;

/**
 * 处理错误的响应数据
 */
export default function (err, callback) {
	if (typeof callback !== 'function') {
		callback = function () {
		};
	}
	let vue = this;
	// 1. 判断是否是被忽略的地址
	if (err && err.request && err.request.responseURL) {
		let responseURL = (err.request.responseURL + "").trim();
		for (let i = 0; i < IGNORANCES.length; i++) {
			let ignorance = IGNORANCES[i];
			let reg = null;
			if (typeof ignorance === "string" && (ignorance + "").trim().length > 0) {
				reg = new RegExp((ignorance + "").trim());
			}
			if (reg != null && reg.test(responseURL)) {
				// 地址被忽略，则直接不做任何处理
				callback();
				return;
			}
		}
	}

	// 2. 如果没有 Vue 的消息提示组件，则直接打印
	if (!(vue && vue.$Message && typeof vue.$Message.error === 'function')) {
		try {
			console.log(JSON.stringify(err));
		} catch (e) {
		}
		callback();
		return;
	}

	// 3. 判断是否是因为离线导致的异常
	if (isOffline(err)) {
		try {
			console.log(JSON.stringify(err));
		} catch (e) {
		}
		// 离线处理
		if (isOpening) {
			callback();
			return;
		}
		isOpening = true;
		vue.$Message.error({
			content: '错误提示: 您的会话已经超时, 请重新登陆!',
			onClose() {
				isOpening = false;
				redirect("/session/logout?t=" + new Date().getTime());
			}
		});
		callback();
		return;
	}

	// 4. 解析接口异常信息， 用以判断是前端的异常还是后端的异常
	if (err.response && err.response.data) {
		let exception = err.response.data;
		let innerHtml= "";
		let innerTitle = "服务器异常";
		if (err.response.status >= 400) {
			if (exception.summary) {
				console.log();
				console.log();
				console.log('信息摘要:', exception.summary);
			}
			if (exception.stackTrace) {
				if (exception.stackTrace.originalException) {
					console.log("堆栈摘要:", exception.stackTrace.originalException);
					innerTitle = (exception.stackTrace.originalException + "").substr(0, 100) + "...";
				}
				if (exception.stackTrace.analyzeException) {
					console.error(exception.stackTrace.analyzeException);
					innerHtml += "<pre style='text-align: left;'>" + exception.stackTrace.analyzeException + "</pre>";
				}
				console.log();
				console.log();
			}
		}
		if (isException(exception.stackTrace)) {
			// 如果存在堆栈信息，则说明，后台出现了没有被捕获的异常，则这里使用友好界面输出
			vue.$Message.error({
				content: '',
				duration: 15,
				render(h) {
					return h('div', {}, [h('strong', {
						domProps: {
							innerHTML: innerTitle
						}
					}), h('a', {
						domProps: {
							innerHTML: '点击此处查看堆栈信息'
						},
						style: {
							padding: "0px 0.5em",
							fontSize: "12px"
						},
						on: {
							click: (e) => {
								if (e && e.target && e.target.nextElementSibling) {
									let p = e.target.nextElementSibling;
									if (p.style) {
										p.style.display = p.style.display == 'block' ? 'none' : 'block';
										p.style.overflow = "auto";
										p.style.maxWidth = "900px";
										p.style.margin = "10px auto";
										p.style.padding = "5px";
										p.style.border = "1px solid #CCC";
										p.style.borderRadius = "3px";
										p.style.maxHeight = "400px";
									}
								}
							}
						}
					}), h('p', {
						domProps: {
							innerHTML: innerHtml
						},
						style: {
							display: 'none'
						}
					})]);
				}
			});
		} else if (exception.message) {
			let msg = (exception.message) + '';
			vue.$Message.error(msg.length > 50 ? msg.substring(0, 50) : msg);
		} else {
			if (typeof exception == 'string') {
				vue.$Message.error(exception);
			} else {
				vue.$Message.error(JSON.stringify(exception));
			}
		}

		callback();
		return;
	} else if (err instanceof Error) {
		// 如果错误是 Error 的实例，则说明是前端的脚本报错
		vue.$Message.error({
			content: '',
			duration: 10,
			render(h) {
				return h('div', {}, [h('strong', {
					domProps: {
						innerHTML: 'JavaScript执行异常:&nbsp;' + err.message
					}
				}), h('a', {
					domProps: {
						innerHTML: 'more'
					},
					on: {
						click: (e) => {
							if (e && e.target && e.target.nextElementSibling) {
								let p = e.target.nextElementSibling;
								if (p.style) {
									p.style.display = p.style.display == 'block' ? 'none' : 'block';
								}
							}
						}
					}
				}), h('p', {
					domProps: {
						innerHTML: "<pre>" + err.stack + "</pre>"
					},
					style: {
						display: 'none'
					}
				})]);
			}
		});
	} else if (err.__CANCEL__) {
		// 如果错误对象中包含 __CANCEL__ 属性，则说明是在发送请求的过程中，被中止了，则直接不做任何处理
		console.error("手动中止了请求触发的异常", err.__CANCEL__);
	} else {
		console.error(err);
		vue.$Message.error("服务器异常");
		// vue.$router.push({ name: '500'});
	}
};
