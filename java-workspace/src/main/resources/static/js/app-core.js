(window.webpackJsonp=window.webpackJsonp||[]).push([[1],{100:function(t,e,a){"use strict";var s=a(31);a.n(s).a},101:function(t,e,a){"use strict";var s=a(32);a.n(s).a},145:function(t,e,a){"use strict";var s=a(38);a.n(s).a},146:function(t,e,a){"use strict";var s=a(39);a.n(s).a},30:function(t,e,a){},31:function(t,e,a){},32:function(t,e,a){},38:function(t,e,a){},39:function(t,e,a){},87:function(t,e,a){"use strict";function s(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"department-container"},[e("Dt",{attrs:{data:this.data}})],1)}function r(){var a=this,t=a.$createElement,s=a._self._c||t;return s("div",{staticClass:"department-tree"},a._l(a.deps,function(t,e){return s("div",{key:e,staticClass:"department-box",style:t.style},[t.line.v.visible?s("div",{staticClass:"department-line-box"},[s("i",{staticClass:"line-vertical-left",style:t.line.v.left}),a._v(" "),s("i",{staticClass:"line-vertical-right",style:t.line.v.right})]):a._e(),a._v(" "),s("span",{staticClass:"department-name"},[a._v(a._s(t.name))]),a._v(" "),t.line.h.visible?s("div",{staticClass:"department-line-box"},[s("i",{staticClass:"line-horizontal-left",style:t.line.h.left}),a._v(" "),s("i",{staticClass:"line-horizontal-right",style:t.line.h.right})]):a._e(),a._v(" "),t.departments instanceof Array?s("department-tree",{attrs:{data:t.departments}}):a._e()],1)}),0)}var i=a(41),n=a.n(i),o=a(85),c=a.n(o);r._withStripped=s._withStripped=!0;var l=a(6),d=a.n(l),v=14,p=1.4;var u={name:"DepartmentTree",props:{data:{type:Array,required:!0}},computed:{deps:{get:function(){var t=JSON.parse(d()(this.data));return function t(e){if(e instanceof Array&&0<e.length)for(var a=0;a<e.length;a++){var s,r=e[a],i=0,n=0,o=0,c=0;if(r&&r.name){if(i+=s=(r.name+"").trim().length,r.departments instanceof Array){for(var l=0;l<r.departments.length;l++){var d=r.departments[l];d&&d.name||(r.departments.splice(l,1),l--)}c=r.departments.length,t(r.departments);for(var u=0;u<c;u++){var h=r.departments[u]._subDepNameLength;0<h&&(i+=h,0==u&&(n=h/2),u==c-1&&(o=h/2))}}i+=p,r._subDepNameLength=i,r.style={width:i*v+"px"};var m=0<c?(s+p)/c/2:0;r.line={v:{visible:0<r.pid,left:{},right:{}},h:{visible:0<c,left:{width:(i/2-m-n)*v+1+"px"},right:{width:(i/2-m-o)*v+1+"px"}}}}}}(t),t}}}},h=(a(145),a(0)),m=Object(h.a)(u,r,[],!1,null,"3381c689",null);m.options.__file="src/views/core/components/department-tree/DepartmentTree.vue";var f={components:{Dt:m.exports},props:{data:{type:Array,required:!0}}},_=(a(146),Object(h.a)(f,s,[],!1,null,"5dccf52b",null));_.options.__file="src/views/core/components/department-tree/DepartmentTreePanel.vue";function b(e,t){c()(g).forEach(function(t){e.component(t,g[t])})}var g={DepartmentTree:_.exports};"undefined"!=typeof window&&window.Vue&&b(window.Vue);var C=n()({version:"1.0",install:b},g);e.a=C},88:function(t,e,a){"use strict";function s(){var t=this.$createElement;return(this._self._c||t)("div",[this._v("\n    403\n")])}s._withStripped=!0;var r={},i=a(0),n=Object(i.a)(r,s,[],!1,null,"20b27161",null);n.options.__file="src/views/core/403.vue";e.a=n.exports},89:function(t,e,a){"use strict";function s(){var t=this.$createElement;return(this._self._c||t)("div",[this._v("\n    404\n")])}s._withStripped=!0;var r={},i=a(0),n=Object(i.a)(r,s,[],!1,null,"20c088e2",null);n.options.__file="src/views/core/404.vue";e.a=n.exports},90:function(t,e,a){"use strict";function s(){var t=this.$createElement;return(this._self._c||t)("div",[this._v("\n    500\n")])}s._withStripped=!0;var r={},i=a(0),n=Object(i.a)(r,s,[],!1,null,"556e661f",null);n.options.__file="src/views/core/500.vue";e.a=n.exports},91:function(t,e,a){"use strict";function s(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"login-container"},[a("div",{staticStyle:{height:"30px"}}),e._v(" "),a("h1",[e._v("欢迎登录")]),e._v(" "),a("div",{staticClass:"form"},[a("div",{staticClass:"form-group"},[a("input",{directives:[{name:"model",rawName:"v-model",value:e.form.authenticator,expression:"form.authenticator"}],staticClass:"form-control form-control-authenticator",style:e.form.authenticator,attrs:{type:"text",placeholder:"请输入登录账号 / 手机号码 / 邮箱"},domProps:{value:e.form.authenticator},on:{input:function(t){t.target.composing||e.$set(e.form,"authenticator",t.target.value)}}})]),e._v(" "),a("div",{staticClass:"form-group"},[a("input",{directives:[{name:"model",rawName:"v-model",value:e.form.certification,expression:"form.certification"}],staticClass:"form-control form-control-certification",style:e.form.certification,attrs:{type:"password",placeholder:"请输入密码"},domProps:{value:e.form.certification},on:{input:function(t){t.target.composing||e.$set(e.form,"certification",t.target.value)}}})]),e._v(" "),a("button",{staticClass:"form-button",attrs:{type:"button"},on:{click:e.handleSubmit}},[e._v("登录")])]),e._v(" "),e._m(0)])}s._withStripped=!0;var r=a(81),i=a.n(r),n=a(40),o={data:function(){return{form:{authenticator:"",certification:""},style:{authenticator:{borderColor:"#1ab394"},certification:{borderColor:"#F00"}},pending:!1}},methods:{handleSubmit:function(){var e=this,t=this.form.authenticator,a=this.form.certification;t&&0<t.length?a&&0<a.length?a.length<4?this.$Message.error("不是有效的登录密码"):(a=i()(a),this.pending=!0,API.login({authenticator:t,certification:a}).then(function(t){e.$Message.success("登录成功，正在跳转..."),Object(n.a)("/?reason=login-succeed&path=/login&t="+Date.now()),e.pending=!1}).catch(function(t){return e.$error(t,function(){e.pending=!1})})):this.$Message.error("请输入登录密码"):this.$Message.error("请输入登录身份，它可以是登录账号 / 手机号码 / 邮箱")}}},c=(a(99),a(0)),l=Object(c.a)(o,s,[function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"copyright"},[e("small",[this._v("Copyright 奇思妙想工作室 1.0.0 © 2020")])])}],!1,null,null,null);l.options.__file="src/views/core/Login.vue";e.a=l.exports},92:function(t,e,a){"use strict";function s(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"dashboard-container"},[a("div",{staticClass:"dashboard-header"},[a("Card",{staticClass:"dashboard-card"},[a("div",{staticClass:"dashboard-card-title",attrs:{slot:"title"},slot:"title"},[t._v("\n                收入\n            ")]),t._v(" "),a("div",{staticClass:"dashboard-card-body"},[a("div",{staticClass:"dashboard-card-money"},[t._v(t._s(t.thisYearIncome))]),t._v(" "),a("div",{staticClass:"dashboard-card-desc"},[a("div",{staticClass:"dashboard-card-desc-label"},[t._v("今年总收入")]),t._v(" "),a("div",{staticClass:"dashboard-card-desc-status"},[a("span",[t._v(t._s(t.statistics.ratio.income)+"%")]),t._v(" "),0<t.statistics.ratio.income?a("Icon",{attrs:{type:"md-trending-up"}}):t._e(),t._v(" "),t.statistics.ratio.income<0?a("Icon",{attrs:{type:"md-trending-down"}}):t._e()],1)])])]),t._v(" "),a("Card",{staticClass:"dashboard-card"},[a("div",{staticClass:"dashboard-card-title",attrs:{slot:"title"},slot:"title"},[t._v("\n                订单\n            ")]),t._v(" "),a("div",{staticClass:"dashboard-card-body"},[a("div",{staticClass:"dashboard-card-money"},[t._v(t._s(t.thisYearNewOrder))]),t._v(" "),a("div",{staticClass:"dashboard-card-desc"},[a("div",{staticClass:"dashboard-card-desc-label"},[t._v("新订单")]),t._v(" "),a("div",{staticClass:"dashboard-card-desc-status"},[a("span",[t._v(t._s(t.statistics.ratio.order)+"%")]),t._v(" "),0<t.statistics.ratio.order?a("Icon",{attrs:{type:"md-trending-up"}}):t._e(),t._v(" "),t.statistics.ratio.order<0?a("Icon",{attrs:{type:"md-trending-down"}}):t._e()],1)])])]),t._v(" "),a("Card",{staticClass:"dashboard-card"},[a("div",{staticClass:"dashboard-card-title",attrs:{slot:"title"},slot:"title"},[t._v("\n                客户\n            ")]),t._v(" "),a("div",{staticClass:"dashboard-card-body"},[a("div",{staticClass:"dashboard-card-money"},[t._v(t._s(t.thisYearNewCustomer))]),t._v(" "),a("div",{staticClass:"dashboard-card-desc"},[a("div",{staticClass:"dashboard-card-desc-label"},[t._v("新客户")]),t._v(" "),a("div",{staticClass:"dashboard-card-desc-status"},[a("span",[t._v(t._s(t.statistics.ratio.customer)+"%")]),t._v(" "),0<t.statistics.ratio.customer?a("Icon",{attrs:{type:"md-trending-up"}}):t._e(),t._v(" "),t.statistics.ratio.customer<0?a("Icon",{attrs:{type:"md-trending-down"}}):t._e()],1)])])])],1)])}s._withStripped=!0;var r={data:function(){return{statistics:{loading:!1,thisYear:{order:0,income:0,customer:0},lastYear:{order:0,income:0,customer:0},ratio:{order:0,income:0,customer:0}}}},computed:{thisYearIncome:{get:function(){var t=this.statistics.thisYear.income+"",e=[],a=0,s=t.lastIndexOf("."),r=t.length;for(1<s&&(e.push(t.substr(s)),r=s-1);-1<r;)e.unshift(t[r]),++a%3==0&&1<r&&e.unshift(","),r--;return 0<e.length?e.join(""):""}},thisYearNewOrder:{get:function(){var t=this.statistics.thisYear.order+"",e=[],a=0,s=t.lastIndexOf("."),r=t.length;for(1<s&&(e.push(t.substr(s)),r=s-1);-1<r;)e.unshift(t[r]),++a%3==0&&1<r&&e.unshift(","),r--;return 0<e.length?e.join(""):""}},thisYearNewCustomer:{get:function(){var t=this.statistics.thisYear.customer+"",e=[],a=0,s=t.lastIndexOf("."),r=t.length;for(1<s&&(e.push(t.substr(s)),r=s-1);-1<r;)e.unshift(t[r]),++a%3==0&&1<r&&e.unshift(","),r--;return 0<e.length?e.join(""):""}}},mounted:function(){this.load()},methods:{load:function(){var a=this;this.statistics.loading=!0,API.getstatistics().then(function(t){if(t&&t.data){var e=t.data;a.statistics.thisYear.income=0<e.thisYearIncome?e.thisYearIncome:0,a.statistics.thisYear.order=0<e.thisYearNewOrder?e.thisYearNewOrder:0,a.statistics.thisYear.customer=0<e.thisYearNewCustomer?e.thisYearNewCustomer:0,a.statistics.lastYear.income=0<e.lastYearIncome?e.lastYearIncome:0,a.statistics.lastYear.order=0<e.lastYearNewOrder?e.lastYearNewOrder:0,a.statistics.lastYear.customer=0<e.lastYearNewCustomer?e.lastYearNewCustomer:0,a.statistics.ratio.income=0<e.growthRatioIncome?e.growthRatioIncome:0,a.statistics.ratio.order=0<e.growthRatioNewOrder?e.growthRatioNewOrder:0,a.statistics.ratio.customer=0<e.growthRatioNewCustomer?e.growthRatioNewCustomer:0}}).catch(function(t){return a.$error(t,function(){a.statistics.loading=!1})})}}},i=(a(100),a(0)),n=Object(i.a)(r,s,[],!1,null,"3d0192c4",null);n.options.__file="src/views/core/Dashboard.vue";e.a=n.exports},93:function(t,e,a){"use strict";function s(){var a=this,t=a.$createElement,s=a._self._c||t;return s("div",{staticClass:"layout"},[s("Layout",{staticStyle:{height:"100vh"}},[s("Sider",{attrs:{"collapsed-width":100,collapsible:""},on:{"on-collapse":a.handleSiderCollapse},model:{value:a.isCollapsed,callback:function(t){a.isCollapsed=t},expression:"isCollapsed"}},[s("div",{staticClass:"logo"},[a.isCollapsed?s("img",{attrs:{src:"/statics/images/favicon.png",alt:"奇思妙想工作室"}}):s("h2",[a._v("奇思妙想工作室")])]),a._v(" "),s("Menu",{class:a.menuitemClasses,attrs:{theme:"dark",width:"auto","active-name":a.$route.name,"open-names":a.onames}},a._l(a.menus,function(t){return s("div",{key:t.name},[t.subs?s("Submenu",{attrs:{name:t.name}},[s("template",{slot:"title"},[s("Icon",{attrs:{type:t.icon}}),a._v(" "),s("span",[a._v(a._s(t.label))])],1),a._v(" "),a._l(t.subs,function(t,e){return s("MenuItem",{key:e,attrs:{name:t.name,to:t.to}},[s("Icon",{attrs:{type:t.icon}}),a._v(" "),s("span",[a._v(a._s(t.label))])],1)})],2):s("MenuItem",{attrs:{name:t.name,to:t.to}},[s("Icon",{attrs:{type:t.icon}}),a._v(" "),s("span",[a._v(a._s(t.label))])],1)],1)}),0)],1),a._v(" "),s("Layout",[s("Header"),a._v(" "),s("Content",{staticStyle:{padding:"10px 16px"}},["Dashboard"==a.$route.name?[s("router-view")]:[s("Breadcrumb",{staticStyle:{"margin-bottom":"10px"}},[a._l(a.$route.matched,function(t,e){return[0<e?s("BreadcrumbItem",{key:e},[a._v(a._s(t.meta.label))]):a._e()]})],2),a._v(" "),s("Card",[s("router-view")],1)]],2)],1)],1)],1)}s._withStripped=!0;var r="app-menu-collapsed",i={data:function(){return{isCollapsed:!1}},computed:{rotateIcon:{get:function(){return["menu-icon",this.isCollapsed?"rotate-icon":""]}},menuitemClasses:{get:function(){return["menu-item",this.isCollapsed?"collapsed-menu":""]}},onames:{get:function(){for(var t=this.$route.matched,e=[],a=0;a<t.length;a++){var s=t[a];s&&s.name&&e.push(s.name)}return e}},menus:{get:function(){for(var t=[],e=[],a=this.$router&&this.$router.options&&this.$router.options.routes instanceof Array?this.$router.options.routes:[],s=0;s<a.length;s++){var r=a[s];if(r&&"App"==r.name&&r.children instanceof Array){e=r.children;break}}for(var i=0;i<e.length;i++){var n=e[i];if(n&&n.name&&n.meta.label){var o={to:n.path||"/?t"+Date.now(),name:n.name,icon:n.meta.icon,label:n.meta.label};if(n.children instanceof Array){for(var c=[],l=0;l<n.children.length;l++){var d=n.children[l];if(d&&d.path){var u=d.meta.defaultPath?d.meta.defaultPath:d.path;c.push({to:u,name:d.name,icon:d.meta.icon,label:d.meta.label})}}o.subs=c}t.push(o)}}return t}}},beforeMount:function(){this.isCollapsed=JSON.parse(localStorage.getItem(r))},methods:{handleSiderCollapse:function(){localStorage.setItem(r,this.isCollapsed)}}},n=(a(101),a(0)),o=Object(n.a)(i,s,[],!1,null,"34bd024c",null);o.options.__file="src/views/core/Application.vue";e.a=o.exports},99:function(t,e,a){"use strict";var s=a(30);a.n(s).a}}]);