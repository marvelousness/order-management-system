<template>
    <div>
        <ButtonGroup>
            <Button type="primary" icon="md-add" @click="handleInsert">新增</Button>
            <!-- <Button icon="md-cloud-download" @click="handleExport">导出</Button> -->
        </ButtonGroup>

        <Table border context-menu show-context-menu :columns="table.columns" :data="table.data" :loading="loading" @on-contextmenu="r => table.crow = r">
            <template slot="footer">
                <Page :total="pager.total" :page-size="pager.size" :current="pager.current" size="small" @on-change="handleTablePageChange" @on-page-size-change="handleTablePageSizeChange" show-total show-sizer/>
            </template>
            <template slot="contextMenu">
                <DropdownItem @click.native="handleContextMenuCreateNewOrder">新建订单</DropdownItem>
                <DropdownItem @click.native="handleContextMenuEdit">编辑</DropdownItem>
                <DropdownItem @click.native="handleContextMenuDelete" style="color: #ed4014">删除</DropdownItem>
            </template>
        </Table>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                loading: false,
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
                        width: 120,
                        title: "旺旺",
                        key: "wangwang",
                        align: "center"
                    }, {
                        width: 120,
                        title: "QQ",
                        key: "qq",
                        align: "center"
                    }, {
                        width: 120,
                        title: "微信",
                        key: "wechat",
                        align: "center"
                    }, {
                        width: 120,
                        title: "电话",
                        key: "phone",
                        align: "center"
                    }, {
                        width: 80,
                        title: "地区",
                        key: "region",
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
                        width: 120,
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
                API.getCustomerList({
                    number: number,
                    size: size
                }).then((response) => {
                    let resp = response && response.data ? response.data : {};
                    that.pager.size = resp.size;
                    that.pager.total = resp.total;
                    that.pager.current = resp.number;
                    that.table.data = resp.rows instanceof Array ? resp.rows : [];
                    that.loading = false;
                }).catch(err => this.$error(err, this.loading = false));
            },
            handleInsert() {
                this.$router.push({
                    name: "CustomerEditor"
                });
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
            }
        }
    }
</script>

<style scoped></style>