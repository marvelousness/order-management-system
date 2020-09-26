<template>
    <div>

        <div class="tool-bar">
            <Button icon="md-cloud-download" @click="exporter.visible = true">导出</Button>
            <div class="tool-searcher">
                <Input search v-model="searcher.word" @on-search="load(1)" enter-button="搜索" placeholder="请输入客户的名称、昵称、微信、电话等信息进行检索" />
            </div>
        </div>

        <Table border context-menu show-context-menu ref="table" :columns="table.columns" :data="table.data" :loading="loading" @on-contextmenu="r => table.crow = r">
            <template slot="footer">
                <Page :total="pager.total" :page-size="pager.size" :current="pager.current" size="small" @on-change="handleTablePageChange" @on-page-size-change="handleTablePageSizeChange" show-total show-sizer/>
            </template>
            <template slot="contextMenu">
                <DropdownItem @click.native="handleContextMenuCreateNewOrder">新建订单</DropdownItem>
                <DropdownItem @click.native="handleContextMenuEdit">编辑</DropdownItem>
                <DropdownItem @click.native="handleContextMenuDelete" style="color: #ed4014">删除</DropdownItem>
            </template>
        </Table>

        <Modal title="导出选项" v-model="exporter.visible" class-name="vertical-center-modal">
            <RadioGroup v-model="exporter.option">
                <Radio label="current">当前页的列表</Radio>
                <Radio label="whole">完整的列表</Radio>
            </RadioGroup>
            <div class="modal-footer" slot="footer">
                <Button type="primary" @click="handleExport">确认</Button>
            </div>
        </Modal>
        
    </div>
</template>

<script>
    export default {
        data() {
            return {
                loading: false,
                searcher: {
                    word: ""
                },
                table: {
                    columns: [{
                        width: 70,
                        type: "index",
                        title: "序号",
                        align: "center"
                    }, {
                        width: 120,
                        title: "名称",
                        key: "name",
                        align: "center"
                    }, {
                        width: 120,
                        title: "昵称",
                        key: "nick",
                        align: "center"
                    }, {
                        width: 140,
                        title: "旺旺",
                        key: "wangwang",
                        align: "center"
                    }, {
                        width: 140,
                        title: "QQ",
                        key: "qq",
                        align: "center"
                    }, {
                        width: 140,
                        title: "微信",
                        key: "wechat",
                        align: "center"
                    }, {
                        width: 140,
                        title: "电话",
                        key: "phone",
                        align: "center"
                    }, {
                        width: 80,
                        title: "地区",
                        key: "region",
                        align: "center"
                    }, {
                        width: 170,
                        title: "创建时间",
                        key: "createTime",
                        align: "center"
                    }, {
                        width: 80,
                        title: "创建人",
                        key: "creatorName",
                        align: "center"
                    }, {
                        width: 120,
                        title: "创建人部门",
                        key: "creatorDepartmentName",
                        align: "center"
                    }, {
                        width: 200,
                        title: "备注",
                        key: "remark"
                    }],
                    data: [],
                    /**
                     * 在右键菜单中，对应的行的数据， contextmenu row 缩写为 crow
                     */
                    crow: {}
                },
                pager: {
                    current: 1,
                    size: 10,
                    total: 0
                },
                exporter: {
                    visible: false,
                    option: "current"
                }
            };
        },
        mounted() {
            this.load(this.pager.current, this.pager.size);
        },
        methods: {
            load(number, size) {
                number = number > 0 ? number : this.pager.number;
                size = size > 0 ? size : this.pager.size;
                let that = this;
                that.loading = true;
                API.getCustomerList({
                    number: number,
                    size: size,
                    keyword: this.searcher.word
                }).then((response) => {
                    let resp = response && response.data ? response.data : {};
                    that.pager.size = resp.size;
                    that.pager.total = resp.total;
                    that.pager.current = resp.number;
                    that.table.data = resp.rows instanceof Array ? resp.rows : [];
                    that.loading = false;
                }).catch(err => this.$error(err, this.loading = false));
            },
            hanleTableRowEdit(row) {
                this.$router.push({
                    name: "CustomerEditor",
                    params: {
                        id: row.id
                    }
                });
            },
            hanleTableRowDelete(row) {
            },
            handleTablePageChange(number) {
                this.load(number, this.pager.size);
            },
            handleTablePageSizeChange(size) {
                this.load(this.pager.current, size);
            },
            handleContextMenuEdit() {
                let row = this.table.crow;
                if(!(row && row.id)) {
                    this.$Message.error("请选择需要操作的客户");
                    return;
                }
                this.$router.push({
                    name: "CustomerEditor",
                    params: {
                        id: row.id
                }});
            },
            handleContextMenuDelete() {
                let row = this.table.crow;
                if(!(row && row.id)) {
                    this.$Message.error("请选择需要操作的客户");
                    return;
                }
                let name = row && row.name ? row.name : "";
                let id = row && row.id > 0 ? row.id : 0;
                if(!(id > 0)) {
                    return;
                }
                let that = this;
                let title = "确定删除" + (name ? "【" + name + "】" : "该") + "的客户信息";
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
                        API.postCustomerDelete({
                            id: id
                        }).then(close).catch((err) => that.$error(err, close));
                    }
                });
            },
            handleContextMenuCreateNewOrder() {
                let row = this.table.crow;
                if(!(row && row.id)) {
                    this.$Message.error("请选择需要操作的客户");
                    return;
                }
                this.$router.push({
                    name: "OrderEditor",
                    params: {
                        customer: row.id
                }});
            },
            handleExport() {
                if(this.exporter.option == "current") {
                    this.$refs.table.exportCsv({
                        filename: new Date().format("客户列表-yyyyMMddHHmmss-") + this.pager.current
                    });
                    this.exporter.visible = false;
                    return;
                }
                this.$Message.info("客户数据导出功能暂未开放!");
            }
        }
    }
</script>

<style scoped>
    .tool-searcher {
        display: inline-block;
        width: 35em;
        vertical-align: middle;
    }
    .tool-bar {
        display: flex;
        justify-content: space-between;
        margin-bottom: 2px;
    }
</style>