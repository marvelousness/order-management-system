<template>
    <div>
        <DepartmentTree :data="departments" />
    </div>
</template>

<script>
    export default {
        data() {
            return {
                departments: [],
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
                    this.departments = response && response.data instanceof Array ? response.data : [];
                    this.loading = false;
                }).catch(err => this.$error(err, () => {
                    this.loading = false;
                }));
            }
        }
    }
</script>

<style scoped></style>