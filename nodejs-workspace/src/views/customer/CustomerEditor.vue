<template>
    <div class="customer-editor">
        <Spin fix v-if="loading">加载中...</Spin>

        <Form :model="form" :label-width="120">
            <FormItem label="名称：">
                <Input v-model="form.name" placeholder="请输入客户名称" />
            </FormItem>
            <FormItem label="昵称：">
                <Input v-model="form.nick" placeholder="请输入客户昵称" />
            </FormItem>
            <FormItem label="旺旺：">
                <Input v-model="form.wangwang" placeholder="请输入客户的旺旺号" />
            </FormItem>
            <FormItem label="QQ：">
                <Input v-model="form.qq" placeholder="请输入客户的QQ号" />
            </FormItem>
            <FormItem label="微信：">
                <Input v-model="form.wechat" placeholder="客户的微信号" />
            </FormItem>
            <FormItem label="手机：">
                <Input v-model="form.phone" placeholder="客户的手机号" />
            </FormItem>
            <FormItem label="地域：">
            <Select v-model="form.region" filterable>
                <OptionGroup :label="p.name" v-for="p in area.data" :key="p.id">
                    <Option v-for="c in p.children" :key="c.id" :value="c.name">{{c.name}}</Option>
                </OptionGroup>
            </Select>
            </FormItem>
            <FormItem label="备注内容：">
                <Input v-model="form.remark" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="请输入备注内容" />
            </FormItem>
            <FormItem>
                <Button type="primary" size="large" :loading="padding" @click="submit">保存</Button>
            </FormItem>
        </Form>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                form: {
                    name: "",
                    nick: "",
                    wangwang: "",
                    qq: "",
                    wechat: "",
                    phone: "",
                    region: "",
                    remark: ""
                },
                area: {
                    data: []
                },
                padding: false,
                loading: false
            };
		},
		computed: {
			id: {
				get() {
					return this.$route.params.id;
				}
			}
		},
        mounted() {
            this.load();
        },
        methods: {
            load() {
                if (this.loading) {
                    return;
                }
                let that = this;
                let ps = [new Promise(function(resolve) {
                    API.getAreaTree().then((response) => {
                        that.area.data = response && response.data instanceof Array ? response.data : [];
                        resolve(true);
                    }).catch((err) => {
                        that.$error(err);
                        resolve(false);
                    });
                }), new Promise(function(resolve) {
					if(!that.id) {
						resolve(false);
						return;
					}
                    API.getCustomer({
						id: that.id
					}).then((response) => {
						that.form = response && response.data ? response.data : {};
                        resolve(true);
                    }).catch((err) => {
                        that.$error(err);
                        resolve(false);
                    });
                })];

                this.loading = true;
                Promise.all(ps).then(() => this.loading = false);
            },
            submit() {
                let customer = JSON.parse(JSON.stringify(this.form));
                let hasValue = false;
                for (let k in customer) {
                    if(customer[k]) {
                        hasValue = true;
                        break;
                    }
                }
                if(!hasValue) {
                    this.$Message.error("最少录入一条信息");
                    return;
                }
                this.padding = true;
                API.postCustomerSave(this.form).then((response) => {
                    if(response && response.data > 0) {
                        this.$Message.success("保存客户信息成功！");
                    }
                    this.$router.push({
                        name: "CustomerList"
                    });
                    this.padding = false;
                }).catch((err) => this.$error(err, this.padding = false));
            }
        },
    };
</script>

<style scoped>
    .customer-editor {
        position: relative;
    }
</style>