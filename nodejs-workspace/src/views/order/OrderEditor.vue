<template>
    <div class="order-editor">
        <Spin fix v-if="loading">加载中...</Spin>

        <Card title="客户信息" class="customer-info">
            <Form :model="customer" :label-width="120">
                <Row>
                    <Col span="6">
                        <FormItem label="名称：">{{customer.name}}</FormItem>
                    </Col>
                    <Col span="6">
                        <FormItem label="昵称：">{{customer.nick}}</FormItem>
                    </Col>
                    <Col span="6">
                        <FormItem label="旺旺：">{{customer.wangwang}}</FormItem>
                    </Col>
                    <Col span="6">
                        <FormItem label="地域：">{{customer.region}}</FormItem>
                    </Col>
                </Row>
                <Row>
                    <Col span="6">
                        <FormItem label="手机：">{{customer.phone}}</FormItem>
                    </Col>
                    <Col span="6">
                        <FormItem label="微信：">{{customer.wechat}}</FormItem>
                    </Col>
                    <Col span="6">
                        <FormItem label="QQ：">{{customer.qq}}</FormItem>
                    </Col>
                </Row>
                <Row>
                    <Col span="24">
                        <FormItem label="备注内容：">{{customer.remark}}</FormItem>
                    </Col>
                </Row>
            </Form>
        </Card>

        <Divider />

        <Form :model="form" :label-width="120" ref="form">
        
            <Row>
                <Col span="12">
                    <FormItem label="订单总金额：" prop="amount" :rules="rules.amount">

                        <InputNumber v-model="form.amount" placeholder="请输入订单总金额"
                        :min="0" :max="100000000000000000000" :step="0.1" 
                        :formatter="value => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\$\s?|(,*)/g, '')"
                        style="min-width: 20em" />

                    </FormItem>
                </Col>
                <Col span="12">
                    <FormItem label="成交时间：" prop="dealTime" :rules="rules.dealTime">
                        <DatePicker v-model="form.dealTime" type="datetime" :editable="false" :clearable="false" placeholder="请选择成交时间" />
                    </FormItem>
                </Col>
            </Row>


            <Collapse :value="collapses" class="order-payment-collapse">
                <Panel v-for="(p, i) in form.payments" :key="i">
                    <span v-if="collapses.length > 1">第 {{i + 1}} 笔</span>
                    <span>付款信息</span>
                    <Card slot="content">

                        <Row>
                            <Col span="12">
                                <FormItem label="付款金额：" :prop="'payments[' + i + '].amount'" :rules="rules.payment.amount">
                                    <InputNumber v-model="p.amount" placeholder="请输入付款金额"
                                    :min="0" :max="100000000000000000000" :step="0.1" 
                                    :formatter="value => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                    :parser="value => value.replace(/\$\s?|(,*)/g, '')"
                                    style="min-width: 20em" />
                                </FormItem>
                            </Col>
                            <Col span="12">
                                <FormItem label="付款方式：" :prop="'payments[' + i + '].paymentWay'" :rules="rules.payment.paymentWay">
                                    <Select v-model="p.paymentWay" filterable allow-create @on-create="handlePaymentWayCreated">
                                        <Option v-for="pay in payments" :value="pay" :key="pay">{{ pay }}</Option>
                                    </Select>
                                </FormItem>
                            </Col>
                        </Row>

                        <Row>
                            <Col span="12">
                                <FormItem label="付款账号：" :prop="'payments[' + i + '].paymentAccount'" :rules="rules.payment.paymentAccount">
                                    <Input v-model="p.paymentAccount" laceholder="请输入付款账号" />
                                </FormItem>
                            </Col>
                            <Col span="12">
                                <FormItem label="付款时间：" :prop="'payments[' + i + '].paymentTime'" :rules="rules.payment.paymentTime">
                                    <DatePicker v-model="p.paymentTime" type="datetime" :editable="false" :clearable="false" placeholder="请选择付款时间" />
                                </FormItem>
                            </Col>
                        </Row>

                        <Row>
                            <Col span="24">
                                <FormItem label="付款凭证：" :prop="'payments[' + i + '].paymentProof'" :rules="rules.payment.paymentProof">
                                    <div class="order-payment-item">
                                        <div class="order-payment-container">
                                            <img :src="p.paymentProof" v-show="p.paymentProof">
                                            <div class="order-payment-container-cover">
                                                <Icon type="ios-eye-outline" @click.native="handlePaymentView(p.paymentProof)"></Icon>
                                                <Icon type="ios-trash-outline" @click.native="p.remove"></Icon>
                                            </div>
                                        </div>
                                        <Tabs value="local" class="order-payment-tabs">
                                            <TabPane label="本地" name="local">
                                                <Upload :format="p.format" 
                                                :show-upload-list="false"
                                                :max-size="10240"
                                                action="/fs/uploader/image"
                                                :on-success="(r,f) => p.success(r,f)"
                                                :on-format-error="handlePaymentFormatError"
                                                :on-exceeded-size="handlePaymentExceededSize"
                                                style="display: inline-block;width:100%;">
                                                    <Button icon="ios-cloud-upload-outline">选择本地文件</Button>
                                                </Upload>
                                            </TabPane>
                                            <TabPane label="网络" name="net">
                                                <Input v-model="p.paymentProof" laceholder="请输入图片的网络地址" />
                                            </TabPane>
                                        </Tabs>
                                    </div>
                                </FormItem>
                            </Col>
                        </Row>

                    </Card>
                </Panel>
            </Collapse>

            <FormItem label="已付总金额：" prop="paidAmount" :rules="rules.paidAmount">

                <InputNumber :value="paidAmount" readonly
                :formatter="value => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                :parser="value => value.replace(/\$\s?|(,*)/g, '')"
                style="min-width: 20em" />
                
                <Tooltip content="点击此处新增一笔新付款信息" placement="top">
                    <Badge :count="form.payments.length">
                        <Button icon="md-add" @click="handlePaymentAdd"></Button>
                    </Badge>
                </Tooltip>
                
            </FormItem>

            <FormItem label="备注内容：" style="margin-top:20px">
                <Input v-model="form.remark" type="textarea" :autosize="{minRows: 5,maxRows: 10}" placeholder="请输入备注内容" />
            </FormItem>

            <FormItem style="margin-top:20px">
                <Button type="primary" size="large" :loading="padding" @click="submit">保存</Button>
            </FormItem>
        </Form>


        <Modal title="预览付款凭证" v-model="modal.visible">
            <img :src="modal.src" v-if="modal.visible" style="width: 100%">
        </Modal>

    </div>
</template>

<script>

    class OrderPayment {
        constructor() {
            
            this.amount = 0,
            this.paymentWay = "";
            this.paymentTime = "";
            this.paymentProof = "";
            this.paymentAccount = "";

            this.uploader = {};
        }

        /**
         * 订单付款凭证的文件格式
         */
        get format() {
            return ["jpg", "jgep", "png"];
        }

        success(res, files) {
            if(res instanceof Array && res.length > 0) {
                // 因为这里是单文件上传，故，只需要获取第一个即可
                let uploader = res[0];
                if(uploader && uploader.url) {
                    this.uploader = uploader;
                    this.paymentProof = uploader.url;
                }
            }
        }

        remove() {
            this.paymentProof = "";
        }
    }

    export default {
        name: "OrderEditor",
        data() {
            return {
                customer: {},
                form: {
                    customerId: 0,
                    amount: 0,
                    dealTime: "",
                    remark: "", 
                    payments: [],
                    paidAmount: 0
                },
                rules: {
                    amount: [{
                        required: true,
                        message: "订单总金额不允许为空"
                    }, {
                        validator(rule, value, callback) {
                            if(value >= 0) {
                                callback();
                            } else {
                                callback(new Error("订单总金额不允许输入负数"));
                            }
                        }
                    }],
                    dealTime: [{
                        required: true,
                        message: "请选择成交时间"
                    }],
                    payment: {
                        amount: [{
                            required: true,
                            message: "请输入付款金额"
                        }],
                        paymentWay: [{
                            required: true,
                            message: "请选择付款方式"
                        }],
                        paymentTime: [{
                            required: true,
                            message: "请选择付款时间"
                        }],
                        paymentProof: [{
                            required: true,
                            message: "上传凭证不允许为空，请上传付款凭证的图片，或者录入网络图片地址"
                        }],
                        paymentAccount: [{
                            required: true,
                            message: "请输入付款账号"
                        }]
                    },
                    paidAmount: [{
                        validator: (rule, value, callback) => {
                            let paid = value;
                            let amount = this.form.amount;
                            if(paid >= 0) {
                                let diff = paid - amount;
                                if(diff > 0) {
                                    callback(new Error("订单实付金额大于订单总金额，相差 " + diff  + " 元，请检查您输入的数据"));
                                } else {
                                    callback();
                                }
                            } else {
                                callback(new Error("订单总金额不允许输入负数"));
                            }
                        }
                    }]
                },
                payments: ["信用卡", "储蓄卡", "支付宝", "QQ支付", "微信支付", "苹果支付", "现金支付"],
                modal: {
                    src: "",
                    visible: false
                },
                padding: false,
                loading: false
            };
        },
		computed: {
            /**
             * 客户ID, customer id 缩写为 cid
             */
			cid: {
				get() {
                    let pcid = this.$route.params.customer;
                    let fcid = this.form.customer;
					return fcid ? fcid : pcid;
				}
            },
            /**
             * 默认情况下使得 折叠面板处于展开状态
             */
            collapses: {
                get() {
                    let cs = [];
                    let i = 0;
                    while (i < this.form.payments.length) {
                        cs.push(i++);
                    }
                    return cs;
                }
            },
            /**
             * 已付总金额
             */
            paidAmount: {
                get() {
                    let amount = 0;
                    let payments = this.form.payments;
                    let i = 0;
                    while (i < payments.length) {
                        let payment = payments[i];
                        amount += payment && payment.amount > 0 ? payment.amount : 0;
                        i++;
                    }
                    this.form.paidAmount = amount;
                    return amount;
                }
            }
		},
        mounted() {
            if(!this.cid) {
                this.$Message.warning("请选择需要船创建订单的客户对象！");
                this.$router.push({
                    name: "CustomerList"
                });
                return;
            }
            this.handlePaymentAdd();
            this.load();
        },
        methods: {
            load() {
                this.loading = true;

                let that = this;
                let ps = [new Promise(function(resolve) {
                    API.postOrderPaymentWay().then((response) => {
                        let ways = response && response.data instanceof Array ? response.data : [];
                        let i = 0;
                        while (i < ways.length) {
                            that.handlePaymentWayCreated(ways[i]);
                            i++;
                        }
                        resolve(true);
                    }).catch(() => resolve(false));
                }), new Promise(function(resolve) {
					if(!that.cid) {
						resolve(false);
						return;
					}
                    API.getCustomer({
						id: that.cid
					}).then((response) => {
						that.customer = response && response.data ? response.data : {};
                        resolve(true);
                    }).catch((err) => {
                        that.$error(err);
                        resolve(false);
                    });
                })];

                this.loading = true;
                Promise.all(ps).then(() => this.loading = false);

            },
            handlePaymentAdd() {
                this.form.payments.push(new OrderPayment());
            },
            handlePaymentWayCreated(val) {
                let v = (val ? val + "" : "").trim()
                if(val && this.payments.indexOf(val) < 0) {
                    this.payments.push(val);
                }
            },
            handlePaymentFormatError(file) {
                this.$Notice.warning({
                    title: "文件格式不正确",
                    desc: "文件 " + file.name + " 的格式不正确，建议上传 png 文件"
                });
            },
            handlePaymentExceededSize(file) {
                this.$Notice.warning({
                    title: "超出文件上限",
                    desc: "文件  " + file.name + " 过大，请选择不大于 10MB 的文件"
                });
            },
            handlePaymentView(url) {
                if(url) {
                    this.modal.src = url;
                    this.modal.visible = true;
                } else {
                    this.$Message.warning("暂无可预览的付款凭证，请上传付款凭证！");
                }
            },
            submit() {
                if(this.padding) {
                    return;
                }
                let that = this;
                that.$refs.form.validate((valid) => {
                    if(valid !== true) {
                        that.$Message.error("请完善表单信息！");
                        return;
                    }
                    let paidAmount = this.paidAmount;
                    let form = this.form;
                    let payments = [];
                    let order = {
                        customerId: this.cid,
                        amount: form.amount,
                        remark: form.remark
                    };
                    let i = 0;
                    if(form.dealTime instanceof Date) {
                        order.dealTime = form.dealTime.format("yyyy-MM-dd HH:mm:ss");
                    }
                    while (i < form.payments.length) {

                        let payment = form.payments[i];
                        let payer = {
                            amount: payment.amount,
                            paymentWay: payment.paymentWay,
                            paymentProof: payment.paymentProof,
                            paymentAccount: payment.paymentAccount
                        };
                        if(payment.paymentTime instanceof Date) {
                            payer.paymentTime = payment.paymentTime.format("yyyy-MM-dd HH:mm:ss");
                        }
                        payments.push(payer);
                        i++;
                    }

                    if(payments.length < 1) {
                        that.$Message.error("请录入付款信息！");
                        return;
                    }
                    order.payments = payments;

                    that.padding = true;
                    API.postOrderSave(order).then((response) => {
                        that.padding = false;
                    }).catch(err => that.$error(err, () => {
                        that.padding = false;
                    }));
                });

            }
        }
    }
</script>

<style scoped>
    .order-editor {
        position: relative;
    }
    .customer-info .ivu-form-item {
        margin-bottom: 0px;
    }

    .order-payment-collapse {
        margin: 20px auto;
    }
    .order-payment-item {
        display: flex;
    }
    .order-payment-container {
        display: inline-block;
        width: 86px;
        height: 86px;
        text-align: center;
        line-height: 60px;
        border: 1px solid #CCC;
        border-radius: 5px;
        overflow: hidden;
        background: #fff;
        position: relative;
        box-shadow: 0 2px 2px rgba(0,0,0, 0.5);
        margin-right: 20px;
    }
    .order-payment-tabs {
        width: calc(100% - 106px);
        width: -ms-calc(100% - 106px);
        width: -moz-calc(100% - 106px);
        width: -webkit-calc(100% - 106px);
    }
    .order-payment-container img {
        width: 100%;
        height: 100%;
    }
    .order-payment-container-cover {
        background: rgba(0, 0, 0, .6);
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
        position: absolute;
        display: none;
        bottom: 0;
        right: 0;
        left: 0;
        top: 0;
    }
    .order-payment-container:hover .order-payment-container-cover {
        display: block;
    }
    .order-payment-container-cover i {
        color: #fff;
        font-size: 20px;
        cursor: pointer;
        margin: 0 2px;
    }
</style>