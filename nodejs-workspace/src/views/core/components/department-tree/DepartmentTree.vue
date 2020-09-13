<template>
    <div class="department-tree">
        <div v-for="(dep, i) in deps" :key="i" class="department-box" :style="dep.style">
			<div class="department-line-box" v-if="dep.line.v.visible">
				<i class="line-vertical-left" :style="dep.line.v.left"></i>
				<i class="line-vertical-right" :style="dep.line.v.right"></i>
			</div>
            <span class="department-name">{{dep.name}}</span>
			<div class="department-line-box" v-if="dep.line.h.visible">
				<i class="line-horizontal-left" :style="dep.line.h.left"></i>
				<i class="line-horizontal-right" :style="dep.line.h.right"></i>
			</div>
            <department-tree :data="dep.departments" v-if="dep.departments instanceof Array"/>
        </div>
    </div>
</template>

<script>
	// 统一字体大小， 14px
	const FONT_SIZE = 14;
	// 给容器左右留白的字数， 1.4em
	const GEP_WIDTH = 1.4;

	function calcStyle(deps) {
		if(!(deps instanceof Array && deps.length > 0)) {
			return;
		}
		for(let i = 0; i < deps.length; i++) {
			let dep = deps[i];
			let length = 0;
			let halfFirst = 0;
			let halfLast = 0;
			let nameLength = 0;
			let subcount = 0;
			if(!(dep && dep.name)) {
				continue;
			}
			nameLength = (dep.name + "").trim().length;
			length += nameLength;
			
			if(dep.departments instanceof Array) {
				// 移除无效的子级
				for(let j = 0; j < dep.departments.length; j++) {
					let subdep = dep.departments[j];
					if(!(subdep && subdep.name)) {
						// 删除没有名字的子级部门
						dep.departments.splice(j, 1);
						j--;
					}
				}

				subcount = dep.departments.length;
				calcStyle(dep.departments);

				for(let j = 0; j < subcount; j++) {
					let subdep = dep.departments[j];
					let sublength = subdep._subDepNameLength;
					if(sublength > 0) {
						length += sublength;
						if(j == 0) {
							// 取第一个的长度的一半
							halfFirst = sublength / 2;
						}
						if (j == subcount - 1) {
							// 取最后一个的长度的一半
							halfLast = sublength / 2;
						}
					}
				}
			}
			// 给长度增加 1.4 倍的字宽，作为左右留白
			length += GEP_WIDTH;
			dep._subDepNameLength = length;
			// 为 department-box 设计样式
			dep.style = {
				width: length * FONT_SIZE + "px"
			};
			// 求子级容器间隙的宽度
			let gepwidth = subcount > 0 ? (nameLength + GEP_WIDTH) / subcount / 2 : 0;
			// 为 department-line 设计样式
			dep.line = {
				v: {
					visible: dep.pid > 0,
					left: {
						// width: length / 2 + "px"
					},
					right: {
						// width: length / 2 + "px"
					}
				},
				h: {
					visible: subcount > 0,
					left: {
						width: (length / 2 - gepwidth - halfFirst) * FONT_SIZE + 1 + "px",
						// backgroundColor: "red"
					},
					right: {
						width: (length / 2 - gepwidth - halfLast) * FONT_SIZE + 1 + "px",
						// backgroundColor: "green"
					}
				}
			};
		}
	}

	export default {
		name: "DepartmentTree",
		props: {
			data: {
				type: Array,
				required: true
			}
		},
		computed: {
			deps: {
				get() {
					let deps = JSON.parse(JSON.stringify(this.data));
					calcStyle(deps);
					return deps;
				}
			}
		}
	}
</script>

<style scoped>
.department-tree {
	text-align: center;
	display: flex;
}
.department-box {
	font-size: 15px;
    display: inline-block;
	margin: 0px auto;
}
.department-name {
	cursor: default;
	background-color: #FFF;
	border-radius: 3px;
	padding: 0.1em 0.5em;
	font-size: 14px;
	line-height: 2em;
	display: inline-block;
    border: solid 1px #CCC;
}
.department-name:hover {
	background-color: mintcream;
}
.department-line-box {
	display: flex;
	justify-content: center;
}
.department-line-box>i {
	display: inline-block;
	height: 0.8em;
	min-width: 0.1px;
}
.line-vertical-left {
	border-right: 1px solid #CCC;
}
.line-vertical-right {
	border-left: 1px solid #CCC;
}
.line-horizontal-left {
	border-right: 1px solid #CCC;
	border-bottom: 2px solid #CCC;
}
.line-horizontal-right {
	border-left: 1px solid #CCC;
	border-bottom: 2px solid #CCC;
}
</style>