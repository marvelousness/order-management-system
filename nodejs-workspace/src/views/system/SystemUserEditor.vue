<template>
    <div class="system-user-container">
        <Split style="height: 450px">
            <div slot="left">
                <Form ref="form" :model="user" :rules="rules" :label-width="110">
                    <FormItem prop="name" label="员工姓名">
                        <Input type="text" v-model="user.name" placeholder="请输入员工姓名" />
                    </FormItem>
                    <FormItem prop="gender" label="员工性别">
                        <RadioGroup v-model="user.gender">
                            <Radio label="male">男</Radio>
                            <Radio label="female">女</Radio>
                        </RadioGroup>
                    </FormItem>
                    <FormItem prop="userid" label="员工工号" v-if="inserted">
                        <Input type="text" disabled v-model="user.userid" placeholder="请输入员工的工号，可用作登录" />
                    </FormItem>
                    <FormItem prop="mobile" label="手机号码">
                        <Input type="text" v-model="user.mobile" placeholder="请输入员工手机号码" />
                    </FormItem>
                    <FormItem prop="email" label="电子邮箱">
                        <Input type="text" v-model="user.email" placeholder="请输入员工邮箱" />
                    </FormItem>
                    <FormItem prop="departmentId" label="所属部门">
                        <strong v-if="department.selected.id > 0">{{department.selected.name}}</strong>
                        <i v-else>-- 请在右侧选择该员工所属的部门 --</i>
                    </FormItem>
                    <FormItem prop="isLeader" label="是部门领导？">
                        <RadioGroup v-model="user.isLeader">
                            <Radio label="yes">是</Radio>
                            <Radio label="no">否</Radio>
                        </RadioGroup>
                    </FormItem>
                    <FormItem prop="hiredDate" label="入职时间">
                        <DatePicker type="date" v-model="user.hiredDate" :editable="false" :clearable="false" placeholder="请选择入职时间" style="width: 100%"> </DatePicker>
                    </FormItem>
                </Form>
            </div>
            <div slot="right" class="container-right">
                <Tree :data="department.tree" @on-select-change="handleDepartmentSelectChange" />
            </div>
        </Split>
        

        <Divider orientation="left" size="small" dashed>说明</Divider>
        <Alert type="warning" v-if="inserted">修改用户信息的时候不允许修改密码，仅能修改自己的密码</Alert>
        <Alert type="warning" v-else>新增用户的初始密码的均为: <Tag>888888</Tag></Alert>

    </div>
</template>
<script>

    /**
     * 修正树结构
     * @param {trees} 树数据
     * @param {id} 选中的ID
     */
    function repairTree(trees, id) {
        if(!(trees instanceof Array && trees.length > 0)) {
            return;
        }
        for (let i = 0; i < trees.length; i++) {
            let tree = trees[i];
            if (!(tree && tree.name && tree.id)) {
                continue;
            }
            tree.expand = true;
            tree.title = tree.name;
            tree.selected = tree.id === id;
            tree.children = tree.departments;
            if(tree.children instanceof Array) {
                repairTree(tree.children, id);
            }
        }
    }

    export default {
        props: {
            data: {
                type: Object,
                required: true
            }
        },
        data() {
            return {
                user: {
                    id: 0,
                    name: "",
                    userid: "",
                    mobile: "",
                    email: "",
                    gender: "male",
                    isLeader: "no",
                    hiredDate: "",
                    departmentId: 0
                },
                rules: {
                    name: [{ required: true, message: "员工姓名不允许为空", trigger: 'blur' }],
                    gender: [{ required: true, message: "请选择员工的性别", trigger: 'change' }],
                    isLeader: [{ required: true, message: "该员工是否是指定部门的领导？", trigger: 'change' }],
                    // userid: [{ required: true, message: "员工的工号不允许为空", trigger: 'blur' }],
                    mobile: [{
                        required: true,
                        message: "员工的手机号码不允许为空",
                        trigger: 'blur'
                    }, {
                        validator(rule, value, callback) {
                            if(value && value.trim().length > 0) {
                                if(/^1[3|4|5|6|7|8|9]\d{9}$/.test(value.trim())) {
                                    callback();
                                } else {
                                    callback(new Error('请输入正确格式的手机号码'))
                                }
                            } else {
                                callback("请填写员工的手机号码");
                            }
                        }
                    }],
                    email: [{
                        required: true,
                        message: "员工的邮箱不允许为空",
                        trigger: 'blur'
                    }, {
                        validator(rule, value, callback) {
                            if(value && value.trim().length > 0) {
                                if(/^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value.trim())) {
                                    callback();
                                } else {
                                    callback(new Error('请输入正确格式的邮箱'))
                                }
                            } else {
                                callback("请填写员工的电子邮箱");
                            }
                        }
                    }],
                    hiredDate: [ {
                        validator(rule, value, callback) {
                            if(value) {
                                callback();
                            } else {
                                callback("请选择员工的入职日期");
                            }
                        }
                    }],
                    // hiredDate: [{ required: true, message: "请选择员工的入职日期", trigger: 'change' }]
                    departmentId: [ {
                        required: true,
                        validator(rule, value, callback) {
                            if(value > 0) {
                                callback();
                            } else {
                                callback("请选择员工所属的部门");
                            }
                        }
                    }]
                },
                department: {
                    tree: [],
                    selected: {
                        id: 0,
                        name: ""
                    },
                    loading: false
                },
                pending: false
            };
        },
        computed: {
            /**
             * 表示当前数据是新增还是修改
             */
            inserted: {
                get() {
                    let user = this.user;
                    return user && user.id > 0 ? true : false;
                }
            }
        },
        mounted() {
            this.init();
        },
        methods: {
            init(data) {
                let that = this;
                let user = {};
                let _data = data ? JSON.parse(JSON.stringify(data)) : {};
                user.id = _data.id && _data.id > 0 ? _data.id : 0;
                user.name = _data.name || "";
                user.email = _data.email || "";
                user.userid = _data.userid || "";
                user.mobile = _data.mobile || "";
                user.gender = _data.gender ? "male" : "female";
                user.isLeader = _data.isLeader ? "yes" : "no";
                user.departmentId = _data.departmentId > 0 ? _data.departmentId : 0;
                user.hiredDate = _data.hiredDate || new Date();
                that.user = user;
                if(that.$refs.form && that.$refs.form.resetFields) {
                    // that.$refs.form.resetFields();
                }

                that.department.selected.id = _data.departmentId > 0 ? _data.departmentId : 0
                that.department.selected.name = _data.departmentName;

                if(that.department.tree.length < 1) {
                    that.department.loading = true;
                    API.getDepartmentTree().then((response) => {
                        let deps = response && response.data instanceof Array ? response.data : [];
                        repairTree(deps, that.department.selected.id);
                        that.department.tree = deps;
                        that.department.loading = false;
                    }).catch(err => that.$error(err, () => {
                        that.department.loading = false;
                    }));
                } else {
                    repairTree(that.department.tree, that.department.selected.id);
                }
            },
            submit(cb) {
                let that = this;
                let user = JSON.parse(JSON.stringify(that.user));
                user.gender = user.gender == "female" ? 0 : 1;
                user.isLeader = user.isLeader == "no" ? 0 : 1;
                if(that.user.hiredDate instanceof Date) {
                    user.hiredDate = that.user.hiredDate.format("yyyy-MM-dd HH:mm:ss");
                } else {
                    delete user.hiredDate;
                }
                that.$refs.form.validate((valid) => {
                    if(!valid) {
                        return;
                    }

                    if(!(user.departmentId > 0)) {
                        that.$Message.error("请选择该员工所属的部门");
                        return;
                    }

                    that.pending = true;
                    API.postUserSave(user).then((response) => {
                        let saved = response && response.data > 0
                        if(saved) {
                            that.$Message.info("保存成功！");
                        }
                        cb(saved);
                        that.pending = false;
                    }).catch((err) => that.$error(err, () => that.pending = false));
                });
            },
            handleDepartmentSelectChange(selection) {
                this.department.selected.id = 0;
                this.department.selected.name = "";
                if(selection instanceof Array && selection.length > 0) {
                    let selected = selection[0];
                    if(selected && selected.id > 0 && selected.name) {
                        this.department.selected.id = selected.id;
                        this.department.selected.name = selected.name;
                    }
                }
                this.user.departmentId = this.department.selected.id;
            }
        }
    }
</script>
<style scoped>
.system-user-container {
}
.container-right {
    height: 100%;
    overflow: auto;
}
</style>