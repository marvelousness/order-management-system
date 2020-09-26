<template>
    <div>
        
        <div class="tool-bar">
            <ButtonGroup>
                <Button type="info" icon="md-add" @click="handleDistribute">分配订单</Button>
                <Button icon="md-cloud-download" @click="exporter.visible = true">导出</Button>
            </ButtonGroup>
            <div class="tool-searcher">
                <Input search v-model="searcher.word" @on-search="load(1)" enter-button="搜索" placeholder="请输入客户的名称、昵称、微信、电话等信息进行检索" />
            </div>
        </div>

        <Table border context-menu show-context-menu ref="table" :columns="table.columns" :data="table.data" :loading="loading" @on-contextmenu="r => table.crow = r" @on-selection-change="s => table.selection = s">
            <template slot="footer">
                <Page :total="pager.total" :page-size="pager.size" :current="pager.current" size="small" @on-change="handleTablePageChange" @on-page-size-change="handleTablePageSizeChange" show-total show-sizer/>
            </template>
            <template slot="contextMenu">
                <DropdownItem @click.native="handleContextMenuDistribute">分配订单</DropdownItem>
                <DropdownItem @click.native="handleContextMenuView">查看详情</DropdownItem>
                <!-- <DropdownItem @click.native="handleContextMenuDelete" style="color: #ed4014">删除</DropdownItem> -->
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

        <Modal title="订单分配" v-model="allocator.visible" class-name="vertical-center-modal">
            <div class="modal-body">
                <Spin fix v-if="allocator.loading">加载中...</Spin>
                <AutoComplete v-model="allocated" icon="ios-search" placeholder="请输入姓名" class="modal-auto-complete" v-else>
                    <div class="demo-auto-complete-item" v-for="(item, i) in allocator.options" :key="i">
                        <div class="demo-auto-complete-group">
                            <span>{{ item.title }}</span>
                        </div>
                        <Option v-for="option in item.children" :value="option" :key="option.title">
                            <span class="demo-auto-complete-title">{{ option.title }}</span>
                        </Option>
                    </div>
                </AutoComplete>
            </div>
            <div class="modal-footer" slot="footer">
                <Button type="primary" :loading="allocator.pending" @click="handleAllocate">确认</Button>
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
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    }, {
                        width: 70,
                        type: "index",
                        title: "序号",
                        align: "center"
                    }, {
                        width: 120,
                        title: "客户名称",
                        key: "customerName",
                        align: "center"
                    }, {
                        width: 120,
                        title: "订单总金额",
                        key: "amount",
                        align: "center"
                    }, {
                        width: 120,
                        title: "已付总金额",
                        key: "amount",
                        align: "center"
                    }, {
                        width: 170,
                        title: "成交时间",
                        key: "dealTime",
                        align: "center"
                    }, {
                        width: 150,
                        title: "订单创建人",
                        key: "creatorName",
                        align: "center"
                    }, {
                        width: 150,
                        title: "订单创建人部门",
                        key: "creatorDepartmentName",
                        align: "center"
                    }, {
                        width: 150,
                        title: "订单执行人",
                        key: "executorName",
                        align: "center"
                    }, {
                        width: 150,
                        title: "订单执行人部门",
                        key: "executorDepartmentName",
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
                    crow: {},
                    /**
                     * 表示在列表中被勾选的行的集合
                     */
                    selection: []
                },
                pager: {
                    current: 1,
                    size: 10,
                    total: 0
                },
                exporter: {
                    visible: false,
                    option: "current"
                },
                allocator: {
                    loading: false,
                    // 订单编号
                    numbers: [],
                    title: "",
                    value: "",
                    options: [],
                    visible: false
                }
            };
        },
        computed: {
            allocated: {
                get() {
                    return this.allocator.title ? this.allocator.title : "";
                },
                set(v) {
                    if(v && v.value && v.title) {
                        this.allocator.value = v.value;
                        this.allocator.title = v.title;
                    }
                }
            }
        },
        watch: {
            "allocator.visible": {
                handler(nv, ov) {
                    if(nv == true && this.allocator.options.length < 1) {
                        this.allocator.loading = true;
                        API.getUserAutoCompleteOptions().then((response) => {
                            this.allocator.options = response && Array.isArray(response.data) ? response.data : [];
                            this.allocator.loading = false;
                        }).catch(() => this.allocator.loading = false);
                    }
                }
            }
        },
        mounted() {
            this.load();
        },
        methods: {
            /**
             * 加载列表
             * @param{Integer} number 页码数
             * @param{Integer} size 页项数
             */
            load(number, size) {
                if(this.loading) {
                    return;
                }
                number = number > 0 ? number : this.pager.number;
                size = size > 0 ? size : this.pager.size;
                let that = this;
                that.loading = true;
                API.getOrderList({
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
            handleTablePageChange(number) {
                this.load(number, this.pager.size);
            },
            handleTablePageSizeChange(size) {
                this.load(this.pager.current, size);
            },
            handleExport() {
                if(this.exporter.option == "current") {
                    this.$refs.table.exportCsv({
                        filename: new Date().format("订单列表-yyyyMMddHHmmss-") + this.pager.current
                    });
                    this.exporter.visible = false;
                    return;
                }
                this.$Message.info("订单数据导出功能暂未开放!");
            },
            handleAllocate() {
                let numbers = this.allocator.numbers;
                let executor = parseInt(this.allocator.value);
                if(numbers.length < 1) {
                    this.$Message.error("请选择需要分配的订单");
                    return;
                }
                if(!(executor > 0)) {
                    this.$Message.error("请选择分配的对象");
                    return;
                }
                this.allocator.pending = true;
                API.postOrderDistribute({
                    numbers: numbers.join(","),
                    executor: executor
                }).then(() => {
                    this.$Message.success("分配成功");
                    this.load();
                    this.allocator.pending = false;
                    this.allocator.visible = false;
                }).catch(err => this.$error(err, () => {
                    this.allocator.pending = false;
                }));
            },
            handleDistribute() {
                let numbers = [];
                let executor = 1;
                let i = 0;
                let rows = this.table.selection;
                while (i < rows.length) {
                    let row = rows[i];
                    if(row && row.number) {
                        numbers.push(row.number);
                    }
                    i++;
                }
                if(numbers.length < 1) {
                    this.$Message.error("请选择需要分配的订单");
                    return;
                }
                this.allocator.numbers = numbers;
                this.allocator.visible = true;
            },
            /**
             * 右键分配订单
             */
            handleContextMenuDistribute() {
                this.allocator.numbers = [this.table.crow.number];
                this.allocator.visible = true;
            },
            /**
             * 右键查看订单
             */
            handleContextMenuView() {
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
    .vertical-center-modal {
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .vertical-center-modal .ivu-modal {
        top: 0;
    }
    .modal-body {
        position: relative;
        text-align: center;
    }
    .modal-auto-complete {
        width: 80%;
        text-align: left;
    }
    .modal-footer {
        text-align: center;
    } 
</style>