// 获取当前时间的中文形式  
Date.prototype.getCNDate = function() {
	var oDateText = '';
	oDateText += this.getFullYear().LenWithZero(4) + new Number(24180).ChrW();
	oDateText += this.getMonth().LenWithZero(2) + new Number(26376).ChrW();
	oDateText += this.getDate().LenWithZero(2) + new Number(26085).ChrW();
	oDateText += this.getHours().LenWithZero(2) + new Number(26102).ChrW();
	oDateText += this.getMinutes().LenWithZero(2) + new Number(20998).ChrW();
	oDateText += this.getSeconds().LenWithZero(2) + new Number(31186).ChrW();
	oDateText += new Number(32).ChrW() + new Number(32).ChrW() + new Number(26143).ChrW() + new Number(26399).ChrW() + new String('26085199682010819977222352011620845').substr(this.getDay() * 5, 5).ToInt().ChrW();
	return oDateText;
};
//扩展Date格式化  
Date.prototype.format = function(format) {
	var o = {
		"M+": this.getMonth() + 1, //月份           
		"d+": this.getDate(), //日           
		"h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时           
		"H+": this.getHours(), //小时           
		"m+": this.getMinutes(), //分           
		"s+": this.getSeconds(), //秒           
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度           
		"S": this.getMilliseconds() //毫秒           
	};
	var week = {
		"0": "\u65e5",
		"1": "\u4e00",
		"2": "\u4e8c",
		"3": "\u4e09",
		"4": "\u56db",
		"5": "\u4e94",
		"6": "\u516d"
	};
	if(/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	if(/(E+)/.test(format)) {
		format = format.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f" : "\u5468") : "") + week[this.getDay() + ""]);
	}
	for(var k in o) {
		if(new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return format;
}
Date.prototype.diff = function(interval, objDate) {
	//若参数不足或 objDate 不是日期类型則回传 undefined  
	if(arguments.length < 2 || objDate.constructor != Date) {
		return undefined;
	}
	switch(interval) {
		//计算秒差                                                          
		case 's':
			return parseInt((objDate - this) / 1000);
			//计算分差  
		case 'n':
			return parseInt((objDate - this) / 60000);
			//计算時差  
		case 'h':
			return parseInt((objDate - this) / 3600000);
			//计算日差  
		case 'd':
			return parseInt((objDate - this) / 86400000);
			//计算周差  
		case 'w':
			return parseInt((objDate - this) / (86400000 * 7));
			//计算月差  
		case 'm':
			return(objDate.getMonth() + 1) + ((objDate.getFullYear() - this.getFullYear()) * 12) - (this.getMonth() + 1);
			//计算年差  
		case 'y':
			return objDate.getFullYear() - this.getFullYear();
			//输入有误  
		default:
			return undefined;
	}
};
/**
 * 将字符串根据指定的格式转换为Date对象
 * @author xiaolinzi
 * @mail xiaolinzistudio@foxmail.com
 * @param {String} dateString 表示时间的字符串表现形式
 * @param {String} pattern 时间格式字符串【支持y(年),M(月),d(日),h(时),m(分),s(秒),S(毫秒)】
 */
Date.prototype.parse = function(dateString, pattern) {
	dateString = dateString || '';
	pattern = pattern || this.DEFAULT_PATTERN;
	console.log(this);
	var matchs1 = pattern.match(this.SIGN_REGEXP);
	var matchs2 = dateString.match(/(\d)+/g);
	if(matchs1.length == matchs2.length) {
		var _date = new Date(1970, 0, 1);
		for(var i = 0; i < matchs1.length; i++) {
			var _int = parseInt(matchs2[i]);
			var sign = matchs1[i];
			switch(sign.charAt(0)) {
				case 'y':
					_date.setFullYear(_int);
					break;
				case 'M':
					_date.setMonth(_int - 1);
					break;
				case 'd':
					_date.setDate(_int);
					break;
				case 'h':
					_date.setHours(_int);
					break;
				case 'm':
					_date.setMinutes(_int);
					break;
				case 's':
					_date.setSeconds(_int);
					break;
				case 'S':
					_date.setMilliseconds(_int);
					break;
			}
		}
		return _date;
	}
	return null;
};
/**
 * 获取当前时间是一年中的第几天
 * @author xiaolinzi
 * @mail xiaolinzistudio@foxmail.com
 */
Date.prototype.getDayOfYear = function() {
	var y, m, d;
	var total = 0;
	var arr = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	y = this.getFullYear();
	m = this.getMonth();
	d = this.getDate();
	for(var i = 0; i < m; i++) {
		total = total + arr[i];
	}
	if((y % 400 == 0 || (y % 4 == 0 && y % 100 != 0)) && m > 2) {
		total = total + d + 1;
	} else {
		total = total + d;
	}
	return total;
};
