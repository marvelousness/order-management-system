// 1天(d)=86400000毫秒(ms)
const ONE_DAY = 86400000;
// 1时(h)=3600000毫秒(ms)
const ONE_HOUR = 3600000;
// 1分(min)=60000毫秒(ms)
const ONE_MINUTE = 60000;
// 1秒(s)=1000毫秒(ms)
const ONE_SECOND = 1000;
// 时间对象
const TIME_OBJECT = {
	'天': ONE_DAY,
	'小时': ONE_HOUR,
	'分钟': ONE_MINUTE,
	'秒': ONE_SECOND
};
/**
 * 将数字转字符串
 * @param {Number} second 毫秒数
 * @param {Number} showSecond 显示毫秒数
 */
const timeToChinese = function(second, showSecond) {
	var timeString = '';
	var time = second || 0;
	showSecond = showSecond ? true : false;
	// 仅在没有反馈信息，且倒计时存在的时候显示倒计时
	if(time > ONE_SECOND) {
		// 剩余时间大于一秒
		var hasTime;
		for(var label in TIME_OBJECT) {
			var _time = TIME_OBJECT[label];
			if(time > _time) {
				var unit = Math.floor(time / _time);
				hasTime = true;
				timeString += (unit < 10 ? ('0' + unit) : unit) + label;
				time = time % _time;
			} else {
				if(hasTime) {
					timeString += '00' + label;
				}
			}
		}
		if(time > 0 && showSecond) {
			// 如果循环结束后，time任有值，则说明存在毫秒数，显示毫秒级别的数据
			timeString += (time < 10 ? ('0' + time) : time) + '毫秒';
		}
	} else {
		timeString = '00秒';
	}
	return timeString;
};
module.exports.default = module.exports = timeToChinese;