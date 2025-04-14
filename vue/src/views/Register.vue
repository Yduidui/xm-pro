<template>
  <div class="login-container">
    <el-card class="login-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h1 class="title">欢 迎 注 册</h1>
          <p class="subtitle">请注册您的账户</p>
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

        <el-form-item label="工号" prop="jobnum">
          <el-input
              size="large"
              v-model="data.form.jobnum"
              placeholder="请输入工号"
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

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
              size="large"
              v-model="data.form.confirmPassword"
              type="password"
              placeholder="请确认密码"
              :prefix-icon="Lock"
              show-password
          />
        </el-form-item>

        <el-button
            @click="register"
            class="login-btn"
            type="primary"
            :icon="Key"
        >
          注 册
        </el-button>
      </el-form>

      <div class="additional-links">
        <el-link type="primary" :underline="false">已有帐号？</el-link>
        <span class="divider">|</span>
        <el-link type="primary" :underline="false">立即登录</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock, Key } from '@element-plus/icons-vue'
import { ElMessage } from "element-plus";
import request from "@/utils/request.js";

const formRef = ref();

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== data.form.password) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
};

const data = reactive({
  form: {
    username: '',
    password: '',
    confirmPassword: ''
  },
  rules: {
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
      { min: 4, max: 16, message: '用户名长度在4到16个字符', trigger: 'blur' }
    ],
    jobnum: [
      { required: true, message: '请输入工号', trigger: 'blur' },
      { min: 4, max: 16, message: '工号长度在4到16个字符', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur' }
    ],
    confirmPassword: [
      { required: true, validator: validatePass, trigger: 'blur' }
    ]
  }
});

const register = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/register', data.form).then((res) => {
        if (res.code === '200') { // 注册成功
          ElMessage.success('注册成功');
          setTimeout(() => {
            location.href = '/login';
          }, 500);
        } else {
          ElMessage.error(res.msg);
        }
      }).catch((error) => {
        ElMessage.error('注册失败，请检查网络或服务器状态');
      });
    }
  });
};
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

@keyframes gradientBG {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

@media (max-width: 480px) {
  .login-card {
    width: 90%;
  }
}
</style>