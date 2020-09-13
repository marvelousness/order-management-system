<template>
    <div>
        订单列表
    </div>
</template>

<script>
    export default {
        data() {
            return {
                loading: false
            };
        },
        mounted() {
            this.load();
        },
        methods: {
            load() {
                if(this.loading) {
                    return;
                }
                this.loading = true;
                API.getDepartmentTree().then((response) => {
                    this.loading = false;
                }).catch(err => this.$error(err, () => {
                    this.loading = false;
                }));
            }
        }
    }
</script>

<style scoped></style>