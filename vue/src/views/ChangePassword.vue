<template>
  <el-card style="margin-bottom: 5px; width: 50%;">
    <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="100px" style="padding-right: 30px">
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input v-model="data.form.password" type="password" placeholder="请输入旧密码" show-password />
      </el-form-item>

      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="data.form.newPassword" type="password" placeholder="请输入新密码" show-password />
      </el-form-item>

      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="data.form.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
      </el-form-item>

      <div style="text-align: center;">
        <el-button type="primary" @click="updatePassword">确认修改</el-button>
        <el-button @click="reset">重置</el-button>
      </div>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import request from '@/utils/request.js';

const formRef = ref();

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请再次确认密码'));
  } else if (value !== data.form.newPassword) {  // 这里应该是 newPassword，而不是 oldPassword
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
};


const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-pro-user')),
  role: JSON.parse(localStorage.getItem('xm-pro-user')),
  form: {
    Password: '',
    newPassword: '',
    confirmPassword: ''
  },
  rules: {
    password: [
      { required: true, message: '请输入旧密码', trigger: 'blur' }
    ],
    newPassword: [
      { required: true, message: '请输入新密码', trigger: 'blur' },
      { min: 6, message: '新密码不能少于6位', trigger: 'blur' }
    ],
    confirmPassword: [
      {validator: validatePass,trigger:'blur'}
    ],
  }
});

const updatePassword = () => {
  data.form.id = data.user.id
  data.form.role = data.user.role
  formRef.value.validate((valid) => {
    if (valid) {
      request.put('/updatePassword', data.form).then((res) => {
        if (res.code === '200') {
          ElMessage.success('修改成功')
          localStorage.removeItem('xm-pro-user')
          setTimeout(() => {
            location.href = '/login';
          },500)

        }else{
          ElMessage.error(res.msg)
        }
      })
    }
  })

};

const reset = () => {
  data.form.password = '';
  data.form.newPassword = '';
  data.form.confirmPassword = '';
};
</script>

<style scoped>
</style>
