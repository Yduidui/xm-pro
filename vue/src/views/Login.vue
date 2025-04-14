<template>
  <div class="login-container">
    <el-card class="login-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h1 class="title">欢迎回来</h1>
          <p class="subtitle">请登录您的账户</p>
        </div>
      </template>

      <el-form
          ref="formRef"
          :rules="data.rules"
          :model="data.form"

          label-position="top"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
              size="large"
              v-model="data.form.username"
              placeholder="请输入用户名"
              :prefix-icon="User"
              clearable
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
              size="large"
              v-model="data.form.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
          />
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-select v-model="data.form.role" size="large">
            <el-option value="ADMIN" label="管理员"></el-option>
            <el-option value="EMP" label="员工"></el-option>
          </el-select>
        </el-form-item>

        <el-button
            @click="login"
            class="login-btn"
            type="primary"
            :icon="Key"
        >
          登 录
        </el-button>
      </el-form>

      <div class="additional-links">
        <el-link type="primary" :underline="false">忘记密码？</el-link>
        <span class="divider">|</span>
        <el-link type="primary" :underline="false" @click="goToRegister">注册新账户</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock, Key } from '@element-plus/icons-vue'
import {ElMessage} from "element-plus";
import request from "@/utils/request.js";
import { useRouter } from 'vue-router';

const router = useRouter();

const goToRegister = () => {
  router.push('/register'); // 跳转到注册页面
};
const formRef = ref();

const data = reactive({
  form:{role:"ADMIN"},
  rules:{
    username:[
      {required: true, message: "请输入账号", trigger: "blur"}
    ],
    password:[
      {required: true, message: "请输入密码", trigger: "blur"}
    ],

    }
  }
)

const login = ()=>{
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then((res) => {
        if (res.code === '200'){   //登录成功
          //存储后台用户信息
          localStorage.setItem('xm-pro-user', JSON.stringify(res.data))  //将json对象转化为json字符串
          ElMessage.success('登录成功')
          setTimeout(()=>{
            location.href = '/manager/home'
          },500)


        } else{
          ElMessage.error(res.msg)
        }
      })
    }
  })
}


</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #6CB4EE 0%, #0096FF 100%);
  animation: gradientBG 15s ease infinite;
}

.login-card {
  width: 400px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.95);
  transition: transform 0.3s ease;
}

.login-card:hover {
  transform: translateY(-5px);
}

.card-header {
  text-align: center;
}

.title {
  color: #2d3748;
  font-size: 1.8rem;
  margin-bottom: 0.5rem;
}



.subtitle {
  color: #718096;
  margin-bottom: 0;
}

.card-header{
  border-bottom: none;
  padding-bottom: 0;
}

.login-btn {
  width: 100%;
  margin-top: 10px;
  background: linear-gradient(135deg, #6CB4EE 0%, #0096FF 100%);
  border: none;
  height: 45px;
  font-size: 1rem;
  font-weight: 600;
  letter-spacing: 2px;
}

.additional-links {
  margin-top: 1.5rem;
  display: flex;
  justify-content: center;
  gap: 1rem;
  color: #718096;
}

.divider {
  color: #cbd5e0;
}

/* 保持原有动画 */
@keyframes cardEnter { /* ... */ }
@keyframes gradientBG { /* ... */ }

@media (max-width: 480px) {
  .login-card {
    width: 90%;
  }
}
</style>