import Vue from "vue";
/**
 * 错误处理器
 * @param {Object} err 发生的异常
 * @param {Object} vm vue虚拟节点
 */
export default function(err, vm) {
	// 当出现异常的情况下，执行对应的逻辑
	if(vm && vm.$Message && typeof vm.$Message.error === "function") {
		vm.$Message.error(err.message);
	}
	console.error(err);
}