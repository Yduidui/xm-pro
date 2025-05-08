<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-card style="margin-bottom: 5px;">
        <el-input
            v-model="data.name"
            style="width: 240px;margin-right: 5px"
            placeholder="Please input"
            clearable
        />
        <el-button :icon="Search" circle @click="load"/>
        <el-button :icon="Delete" circle @click="reset"/>
      </el-card>
      <el-card style="margin-bottom: 5px;">
        <el-button type="primary" @click="handleAdd">新增</el-button>
        <el-button type="success" @click="delBatch">批量删除</el-button>
        <el-button type="info">Info</el-button>
        <el-button type="warning">Warning</el-button>
      </el-card>
      <el-card>
        <el-table :data="data.tabledata" stripe @selection-change="handleSelectionChange">
          <el-table-column type="selection"  width="55" />
          <el-table-column label="账号" prop="username"></el-table-column>
          <el-table-column label="头像" prop="avatar">
            <template #default="scope">
              <div style="display: flex; align-items: center; height: 100%;">
                <img v-if="scope.row.avatar" :src="scope.row.avatar" style="width: 40px; border-radius: 50%; height: 40px;">
              </div>
            </template>
          </el-table-column>
          <el-table-column label="姓名" prop="name"></el-table-column>
          <el-table-column label="操作" >
            <template #default="scope">
              <el-button @click="handleUpdate(scope.row)" type="primary" :icon="Edit" circle></el-button>
              <el-button @click="del(scope.row.id)" type="danger" :icon="Delete" circle></el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 15px">
          <el-pagination
              v-model:current-page="data.pageNum"
              v-model:page-size="data.pageSize"
              :page-sizes="[5, 10, 15, 20]"
              :size="data.pageSize"
              :disabled="disabled"
              :background="background"
              layout="total, sizes, prev, pager, next, jumper"
              :total="data.total"
              @size-change="load"
              @current-change="load"
          />
        </div>
      </el-card>
    </div>

    <!-- Dialog 弹窗 -->
    <el-dialog title="管理员信息" v-model="data.formVisible" width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding-right: 30px">
        <div style="width: 100%; display: flex; justify-content: center; ">
          <el-form-item prop="avatar" style="margin-left: -40px;">
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
          <el-input v-model="data.form.username" autocomplete="off" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="姓名" >
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入姓名"/>
        </el-form-item>
      </el-form>

      <!-- footer 插槽 -->
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import {Search, Delete, Edit} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref();

const data = reactive({
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

//当上传成功后，handleAvatarSuccess 被调用，并接收服务器的响应数据。
//该函数将新头像的 URL 更新到 data.form.avatar 中。
//由于 data.form.avatar 在模板中被用作头像图片的 src，更新后头像会实时显示在页面上。
const handleAvatarSuccess = (res) =>{
  data.form.avatar = res.data
}

// 异步请求
const load = () => {
  request.get('/admin/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
    }
  }).then((res) => {
    data.tabledata = res.data.list;
    data.total = res.data.total;
  });
};
load();

const reset = () => {
  data.name = null;
  load();
};

const handleAdd = () => {
  data.formVisible = true;
  data.form = {};   //清空，以免脏数据
};

const handleUpdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row));    //深拷贝一个新的对象 用于编辑

  data.formVisible = true;
};

const save = () =>{   //在一个保存方法里做两个操作，一个是新增，一个是编辑
  formRef.value.validate(valid => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const add = () =>{
  request.post('/admin/add',data.form).then((res) => {     //新增的对象没有id
    if(res.code === '200'){
      data.formVisible = false;
      ElMessage.success('操作成功')
      load()    //新增后加载最新的数据
    }else{
      ElMessage.error(res.msg);
    }
  })
}
const update = () =>{
  request.put('/admin/update',data.form).then((res) => {    //编辑的对象有id
    if(res.code === '200'){
      data.formVisible = false;
      ElMessage.success('操作成功')
      load()    //新增后加载最新的数据
    }else{
      ElMessage.error(res.msg);
    }
  })
}

const del = (id) =>{
  ElMessageBox.confirm('删除数据后无法恢复', '删除确认', {type: 'warning'}).then(() => {
    request.delete(`/admin//` + id).then((res) => {
      if(res.code === '200'){
        ElMessage.success('操作成功')
        load()    //删除后加载最新的数据
      }else{
        ElMessage.error(res.msg);
      }
    })
  }).catch()
}

const handleSelectionChange =(rows)=>{      //返回所有选中的行对象数组
  console.log(rows)
  //从选中的行数组中，取出所有行的id组成一个新的数组
  data.ids = rows.map(row => row.id)
  console.log(data.ids)
}

const delBatch=() =>{
  if(data.ids.length === 0){
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除数据后无法恢复', '删除确认', {type: 'warning'}).then(() => {
    request.delete('/admin/deleteBatch', {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()    //删除后加载最新的数据
      } else {
        ElMessage.error(res.msg);
      }
    })

  }).catch()
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