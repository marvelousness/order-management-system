<template>
    <div class="department-container">
        <Dt :data="data" />
        <!-- <canvas id="ap" class="department-arrow-panel"></canvas> -->
    </div>
</template>

<script>
import Dt from "./DepartmentTree";

export default {
    components: {
        Dt
    },
    props: {
        data: {
            type: Array,
            required: true
        }
    }
}
</script>

<style scoped>
.department-container {
    position: relative;
}
.department-arrow-panel {
    position: absolute;
    width: 100%;
    height: 100%;
    z-index: 1;
    top: 0;
    left: 0;
}
</style>