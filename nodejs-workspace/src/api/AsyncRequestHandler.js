import axios from "axios";
import qs from "qs";

/**
 * 上传服务器
 */
const API_UPLOAD_SERVER = (typeof window === "object" && window) ? window.fileServer : "";

/**
 * 统一异步请求处理器
 * @param {String} _url 请求的地址
 * @param {String} _method 请求的方法
 * @param {Object || Array} _params 请求的参数
 * @param {String} type 请求的参数
 * @param {Object} _cancelTokenSource 取消请求的 CancelToken 的 source，详细内容参考：http://www.axios-js.com/zh-cn/docs/#axios-config
 */
function ajax(_url, _method, _params, _type, _cancelTokenSource) {
	_method = _method || "GET";
	_params = _params || {};
	_type = _type || "QueryString";
	if(!_url) {
		return new Promise(function(resolve, reject) {
			reject("Unable to parse the address of the request");
		});
	}
	var config = {
		headers: {
			// "X-Requested-Body-Type": _type,
			"X-Requested-With": "XMLHttpRequest"
		},
		method: _method,
		url: _url,
		withCredentials: true
	};
	if(_cancelTokenSource && typeof _cancelTokenSource.cancel == "function" && "token" in _cancelTokenSource) {
		config.cancelToken = _cancelTokenSource.token;
	}
	if(_method.toUpperCase() == "PUT" || _method.toUpperCase() == "POST" || _method.toUpperCase() == "PATCH") {
		if(_type.toUpperCase() == "FORMDATA") {
			// `transformRequest` 允许在向服务器发送前，修改请求数据，
			// 只能用在 "PUT", "POST" 和 "PATCH" 这几个请求方法
			// 后面数组中的函数必须返回一个字符串，或 ArrayBuffer，或 Stream
			// 如果传递 type 值为 FormData，则认为需要进行表单提交
			// 如果请求方式为 POST 则处理为 表单提交
			config.headers["Content-Type"] = "application/x-www-form-urlencoded;charset=UTF-8";
			config.transformRequest = [function(data) {
				return qs.stringify(_params);
			}];
		} else {
			config.data = _params;
		}
	} else if(_method.toUpperCase() == "DELETE") {
		// 如果请求方式为 DELETE 则处理为 表单提交
		config.headers["Content-Type"] = "application/x-www-form-urlencoded;charset=UTF-8";
		config.transformRequest = [function(data) {
			return qs.stringify(_params);
		}];
	} else {
		config.params = _params;
	}
	return axios(config);
}

/**
 * 上传助手
 */
const UPLOAD_HELPER = {
	mapper: {
		/**
		 * 文件上传服务器地址
		 */
		root: API_UPLOAD_SERVER,
		/**
		 * 图片上传地址
		 */
		picture: API_UPLOAD_SERVER + '/uploader/picture',
		/**
		 * 文档上传地址getCrmCustomerFindExportColumns
		 */
		document: API_UPLOAD_SERVER + '/uploader/document',
		/**
		 * 文件上传地址
		 */
		file: API_UPLOAD_SERVER + '/uploader/file',
		/**
		 * 知识库  新建文档   文件上传地址
		 */
		wiki: API_UPLOAD_SERVER + '/uploader/wiki',
		/**
		 * 学习中心视频上传
		 */
		video: API_UPLOAD_SERVER + '/exam/video/upload',
		// 题库管理-导入
		xlsxImmport: API_UPLOAD_SERVER + '/exam/question/import',
		/**
		 * 上传发票照片
		 */
		// mailInvoicePic:API_UPLOAD_SERVER + '/exam/question/import'
	},
	/**
	 * 公共文件上传，
	 * @param {String} id Input 元素的ID
	 * @param {String} url 上传的地址
	 * @param {Function} process 进度回调函数
	 * @deprecated 不建议使用，这里的上传函数
	 */
	handler(id, url, process) {
		// 统一上传地址，不做更改
		if(url) {
			if(!url.startsWith(API_UPLOAD_SERVER)) {
				url = API_UPLOAD_SERVER + url;
			}
		} else {
			// 图片上传默认地址
			url = API_UPLOAD_SERVER + '/uploader/picture';
		}
		// 进度处理函数
		process = typeof process == 'function' ? process : function() {};
		var name = 'file';
		var files = [];
		if(id) {
			try {
				var control = document.getElementById(id);
				if(control) {
					name = control.name || name;
					files = control.files;
				}
			} catch(e) {
				files = [];
			}
		} else {
			try {
				var controls = document.getElementsByTagName('input');
				for(var i = 0; i < controls.length; i++) {
					if(controls[i].type == 'file') {
						files = controls[i].files;
						name = controls[i].name || name;
						break;
					}
				}
			} catch(e) {
				files = [];
			}
		}
		// 设置批量文件提交
		var ps = [];
		for(var i = 0; i < files.length; i++) {
			ps.push(new Promise(function(resolve, reject) {
				if(FormData) {
					var param = new FormData();
					param.append(name, files[i], files[i].name);
					var a = new axios.post(url, param, {
						headers: {
							'Content-Type': 'multipart/form-data'
						},
						withCredentials: true
					}).then(function(response) {
						process(response, files.length);
						if(response && response.data) {
							resolve(response.data);
						} else {
							reject('服务器未响应')
						}
					}).catch(reject);
				} else {
					reject('浏览器不支持 FormData 对象导致文件上传失败, 请升级您的浏览器');
				}
			}));
		}
		return Promise.all(ps);
	}
};

export default {
	ajax: ajax,
	uploadHelper: UPLOAD_HELPER,
	API_UPLOAD_SERVER: API_UPLOAD_SERVER,
	source: () => axios.CancelToken.source()
};