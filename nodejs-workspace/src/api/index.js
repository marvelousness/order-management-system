import ARH from "./AsyncRequestHandler.js";
import * as apiCore from "./ApiCore";

/**
 * 图片上传助手
 */
const uploadPictrue = function(id, process) {
	return ARH.uploadHelper.handler(id, ARH.uploadHelper.mapper.picture, process);
};

// 
const API = {
	...apiCore,
	ajax: ARH.ajax,
	source: ARH.source,
	uploadPictrue,
	uploader: ARH.uploadHelper,
	UPLOAD_SERVER: ARH.API_UPLOAD_SERVER
}

if(typeof window !== 'undefined') {
	window.API = API;
}
export default API;