<template>
  <el-container class="not-found">
    <el-header>
      <h1>404</h1>
    </el-header>
    <el-main>
      <el-card class="card" shadow="always">
        <p class="message">抱歉，您访问的页面找不到。</p>
        <el-button type="primary" @click="goHome">返回首页</el-button>
      </el-card>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: "NotFound",
  methods: {
    goHome() {
      this.$router.push({ path: '/manager/home' }); // 返回首页
    }
  }
}
</script>

<style scoped>
.not-found {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f5f7fa;
  text-align: center;
}

.card {
  padding: 20px;
  max-width: 400px;
  margin: auto;
}

.message {
  font-size: 1.5rem;
  margin: 1rem 0;
}
</style>