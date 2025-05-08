<template>

    <el-card style="margin-bottom: 5px;width: 50%">
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px"  style="padding-right: 30px">
        <div style="width: 100%; display: flex; justify-content: center; ">
          <el-form-item style="margin-left: -40px;">
            <el-upload
                class="avatar-uploader"
                action="http://localhost:8080/files/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
            >
              <img v-if="data.form.avatar" :src="data.form.avatar" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
        </div>

        <el-form-item label="用户名" prop="username">
          <el-input disabled v-model="data.form.username" autocomplete="off" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="姓名" >
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入姓名"/>
        </el-form-item>

        <div v-if="data.user.role === 'EMP'">
          <el-form-item label="性别" >
            <el-radio-group v-model = "data.form.sex">
              <el-radio value="男" label="男"></el-radio>
              <el-radio value="女" label="女"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="工号" >
            <el-input disabled v-model="data.form.jobnum" autocomplete="off" placeholder="请输入工号"/>
          </el-form-item>
          <el-form-item label="年龄" >
            <el-input-number v-model="data.form.age" :min="18" style="width: 170px" autocomplete="off" placeholder="请输入年龄"/>
          </el-form-item>
          <el-form-item label="个人介绍" >
            <el-input :rows="3" type="textarea" v-model="data.form.descr" autocomplete="off" placeholder="请输入个人介绍"/>
          </el-form-item>
        </div>

        <div style="text-align: center">
          <el-button @click = "updateUser" type="primary">更新个人信息</el-button>
        </div>

      </el-form>
    </el-card>



</template>

<script setup>
import { reactive, ref } from "vue";
import {Search, Delete, Edit} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref();

const data = reactive({
  user : JSON.parse(localStorage.getItem('xm-pro-user')),
  name: null,
  tabledata: [],
  pageNum: 1,
  pageSize: 10,
  total: 47,
  formVisible: false,
  form: [],
  ids:[],
  rules:{
    username:{
      required: true,
      message: "请输入账号",
      trigger: "blur"
    }

  }
});

const emit =  defineEmits(['updateUser'])

const updateUser = () => {
  if (data.user.role === 'EMP') {
    request.put('/employee/update', data.form).then((res) => {
      console.log('更新返回的数据:', res.data);
      if (res.code === '200') {
        ElMessage.success('更新成功')
        // 更新缓存数据
        localStorage.setItem('xm-pro-user', JSON.stringify(res.data));
        emit('updateUser')
      } else {
        ElMessage.error(res.msg)
      }
    });
  } else {
    request.put('/admin/update', data.form).then((res) => {
      console.log('更新返回的数据:', res.data);
      if (res.code === '200') {
        ElMessage.success('更新成功')
        // 更新缓存数据
        localStorage.setItem('xm-pro-user', JSON.stringify(res.data))
        emit('updateUser')
      } else {
        ElMessage.error(res.msg) // 添加错误处理，确保用户知道更新失败
      }
    })
  }
}
const handleAvatarSuccess = (res) =>{
  data.form.avatar = res.data
}

if(data.user.role === 'EMP'){
  request.get('/employee/selectById/'+ data.user.id).then((res) => {
    data.form = res.data
  })
}else if(data.user.role === 'ADMIN'){
  data.form= data.user
}
</script>


<style scoped>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>