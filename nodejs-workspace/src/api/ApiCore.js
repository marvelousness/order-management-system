import ARH from "./AsyncRequestHandler.js";
/**
 * 客户系统-API服务
 */
const API_SERVER = "/api";
/**
 * 统一异步请求处理器
 * @param {String} _url 请求的地址
 * @param {String} _method 请求的方法
 * @param {Object || Array} _params 请求的参数
 * @param {String} type 请求的参数
 */
const handleRequest = ARH.ajax;

// TODO=::=JavaScript Function Generator=::=BEGIN

/**
 * 登录接口
 * 
 * @author 981247127@qq.com
 * @time 2020-08-15 17:53
 */
export const login = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/../authentication/login`, 'POST', params, 'FormData');
};
/**
 * 获取地区树
 * 
 * @author 981247127@qq.com
 * @time 2020-08-16 19:53
 */
export const getAreaTree = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/area/tree`, 'GET', params, 'QueryString');
};
/**
 * 获取部门树
 * 
 * @author 981247127@qq.com
 * @time 2020-08-16 19:53
 */
export const getDepartmentTree = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/department/tree`, 'GET', params, 'QueryString');
};
/**
 * 保存用户信息
 * 
 * @author 981247127@qq.com
 * @time 2020-08-19 16:21
 */
export const getUserList = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/user/list`, 'Get', params, 'QueryString');
};
/**
 * 为前端的 AutoComplete 提供数据支持
 * 
 * @author 981247127@qq.com
 * @time 2020-08-19 16:21
 */
export const getUserAutoCompleteOptions = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/user/auto-complete`, 'Get', params, 'QueryString');
};
/**
 * 保存用户信息
 * 
 * @author 981247127@qq.com
 * @time 2020-08-19 16:21
 */
export const postUserSave = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/user/save`, 'POST', params, 'RequestPayload');
};
/**
 * 修改自己的登录密码
 * 
 * @author 981247127@qq.com
 * @time 2020-08-19 16:21
 */
export const postUserPasswd = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/user/passwd`, 'POST', params, 'FormData');
};
/**
 * 修改账号的状态
 * 
 * @author 981247127@qq.com
 * @time 2020-08-19 16:21
 */
export const postUserStatusDisable = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/user/status/disable`, 'POST', params, 'FormData');
};
/**
 * 修改账号的状态
 * 
 * @author 981247127@qq.com
 * @time 2020-08-19 16:21
 */
export const postUserStatusEnable = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/user/status/enable`, 'POST', params, 'FormData');
};
/**
 * 删除用户
 * 
 * @author 981247127@qq.com
 * @time 2020-08-19 16:21
 */
export const postUserDelete = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/user/delete`, 'POST', params, 'FormData');
};
/**
 * 保存客户信息
 * 
 * @author 981247127@qq.com
 * @time 2020-09-05 16:21
 */
export const postCustomerSave = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/customer/save`, 'POST', params, 'RequestPayload');
};
/**
 * 查询客户列表
 * 
 * @author 981247127@qq.com
 * @time 2020-09-05 16:21
 */
export const getCustomerList = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/customer/list`, 'Get', params, 'QueryString');
};
/**
 * 查询单条客户信息
 * 
 * @author 981247127@qq.com
 * @time 2020-09-05 16:21
 */
export const getCustomer = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/customer/get`, 'Get', params, 'QueryString');
};
/**
 * 删除客户信息
 * 
 * @author 981247127@qq.com
 * @time 2020-09-05 16:21
 */
export const postCustomerDelete = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/customer/delete`, 'POST', params, 'FormData');
};
/**
 * 查询订单支付方式
 * 
 * @author 981247127@qq.com
 * @time 2020-09-05 16:21
 */
export const getOrderPaymentWay = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/order/payment/ways`, 'Get', params, 'QueryString');
};
/**
 * 保存订单信息
 * 
 * @author 981247127@qq.com
 * @time 2020-09-05 16:21
 */
export const postOrderSave = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/order/save`, 'POST', params, 'RequestPayload');
};
/**
 * 查询订单列表
 * 
 * @author 981247127@qq.com
 * @time 2020-09-05 16:21
 */
export const getOrderList = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/order/list`, 'Get', params, 'QueryString');
};
/**
 * 查询订单列表
 * 
 * @author 981247127@qq.com
 * @time 2020-09-05 16:21
 */
export const postOrderDistribute = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/order/distribute`, 'POST', params, 'FormData');
};
/**
 * 查询统计信息
 * 
 * @author 981247127@qq.com
 * @time 2020-09-05 16:21
 */
export const getstatistics = function(params) {
	params = params || {};
	return handleRequest(`${API_SERVER}/statistics`, 'Get', params, 'QueryString');
};


// TODO=::=JavaScript Function Generator=::=END 

