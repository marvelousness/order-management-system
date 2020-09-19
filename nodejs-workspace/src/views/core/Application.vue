<template>
    <div class="layout">
        <Layout style="height: 100vh">
            <Sider :collapsed-width="100" v-model="isCollapsed" @on-collapse="handleSiderCollapse" collapsible>
                
                <div class="logo">
                    <img src="/statics/images/favicon.png" alt="奇思妙想工作室" v-if="isCollapsed" />
                    <h2 v-else>奇思妙想工作室</h2>
                </div>

                <Menu theme="dark" width="auto" :active-name="$route.name" :open-names="onames" :class="menuitemClasses">
                    <div v-for="menu in menus" :key="menu.name">
                        <Submenu v-if="menu.subs" :name="menu.name">
                            <template slot="title">
                                <Icon :type="menu.icon" />
                                <span>{{menu.label}}</span>
                            </template>
                            <MenuItem v-for="(m, y) in menu.subs" :name="m.name" :to="m.to" :key="y">
                                <Icon :type="m.icon" />
                                <span>{{m.label}}</span>
                            </MenuItem>
                        </Submenu>
                        <MenuItem :name="menu.name" :to="menu.to" v-else>
                            <Icon :type="menu.icon" />
                            <span>{{menu.label}}</span>
                        </MenuItem>
                    </div>
                </Menu>

            </Sider>
            <Layout>
                
                <Header />

                <Content style="padding: 10px 16px">
                    <template v-if="$route.name == 'Dashboard'">
                        <router-view></router-view>
                    </template>
                    <template v-else>
                        <Breadcrumb style="margin-bottom: 10px" >
                            <template v-for="(m, i) in $route.matched">
                                <BreadcrumbItem :key="i" v-if="i > 0">{{m.meta.label}}</BreadcrumbItem>
                            </template>
                        </Breadcrumb>
                        <Card>
                            <router-view></router-view>
                        </Card>
                    </template>
                </Content>
            </Layout>
        </Layout>
    </div>
</template>
<script>
    const APP_KEY = "app-menu-collapsed";
    export default {
        data () {
            return {
                isCollapsed: false
            }
        },
        computed: {
            rotateIcon: {
                get() {
                    return ['menu-icon', this.isCollapsed ? 'rotate-icon' : ''];
                }
            },
            menuitemClasses: {
                get() {
                    return ['menu-item', this.isCollapsed ? 'collapsed-menu' : '']
                }
            },
            /**
             * 左侧菜单被打开的菜单名称的集合
             */
            onames: {
                get() {
                    let matched = this.$route.matched;
                    let os = [];
                    for(let i = 0; i < matched.length; i++) {
                        let m = matched[i];
                        if(m && m.name) {
                            os.push(m.name);
                        }
                    }
                    return os;
                }
            },
            /**
             * 左侧菜单的集合
             */
            menus: {
                get() {
                    let menus = [];
                    let routes = [];
                    // 查找 App 组件
                    {
                        let rs = this.$router && this.$router.options && this.$router.options.routes instanceof Array ? this.$router.options.routes : [];
                        for(let i = 0; i < rs.length; i++) {
                            let route = rs[i];
                            if(route && route.name == "App" && route.children instanceof Array) {
                                routes = route.children;
                                break;
                            }
                        }
                    }
                    // 根据路由组件左侧菜单的数据源
                    for(let i = 0; i < routes.length; i++) {
                        let route = routes[i];
                        if(!(route && route.name && route.meta.label)) {
                            continue;
                        }
                        // 父级菜单
                        let menu = {
                            to: route.path || "/?t" + Date.now(),
                            name: route.name,
                            icon: route.meta.icon,
                            label: route.meta.label
                        };
                        if(route.children instanceof Array) {
                            // 子级菜单
                            let subs = [];
                            for(let j = 0; j < route.children.length; j++) {
                                let r = route.children[j];
                                if(!(r && r.path)) {
                                    continue;
                                }
                                let path = r.meta.defaultPath ? r.meta.defaultPath : r.path;
                                subs.push({
                                    to: path,
                                    name: r.name,
                                    icon: r.meta.icon,
                                    label: r.meta.label
                                });
                            }
                            menu.subs = subs;
                        }
                        menus.push(menu);
                    }
                    return menus;
                }
            }
        },
        beforeMount() {
            this.isCollapsed = JSON.parse(localStorage.getItem(APP_KEY));
        },
        methods: {
            handleSiderCollapse () {
                // 记住折叠的状态
                localStorage.setItem(APP_KEY, this.isCollapsed);
            }
        }
    }
</script>
<style scoped>
    .logo {
        text-align: center;
    }
    .logo h2 {
        color: #FFF;
        text-align: center;
        line-height: 3em;
    }
    .logo img {
        width: 50%;
    }
    .layout-con{
        height: 100%;
        width: 100%;
    }
    .menu-item span{
        display: inline-block;
        overflow: hidden;
        vertical-align: bottom;
        transition: width .2s ease .2s;
    }
    .menu-item i{
        transform: translateX(0px);
        transition: font-size .2s ease, transform .2s ease;
        vertical-align: middle;
        font-size: 16px;
    }
    .collapsed-menu span{
        display: none;
        transition: display .2s ease;
    }
    .collapsed-menu i{
        transform: translateX(5px);
        transition: font-size .2s ease .2s, transform .2s ease .2s;
        vertical-align: middle;
        font-size: 22px;
    }
</style>