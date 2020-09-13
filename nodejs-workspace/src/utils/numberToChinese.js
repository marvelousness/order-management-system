const chnNumChar = ["零", "一", "二", "三", "四", "五", "六", "七", "八", "九"];
const chnUnitSection = ["", "万", "亿", "万亿", "亿亿"];
const chnUnitChar = ["", "十", "百", "千"];

/**
 * 将数字转字符串
 * @param {Number} section
 */
const NumberToChinese = function(section) {
	section = parseInt(section);
	if(isNaN(section)) {
		return '';
	}
	if(section === 0) {
		return chnNumChar[0];
	}
	var strIns = '',
		chnStr = '';
	var unitPos = 0;
	var zero = true;
	while(section > 0) {
		var v = section % 10;
		if(v === 0) {
			if(!zero) {
				zero = true;
				chnStr = chnNumChar[v] + chnStr;
			}
		} else {
			zero = false;
			strIns = chnNumChar[v];
			strIns += chnUnitChar[unitPos];
			chnStr = strIns + chnStr;
		}
		unitPos++;
		section = Math.floor(section / 10);
	}
	if(chnStr.indexOf('一十') === 0) {
		chnStr = chnStr.substr(1);
	}
	return chnStr;
};
module.exports.default = module.exports = NumberToChinese;