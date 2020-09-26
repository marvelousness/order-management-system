<template>
    <div class="dashboard-container">
        <div class="dashboard-header">
            <Card class="dashboard-card">
                <div class="dashboard-card-title" slot="title">
                    收入
                </div>
                <div class="dashboard-card-body">
                    <div class="dashboard-card-money">{{thisYearIncome}}</div>
                    <div class="dashboard-card-desc">
                       <div class="dashboard-card-desc-label">今年总收入</div>
                       <div class="dashboard-card-desc-status">
                           <span>{{statistics.ratio.income}}%</span>
                           <Icon type="md-trending-up" v-if="statistics.ratio.income > 0"/>
                           <Icon type="md-trending-down" v-if="statistics.ratio.income < 0"/>
                       </div>
                    </div>
                </div>
            </Card>
            
            <Card class="dashboard-card">
                <div class="dashboard-card-title" slot="title">
                    订单
                </div>
                <div class="dashboard-card-body">
                    <div class="dashboard-card-money">{{thisYearNewOrder}}</div>
                    <div class="dashboard-card-desc">
                       <div class="dashboard-card-desc-label">新订单</div>
                       <div class="dashboard-card-desc-status">
                           <span>{{statistics.ratio.order}}%</span>
                           <Icon type="md-trending-up" v-if="statistics.ratio.order > 0"/>
                           <Icon type="md-trending-down" v-if="statistics.ratio.order < 0"/>
                       </div>
                    </div>
                </div>
            </Card>
            
            <Card class="dashboard-card">
                <div class="dashboard-card-title" slot="title">
                    客户
                </div>
                <div class="dashboard-card-body">
                    <div class="dashboard-card-money">{{thisYearNewCustomer}}</div>
                    <div class="dashboard-card-desc">
                       <div class="dashboard-card-desc-label">新客户</div>
                       <div class="dashboard-card-desc-status">
                           <span>{{statistics.ratio.customer}}%</span>
                           <Icon type="md-trending-up" v-if="statistics.ratio.customer > 0"/>
                           <Icon type="md-trending-down" v-if="statistics.ratio.customer < 0"/>
                       </div>
                    </div>
                </div>
            </Card>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            statistics: {
                loading: false,
                thisYear: {
                    order: 0,
                    income: 0,
                    customer: 0
                },
                lastYear: {
                    order: 0,
                    income: 0,
                    customer: 0
                },
                ratio: {
                    order: 0,
                    income: 0,
                    customer: 0
                }
            }
        }
    },
    computed: {
        thisYearIncome: {
            get() {
                let value = this.statistics.thisYear.income + "";
                let vs = [];
                let length = 0;
                let index = value.lastIndexOf(".");
                let i = value.length;
                if(index > 1) {
                    vs.push(value.substr(index));
                    i = index - 1;
                }
                while (i > -1) {
                    vs.unshift(value[i]);
                    length++;
                    if(length % 3 == 0 && i > 1) {
                        vs.unshift(",");
                    }
                    i--;
                }
                return vs.length > 0 ? vs.join("") : "";
            }
        },
        thisYearNewOrder: {
            get() {
                let value = this.statistics.thisYear.order + "";
                let vs = [];
                let length = 0;
                let index = value.lastIndexOf(".");
                let i = value.length;
                if(index > 1) {
                    vs.push(value.substr(index));
                    i = index - 1;
                }
                while (i > -1) {
                    vs.unshift(value[i]);
                    length++;
                    if(length % 3 == 0 && i > 1) {
                        vs.unshift(",");
                    }
                    i--;
                }
                return vs.length > 0 ? vs.join("") : "";
            }
        },
        thisYearNewCustomer: {
            get() {
                let value = this.statistics.thisYear.customer + "";
                let vs = [];
                let length = 0;
                let index = value.lastIndexOf(".");
                let i = value.length;
                if(index > 1) {
                    vs.push(value.substr(index));
                    i = index - 1;
                }
                while (i > -1) {
                    vs.unshift(value[i]);
                    length++;
                    if(length % 3 == 0 && i > 1) {
                        vs.unshift(",");
                    }
                    i--;
                }
                return vs.length > 0 ? vs.join("") : "";
            }
        }
    },
    mounted() {
        this.load();
    },
    methods: {
        load() {

            // 1. 加载统计结果
            this.statistics.loading = true;
            API.getstatistics().then((response) => {
                if(response && response.data) {
                    let data = response.data;
                    this.statistics.thisYear.income = data.thisYearIncome > 0 ? data.thisYearIncome : 0;
                    this.statistics.thisYear.order = data.thisYearNewOrder > 0 ? data.thisYearNewOrder : 0;
                    this.statistics.thisYear.customer = data.thisYearNewCustomer > 0 ? data.thisYearNewCustomer : 0;
                    
                    this.statistics.lastYear.income = data.lastYearIncome > 0 ? data.lastYearIncome : 0;
                    this.statistics.lastYear.order = data.lastYearNewOrder > 0 ? data.lastYearNewOrder : 0;
                    this.statistics.lastYear.customer = data.lastYearNewCustomer > 0 ? data.lastYearNewCustomer : 0;
                    
                    this.statistics.ratio.income = data.growthRatioIncome > 0 ? data.growthRatioIncome : 0;
                    this.statistics.ratio.order = data.growthRatioNewOrder > 0 ? data.growthRatioNewOrder : 0;
                    this.statistics.ratio.customer = data.growthRatioNewCustomer > 0 ? data.growthRatioNewCustomer : 0;
                }
            }).catch((err) => this.$error(err, () => {
                this.statistics.loading = false;
            }));
            // 2. 加载折线图
        }
    }
}
</script>

<style scoped>
    .dashboard-container {
        margin: 20px;
    }
    .dashboard-header{
        display: flex;
        justify-content: space-between;
    }
    .dashboard-card {
        width: 30%;
    }
    .dashboard-card-money {
        font-size: 20px;
        font-weight: 600;
    }
    .dashboard-card-desc {
        display: flex;
        justify-content: space-between;
    }
</style>