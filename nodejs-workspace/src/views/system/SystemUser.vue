<template>
    <div>
        <ButtonGroup>
            <Button type="primary" icon="md-add" @click="handleInsert">新增</Button>
            <!-- <Button icon="md-cloud-download" @click="handleExport">导出</Button> -->
        </ButtonGroup>

        <Modal v-model="model.visible" :title="model.title" :mask-closable="false" :width="900">
            <Editor ref="editor" :data="model.user" />
            <div slot="footer" style="text-align: center">
                <Button type="primary" @click="handleModelSubmit">保存</Button>
            </div>
        </Modal>
        
        <Table border :columns="table.columns" :data="table.data" :loading="loading">
            <template slot="footer">
                <Page :total="pager.total" :page-size="pager.size" :current="pager.current" size="small" @on-change="handleTablePageChange" @on-page-size-change="handleTablePageSizeChange" show-total show-sizer/>
            </template>
        </Table>

    </div>
</template>

<script>
    import Editor from "./SystemUserEditor";
    export default {
        components: {
            Editor
        },
        data() {
            return {
                loading: false,
                model: {
                    visible: false,
                    user: {},
                    title: "新增&修改用户"
                },
                table: {
                    columns: [{
                        width: 70,
                        type: "index",
                        title: "序号",
                        align: "center"
                    }, {
                        width: 130,
                        title: "工号",
                        key: "userid",
                        align: "center"
                    }, {
                        width: 120,
                        title: "姓名",
                        key: "name",
                        align: "center"
                    }, {
                        width: 70,
                        title: "性别",
                        key: "gender",
                        align: "center",
                        render(h, params) {
                            return h('span', params.row.gender == 0 ? "女" : "男");
                        }
                    }, {
                        width: 80,
                        title: "状态",
                        key: "gender",
                        align: "center",
                        render(h, params) {
                            return h('span', params.row.disabled ? "停用" : "启用");
                        }
                    }, {
                        width: 120,
                        title: "部门名称",
                        key: "departmentName",
                        align: "center"
                    }, {
                        width: 70,
                        title: "领导",
                        align: "center",
                        render(h, params) {
                            return h('span', params.row.isLeader == 0 ? "否" : "是");
                        }
                    }, {
                        width: 140,
                        title: "手机号码",
                        key: "mobile",
                        align: "center"
                    }, {
                        width: 120,
                        title: "电子邮箱",
                        key: "email",
                        align: "center"
                    }, {
                        width: 120,
                        title: "入职日期",
                        key: "hiredDate",
                        align: "center"
                    }, {
                        width: 180,
                        title: "创建时间",
                        key: "createTime",
                        align: "center"
                    }, {
                        width: 220,
                        title: "操作",
                        align: "center",
                        render: (h, params) => {
                            let label = params && params.row && params.row.disabled ? "启用" : "停用";
                            if(params && params.row && params.row.id < 2) {
                                return h('span', '管理员不允许被操作');
                            }
                            return h('div', [ h('Button', {
                                props: {
                                    type: 'info',
                                    size: 'small'
                                },
                                on: {
                                    click: () => this.hanleTableRowEdit(params.row)
                                }
                            }, "编辑"), h('Button', {
                                props: {
                                    type: 'warning',
                                    size: 'small'
                                },
                                on: {
                                    click: () => this.hanleTableRowDisable(params.row)
                                }
                            }, label), h('Button', {
                                props: {
                                    type: 'error',
                                    size: 'small'
                                },
                                on: {
                                    click: () => this.hanleTableRowDelete(params.row)
                                }
                            }, "删除")]);
                        }
                    }],
                    data: []
                },
                pager: {
                    current: 1,
                    size: 10,
                    total: 0
                }
            };
        },
        mounted() {
            this.load(this.pager.current, this.pager.size);
        },
        methods: {
            load(number, size) {
                if(this.loading) {
                    return;
                }
                let that = this;
                that.loading = true;
                API.getUserList({
                    number: number,
                    size: size
                }).then((response) => {
                    let resp = response && response.data ? response.data : {};
                    that.pager.size = resp.size;
                    that.pager.total = resp.total;
                    that.pager.current = resp.number;
                    that.table.data = resp.rows instanceof Array ? resp.rows : [];
                    that.loading = false;
                }).catch(err => that.$error(err, that.loading = false));
            },
            handleInsert() {
                this.$refs.editor.init({});
                this.model.visible = true;
                this.model.title = "新增用户";
            },
            handleModelSubmit() {
                this.$refs.editor.submit(() => this.model.visible = false);
                this.load(this.pager.current, this.pager.size);
            },
            hanleTableRowEdit(row) {
                this.$refs.editor.init(row);
                this.model.visible = true;
                this.model.title = "编辑用户";
            },
            hanleTableRowDisable(row) {
                let name = row && row.name ? row.name : "";
                let id = row && row.id > 0 ? row.id : 0;
                if(!(name && id > 0)) {
                    return;
                }
                let that = this;
                let disabled = row.disabled ? true : false;
                let title = "确定" + (disabled ? "启用" : "停用") + name + "的账号？";
                let close = () => {
                    setTimeout(() => that.$Modal.remove(), 500);
                    this.load(this.pager.current, this.pager.size);
                };
                that.$Modal.confirm({
                    loading: true,
                    title: title,
                    onOk: () => {
                        let p = disabled ? API.postUserStatusEnable({
                            id: id
                        }) : API.postUserStatusDisable({
                            id: id
                        });
                        p.then(close).catch((err) => that.$error(err, close));
                    }
                });
            },
            hanleTableRowDelete(row) {
                let name = row && row.name ? row.name : "";
                let id = row && row.id > 0 ? row.id : 0;
                if(!(name && id > 0)) {
                    return;
                }
                let that = this;
                let title = "确定删除" + name + "的账号？";
                let close = () => {
                    setTimeout(() => {
                        that.$Modal.remove();
                        let number = that.pager.current;
                        if(that.table.data.length < 2 && number > 1) {
                            number--;
                        }
                        that.load(number, that.pager.size);
                    }, 500);
                };
                that.$Modal.confirm({
                    loading: true,
                    title: title,
                    onOk: () => {
                        API.postUserDelete({
                            id: id
                        }).then(close).catch((err) => that.$error(err, close));
                    }
                });
            },
            handleTablePageChange(number) {
                this.load(number, this.pager.size);
            },
            handleTablePageSizeChange(size) {
                this.load(this.pager.current, size);
            },
            handleExport() {
                this.$Message.info("导出功能正在开发中");
            }
        }
    }
</script>

<style scoped></style>